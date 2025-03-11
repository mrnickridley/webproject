package webproject.webproject.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import webproject.webproject.models.payPalModel;
import webproject.webproject.repositories.paypalRepo;
import webproject.webproject.services.payPalService;

@RestController
@RequestMapping("/paypal-api")
public class paypalController {
    paypalRepo repo;
    private final payPalService serv;

    public paypalController(payPalService service, paypalRepo repo){
        this.serv = service;
        this.repo = repo;
    }

    @PostMapping("/createOrder")
    @CrossOrigin
    /*"ResponseEntity" wraps the reponse body and HTTP status, making it more flexible for setting response data
     * in the parameter, "ResponseBody" tells Spring to map the incoming JSON payload in the request body to the Map<String, String> object.
     *  "Map<String, String> " stores the key-value pairs from the JSON payload sent by the client
     */
    public ResponseEntity<String> createOrder(@RequestBody Map<String, String> request){
        try{
            /*extracts the amount, currency, description, and payerEmail values from the request map */
            String amount = request.get("amount");
            String currency = request.get("currency");
            String description = request.get("description");
            String payerEmail = request.get("payeremail");

            // Call the PayPalService to create the order
            /*invokes my service method which interacts with PayPal's API */
            String responseToOrder = serv.orderCreation(amount, currency, description, payerEmail);

            return ResponseEntity.ok(responseToOrder);// Return PayPal's response
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/captureTransaction/{orderId}")
    @CrossOrigin
    public ResponseEntity<String> captureOrder(@PathVariable String orderId){
        try{
            String captureResponse = serv.orderCapture(orderId);
            return ResponseEntity.ok(captureResponse);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to capture order: " + e.getMessage());
        }
    }

    @GetMapping("/getAllOrders")
    @CrossOrigin
    public List<payPalModel> getAllOrders(){
        return repo.findAll();
    }


    @GetMapping("/getOrderId/{orderId}")
    @CrossOrigin
    public payPalModel getOrderId(@PathVariable String orderId){
        return repo.findByOrderId(orderId);
    }

}
