package com.example.Project1.controller.product_customer;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestBody;







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
    public String getProduct(@RequestParam int id, Model model) {
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
    public String remove(@RequestParam int id, Model model) {
        CartItem cartItem = cartItemRepository.findByCartItemID(id);
        cartItemRepository.delete(cartItem);
        return "redirect:/product_customer/shoppingcart";
    }
    @GetMapping("/addtocart")
    public String addtocart(@RequestParam int id, Model model, RedirectAttributes redirectAttributes, @RequestParam int quantity) {
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
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            redirectAttributes.addFlashAttribute("message1", "Add to cart successfully");
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
            redirectAttributes.addFlashAttribute("message2", "Vua them " + quantity + " san pham " + productRepository.findById(id).get().getProductName() + " vao gio hang");
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
    public String postMethodName(Model model, @ModelAttribute CartWrapper cartWrapper, RedirectAttributes redirectAttributes) {
        // for(CartItem cartItem : cartItems){
        //     cartItemRepository.save(cartItem);
        // }
        StringBuilder error = new StringBuilder("Mot so san pham khong du hang: ");
        boolean outOfStock = false;
        for(CartItem cartItem : cartWrapper.getCartItems()){
            Product product = productRepository.findById(cartItem.getProductID()).orElse(null);
            if(product == null){
                outOfStock = true;
                error.append("productID: " + cartItem.getProductID() + " khong ton tai.\n ");
            }
            else if(cartItem.getQuantity() > product.getStockQuantity()){
                outOfStock = true;
                error.append(product.getProductName())
                    .append(" chi con ")
                    .append(product.getStockQuantity())
                    .append(" san pham.\n")
                    .append("So luong ban dat la: ")
                    .append(cartItem.getQuantity())
                    .append("\n");
            }
        }
        if(outOfStock){
            
            redirectAttributes.addFlashAttribute("error", error.toString());
            return "redirect:/product_customer/shoppingcart";
        }

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
            Product product = productRepository.findById(cartItem.getProductID()).get();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }
        orderItemWrapper.setOrderItems(orderItems);
        model.addAttribute("orderItemWrapper", orderItemWrapper);
        
        // model.addAttribute("orderItems", orderItems);
        return "user/checkoutpage";
    }
    @GetMapping("/checkoutpage")
    public String checkoutpage(Model model, @ModelAttribute Orders order, @ModelAttribute CartWrapper cartWrapper) {
        
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
    public String checkout(Model model, @ModelAttribute OrderItemWrapper orderItemWrapper, @ModelAttribute CartWrapper cartWrapper) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        WebUser user = userRepository.findByEmail(email);
        int userID = user.getUserID();
        Orders order = orderItemWrapper.getOrder();
        order.setUserID(userID);
        order.setStatus("Chờ xác nhận");
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
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(query);
        return ResponseEntity.ok(products);
    }

    // @PostMapping("/evaluate")
    // public String evaluate(@RequestParam("productID") int id, @RequestParam String evaluateContent, Model model) {
    //     Evaluate_Product evaluate = new Evaluate_Product();
    //     evaluate.setProductID(id);
    //     evaluate.setEvaluateContent(evaluateContent);
    //     evaluateProductRepository.save(evaluate);
    //     return "redirect:/product_customer/product?id=" + id;
    // }

    @GetMapping("/getEvaluates")
    public ResponseEntity<List<Evaluate_Product>> getEvaluate(@RequestParam int productID) {
        List<Evaluate_Product> evaluates = evaluateProductRepository.findByProductID(productID);
        return ResponseEntity.ok(evaluates);
    }   
    
    @PostMapping("/evaluate")
    public ResponseEntity<String> saveEvaluate(
        @RequestParam("productID") int productID,
        @RequestParam("evaluateContent") String evaluateContent) {
    try {
        // Tạo đối tượng đánh giá
        Evaluate_Product evaluation = new Evaluate_Product();
        evaluation.setProductID(productID);
        evaluation.setEvaluateContent(evaluateContent);

        // Lưu vào database
        evaluateProductRepository.save(evaluation);

        return new ResponseEntity<>("Đánh giá đã được lưu thành công!", HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("Lỗi khi lưu đánh giá", HttpStatus.BAD_REQUEST);
    }
}
    
}
