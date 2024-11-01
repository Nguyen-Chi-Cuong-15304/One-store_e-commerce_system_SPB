// package com.example.Project1.controller.Payment;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.Project1.service.PaypalService;
// import com.paypal.api.payments.Links;
// import com.paypal.api.payments.Payment;
// import com.paypal.base.rest.PayPalRESTException;

// @RestController
// @RequestMapping("/api/payment")
// public class PaymentController {

//     @Autowired
//     PaypalService paypalService;

//     @PostMapping("/create")
//     public ResponseEntity<String> createPayment(
//             @RequestParam("price") String price,
//             @RequestParam("currency") String currency,
//             @RequestParam("method") String method,
//             @RequestParam("description") String description) {
//         try {
//             Payment payment = paypalService.createPayment(
//                 Double.parseDouble(price), 
//                 currency, 
//                 method, 
//                 "sale",
//                 description, 
//                 "http://localhost:8080/api/payment/cancel",
//                 "http://localhost:8080/api/payment/success");
            
//             for(Links links : payment.getLinks()){
//                 if(links.getRel().equals("approval_url")) {
//                     return ResponseEntity.ok(links.getHref());
//                 }
//             }
//         } catch (PayPalRESTException e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                .body("Error occurred: " + e.getMessage());
//         }
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                            .body("Error occurred");
//     }

//     @GetMapping("/success")
//     public ResponseEntity<String> successPay(
//             @RequestParam("paymentId") String paymentId,
//             @RequestParam("PayerID") String payerId) {
//         try {
//             Payment payment = paypalService.executePayment(paymentId, payerId);
//             if(payment.getState().equals("approved")) {
//                 return ResponseEntity.ok("Payment successful!");
//             }
//         } catch (PayPalRESTException e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                .body("Error occurred: " + e.getMessage());
//         }
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                            .body("Payment failed");
//     }

//     @GetMapping("/cancel")
//     public ResponseEntity<String> cancelPay() {
//         return ResponseEntity.ok("Payment cancelled");
//     }
// }
