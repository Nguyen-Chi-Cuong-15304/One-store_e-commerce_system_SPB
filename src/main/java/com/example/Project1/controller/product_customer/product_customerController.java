package com.example.Project1.controller.product_customer;

import java.math.BigDecimal;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Project1.entity.Cart;
import com.example.Project1.entity.CartItem;
import com.example.Project1.entity.CartWrapper;
import com.example.Project1.entity.Evaluate_Product;
import com.example.Project1.entity.Orders;
import com.example.Project1.entity.OrderItem;
import com.example.Project1.entity.OrderItemWrapper;
import com.example.Project1.entity.Product;
import com.example.Project1.entity.WebUser;
import com.example.Project1.repository.CartItemRepository;
import com.example.Project1.repository.CartRepository;
import com.example.Project1.repository.EvaluateRepository;
import com.example.Project1.repository.OrderItemRepository;
import com.example.Project1.repository.OrderRepository;
import com.example.Project1.repository.ProductRepository;
import com.example.Project1.repository.WebUserRepository;
import org.springframework.web.bind.annotation.PostMapping;






@Controller
@RequestMapping("/product_customer")
public class product_customerController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WebUserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private EvaluateRepository evaluateProductRepository;

    @GetMapping("/product")
    public String getProduct(@RequestParam("id") int id, Model model) {
        Product product = productRepository.findById(id).get();
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        model.addAttribute("product", product);
        int productID = product.getProductID();
        List<Product> relatedProducts = productRepository.findRelatedProducts(productID);
        relatedProducts.add(product);
        model.addAttribute("relatedProducts", relatedProducts);

        List<Evaluate_Product> evaluates = evaluateProductRepository.findByProductID(productID);
        model.addAttribute("evaluates", evaluates);
        return "product";
    }
    @GetMapping("/remove")
    public String remove(@RequestParam("id") int id, Model model) {
        CartItem cartItem = cartItemRepository.findByCartItemID(id);
        cartItemRepository.delete(cartItem);
        return "redirect:/product_customer/shoppingcart";
    }
    @GetMapping("/addtocart")
    public String addtocart(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        WebUser user = userRepository.findByEmail(email);
        int userID = user.getUserID();
        Cart cart = cartRepository.findByUserID(userID);
        if(cart == null){
            cart = new Cart();
            cart.setUserID(userID);
            cartRepository.save(cart);
        }
        int cartID = cart.getCartID();
        CartItem cartItem = cartItemRepository.findByProductIDandCartID(id, cartID);
        if(cartItem == null){
            cartItem = new CartItem();
            cartItem.setCartID(cartID);
            cartItem.setProductID(id);
            cartItem.setQuantity(1);
            cartItemRepository.save(cartItem);
            redirectAttributes.addFlashAttribute("message1", "Add to cart successfully");
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.save(cartItem);
            redirectAttributes.addFlashAttribute("message2", "San pham da co trong gio hang, ban vua them 1 san pham moi");
        }
        return "redirect:/product_customer/product?id=" + id;
    }
    @GetMapping("/shoppingcart")
    public String shoppingcart(Model model) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        WebUser user = userRepository.findByEmail(email);
        int userID = user.getUserID();
        Cart cart = cartRepository.findByUserID(userID);
        int cartID = cart.getCartID();
        List<CartItem> cartItems = cartItemRepository.findByCartIDList(cartID);
        
        for(CartItem cartItem : cartItems){
            Product product = productRepository.findById(cartItem.getProductID()).get();
            cartItem.setProductName(product.getProductName());
            cartItem.setPrice(product.getPrice());
            cartItem.setLinkImg(product.getLinkImg());
            
        }
        CartWrapper cartWrapper = new CartWrapper();
        cartWrapper.setCartItems(cartItems);
        model.addAttribute("cartWrapper", cartWrapper);

        List<Product> products = new ArrayList<Product>();
        int c = 0;
        for(CartItem cartItem : cartItems){
            Product product = productRepository.findById(cartItem.getProductID()).get();
            products.add(product);
            c++;
        }
        for(int i = 0; i < c; i++){
            products.addAll(productRepository.findRelatedProducts(products.get(i).getProductID()));
        }
        //products.subList(0, c).clear();
        model.addAttribute("products", products);
        return "user/shoppingcart";
    }
    
    @PostMapping("/paynow")
    public String postMethodName(Model model, @ModelAttribute("cartWrapper") CartWrapper cartWrapper) {
        // for(CartItem cartItem : cartItems){
        //     cartItemRepository.save(cartItem);
        // }
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        WebUser user = userRepository.findByEmail(email);
        int userID = user.getUserID();
        Orders order = new Orders();
        order.setUserID(userID);
        order.setStatus("Chua thanh toan");
        order.setTotalAmount(new BigDecimal(0));
        BigDecimal totalAmount = new BigDecimal(0);
        for(CartItem cartItem : cartWrapper.getCartItems()){
            System.out.println(cartItem.getProductName());
            Product product = productRepository.findById(cartItem.getProductID()).get();
            cartItem.setProductName(product.getProductName());
            cartItem.setPrice(product.getPrice());
            cartItem.setLinkImg(product.getLinkImg());

            totalAmount = totalAmount.add(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        }
        order.setTotalAmount(totalAmount);
        Date date = new Date(System.currentTimeMillis());
        order.setOrderDate(date);
        OrderItemWrapper orderItemWrapper = new OrderItemWrapper();
        orderItemWrapper.setOrder(order);
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for(CartItem cartItem : cartWrapper.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderID(order.getOrderID());
            orderItem.setProductID(cartItem.getProductID());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setLinkImg(cartItem.getLinkImg());
            orderItems.add(orderItem);
        }
        orderItemWrapper.setOrderItems(orderItems);
        model.addAttribute("orderItemWrapper", orderItemWrapper);
        
        // model.addAttribute("orderItems", orderItems);
        return "user/checkoutpage";
    }
    @GetMapping("/checkoutpage")
    public String checkoutpage(Model model, @ModelAttribute("order") Orders order, @ModelAttribute("cartWrapper") CartWrapper cartWrapper) {
        
        if(!model.containsAttribute("orderItemWrapper")){
            List<OrderItem> orderItems = new ArrayList<OrderItem>();
            for(CartItem cartItem : cartWrapper.getCartItems()){
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderID(order.getOrderID());
                orderItem.setProductID(cartItem.getProductID());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setProductName(cartItem.getProductName());
                orderItem.setPrice(cartItem.getPrice());
                orderItem.setLinkImg(cartItem.getLinkImg());
                orderItems.add(orderItem);
            }
            BigDecimal totalAmount = new BigDecimal(0);
            for(OrderItem orderItem : orderItems){
                totalAmount = totalAmount.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
            }
            order.setTotalAmount(totalAmount);
            OrderItemWrapper orderItemWrapper = new OrderItemWrapper();
            orderItemWrapper.setOrderItems(orderItems);
            orderItemWrapper.setOrder(order);
            model.addAttribute("orderItemWrapper", orderItemWrapper);
            model.addAttribute("order", order);
        }

        return "user/checkoutpage";
       
    }
    @PostMapping("/checkout")
    public String checkout(Model model, @ModelAttribute("orderItemWrapper") OrderItemWrapper orderItemWrapper, @ModelAttribute("cartWrapper") CartWrapper cartWrapper) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        WebUser user = userRepository.findByEmail(email);
        int userID = user.getUserID();
        Orders order = orderItemWrapper.getOrder();
        order.setUserID(userID);
        order.setStatus("Dang xu ly");
        order.setOrderDate(new Date(System.currentTimeMillis()));
        
        Date date = order.getOrderDate();
        orderRepository.save(order);
        Orders order1 = orderRepository.findByOrderDate(date);
        int orderID = order1.getOrderID();
        
        BigDecimal totalAmount = new BigDecimal(0);
        for(OrderItem orderItem : orderItemWrapper.getOrderItems()){
            orderItem.setOrderID(orderID);
            orderItemRepository.save(orderItem);
            totalAmount = totalAmount.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        
        return "redirect:/product_customer/shoppingcart";
    }

    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("query") String query) {
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(query);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/evaluate")
    public String evaluate(@RequestParam("productID") int id, @RequestParam("evaluateContent") String evaluateContent, Model model) {
        Evaluate_Product evaluate = new Evaluate_Product();
        evaluate.setProductID(id);
        evaluate.setEvaluateContent(evaluateContent);
        evaluateProductRepository.save(evaluate);
        return "redirect:/product_customer/product?id=" + id;
    }
    
}
