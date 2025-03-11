package webproject.webproject.services;


import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import webproject.webproject.models.payPalModel;
import webproject.webproject.models.payPalTransactionModel;
import webproject.webproject.repositories.payPalTransactionRepo;
import webproject.webproject.repositories.paypalRepo;

/*In order to inject Paypal into our program, we must first retrieve a token from Paypal's API, which is the purpose of the "myToken()" method. 
PayPal requires this token to authenticate API calls, to ensure that requests come from a trusted source.
 *-Tokens are essential for securely identifyng my app when calling PayPal's services.

After the token is retrieved, we then create an order, which defines an order payment detail and specifies the intent of the order.
This process separates the client-side and server-side responsibilities.

once the order is created, Paypal will send the frontend a link to redirect the user to PayPal's platform to approve their order.
Redirecting the user to PayPal ensures the user interacts with PayPal's secure interface to confirm payment details.

Once the user approves the payment, Paypal redirects the user to your return URL with the order ID.
with this OrderId, we capture the payment.
Capturing the payment finalizes the transaction, transferring the money into our PayPal business account.
 */


@Service
public class payPalService {

    @Value ("${paypal.client-id}")
    private String idClient;

    @Value ("${paypal.client-secret}")
    private String secretClient;

    @Value("${paypal.api-url}")
    private String ppApiUrl;

    private final RestTemplate restTemplate;

    private final paypalRepo ppRepo;

    private final payPalTransactionRepo transRepo;

    /*Constructor "PayPalService(RestTemplate restTemplate)" 
    Initializes the "payPal Service class and injects a RestTemplate Object"

     *"RestTemplate" acts a client-side HTTP utility to send HTTP requests and HTTP responses.
    & it automatically converts Java objects to JSON/XML when sending data 
    and deserializes the response JSON/XML back into java objects.
     */
    public payPalService(RestTemplate template, paypalRepo repo, payPalTransactionRepo transRepo){
        this.restTemplate = template;
        this.ppRepo = repo;
        this.transRepo = transRepo;
    }



/*-------------------------------------------------------------- */
    
    /*Method "myToken()" 
    purpose is to fetch an OAuth 2.0 access token from Paypal using client credntials
     * 
     */
    public String myToken(){
        // PREPARE THE HEADERS
        /*the idClient and secretClient are concatenated witha colon(:) to form the credentials 
         * These credentials are Base64 encoded to comply with the Basic Auth header format
         * the encoded string is added to the Autorization header as Basic<encoded-credentials>
        */
        String creds = idClient + ":" + secretClient;
        String encodedCreds = Base64.getEncoder().encodeToString(creds.getBytes());
        HttpHeaders head = new HttpHeaders();
        head.add("Authorization", "Basic " + encodedCreds);
        head.add("Content-Type", "application/x-www-form-urlencoded");

        // PREPARE THE REQUEST BODY
        /*the body is set as a "grant_type=client_credentials" key-value pair.
         * this specifies that the client wants to autheniticate using its credentials
         */
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        /*"HttpEntity" combines the headers and the body into a single object that can be sent int an Http request*/
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, head);


        // MAKE THE REQUEST
        /*the "template.exchange()"" method sends a POST request to the PayPal API URL(apiURL) with the given headers and body.
         * the response is mapped into the "Map" object for easy parsing
         */
        ResponseEntity<Map> response = restTemplate.exchange(
                                                        ppApiUrl, 
                                                        HttpMethod.POST, 
                                                        request, 
                                                        Map.class
        );

        // EXTRACT THE TOKEN
        /*The response body (Map) is inspected for the key "access_token" 
         * if the token is found, it is returned as a string
         * if the token is not found, an exception is thrown to indicate the operation field 
        */
        Map<String, Object> bodyResponse = response.getBody();
        if(bodyResponse != null && bodyResponse.containsKey("access_token")){
            return bodyResponse.get("access_token").toString();
        } else{
            throw new RuntimeException("Cant retrieve acces token");
        }
    }

/*------------------------------------------------------------ */

    /* The throws JsonProcessingException declaration in a method signature tells the Java compiler that the method might throw a JsonProcessingException during execution, 
    and the calling method must either handle it (using a try-catch block) or declare it in its own throws clause.*/
    public String orderCreation(String amount, String currency, String description, String payerEmail) throws JsonProcessingException{

        /*retrieves the OAuth 2 access token needed to authenticate the API call*/
        String token = myToken();

        /*The "Authorization" header includes the Bearer token (token). This authenticates the request*/
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + token);
        header.setContentType(MediaType.APPLICATION_JSON);

        /* The '"intent": "CAPTURE"' specifies that this order is intended for immediate payment capture.
        * (rather than authorization, which requires a separate capture step)
        *
        * "%s" The %s in Java (or many other programming languages) is a format specifier used in string formatting:
        *  It acts as a placeholder for a string and is replaced by a value when the formatted string is created.
        * In Java, %s is used with methods like String.format() to insert strings into a formatted template.
        * 
        * This code constructs a JSON-formatted string using Java's String.format()
        * to explain "currency,amount,description" at the end of variable: 
        * 1."currency_code" will be replaced by value of currency
        * 2."value":"%s" will be replaced by the value of amount
        * 3. "description":"%s" will be replaced by the value of description
        
        1.application_context:
            -This section is required to define specific PayPal behaviors, such as how the user interacts with the approval page.
        2.return_url:
            -URL where PayPal redirects the user after they approve the payment.
            -This should be an endpoint in your Spring Boot application (e.g., /complete-order) to handle order capture.
        2.cancel_url:
            -URL where PayPal redirects the user if they cancel the payment.*/
        String bodyRequest = String.format("""
                {
                    "intent":"CAPTURE",
                    "purchase_units":[
                        {
                            "amount": {
                                "currency_code":"%s",
                                "value": "%s"
                            },
                            "description": "%s",
                            "payeremail": "%s"
                        }
                    ],
                    "application_context": {
                        "return_url": "http://127.0.0.1:5500/websitefrontend/checkoutpage.html",
                        "cancel_url": "http://127.0.0.1:5500/websitefrontend/homepage.html",
                        "brand_name": "YourBrandName",
                        "landing_page": "LOGIN",
                        "user_action": "CONTINUE"
                    }
                }
                """,currency,amount,description,payerEmail);

        /*Combines the request body("bodyRequest") and headers into and HttpEntity. 
        * This will be sent to the Paypal API.
        * "HttpEntity" passes the request body and headers when making HTTP calls
        */
        HttpEntity<String> request = new HttpEntity(bodyRequest, header);

        /* ".postForEntity" sends an HTTP POST request to the specified URL ()*/
        ResponseEntity<String> response = restTemplate.postForEntity("https://api.paypal.com/v2/checkout/orders", request, String.class);

        if(response.getStatusCode() == HttpStatus.CREATED){
            String responseBody = response.getBody();

            // Parse JSON Response
            JsonNode jsonResponse = new ObjectMapper().readTree(responseBody);
            String orderId = jsonResponse.get("id").asText();
            String status = jsonResponse.get("status").asText();
            
            // Save to Database
            payPalModel order = new payPalModel();
            order.setOrderId(orderId);
            order.setStatus(status);
            order.setAmount(amount);
            order.setCurrency(currency);
            order.setDescription(description);
            order.setPayerEmail(payerEmail);
            order.setTimeOfCreatedOrder(LocalDateTime.now());


            ppRepo.save(order);

            System.out.println(responseBody);
            return responseBody;

        } else{
            throw new RuntimeException("Failed creating PayPal Order");
        }


        /*How It Works in Context:
        *  1. THIS METHOD SETS UP A REST API CALL TO PayPal's sandbox environment for testing purposes
            2. It creates a Paypal order with the specified amount and intent
            3. After receiving a response from Paypal, the method returns the order details to the frontend. 
        */

    }
    
/*-------------------------------------------------------- */
/*saves the transaction to my MySql Database & this methos is called in my "orderCapture" method */
    public void saveTrans(payPalTransactionModel transactionModel){
        transRepo.save(transactionModel);
    }

/*---------------------------------------------------------- */

/*This method, "orderCapture", is designed to perform the following operations: 
 * - Make a POST request tote PayPal Capture API
 * - Parse and validate the response
 * - extract critical information
 * - save the information into the database
 */
    public String orderCapture(String orderId) throws JsonProcessingException{
        //Retrieving the Access Token
        /*This fetches the authorization token(Bearer Token)  required for the PayPal authentication.
         * -This ensures the API request is authenticated
        */
        String token = myToken();

        // Set Up HTTP Headers
        /*This sets the content type to "application/json", ensuring the API knows the type of data being sent.
         * Also, adds the Bearer token to the Autorization header for API authentication
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        //Build Http Request
        /*This wraps the headers(and an optional body, which is null here) in an HttpEntity objectto prepare for the API call */
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        //Paypal Capture API URL
        /*The Paypal Capture API URL dynamically includes the "orderId" parameter to specify the order being captured.
         * The "/capture" endpoint is used to finalize the payment.
         */
        String captureUrl= "https://api.paypal.com/v2/checkout/orders/" + orderId + "/capture";

        try{
            /*'restTemplate' which is the name of the injected "RestTemplate" class makes an HTTP request, specifically POST, and handles the response. 
             *"restTemplate.exchange" is used to perform HTTP requests. It allows you to specify the HTTP method, URL, request entity(headers + body), and the type of response you expect.
            "captureURL" is the URL in which the POST request will be sent
            "HttpMethod.POST" indicates that the HTTP method for this request is POST
            "requestEnity" is the instance of HttpEntity that encapsulates the request data, including: Headers & Body
            "String.class specifies the type of response body. In this case the response is expected to be a String."
            the ".exhange" method returns a "ResponseEntity" object that contains a HTTP Status Code(200 Ok, 400 BAd Request, etc.), Headers, and a Body.*/
            ResponseEntity<String> response = restTemplate.exchange(captureUrl,
                                                                HttpMethod.POST,
                                                                requestEntity,
                                                                String.class);
            

            /*This checks if the HTTP response status is 201 CREATED, which indicates the order was successfully captured. */                                                
            if(response.getStatusCode() == HttpStatus.CREATED){

                // Parse and handle the JSON response
                /*"JsonNode" is used to represent JSON data in a tree-like structure.
                * "JsonNode" provides a flexible and dynamic way to work with JSON without needing to bind it to a specific Java object.
                * especially useful when you are working with dynamic or partially unknown JSON structures.
                */
                String responseBody = response.getBody();
                JsonNode responseJson= new ObjectMapper().readTree(responseBody);

                 // Extract the status of the order (check if field exists)
                /*This extracts the status field from the JSON response
                  * - Defaults to 'UNKNOWN' if field is absent
                  */
                String status = responseJson.has("status") ? responseJson.get("status").asText() : "UNKNOWN";


                // Extract purchase_units array and check if it has values
                /*The 'purchase_units' array contains details about the order, including the amount and payment info. */
                JsonNode unitsPurchased = responseJson.get("purchase_units");
                double priceAmount=0.0;
                String transactionId=null;

                /*The (unitsPurchased != null) checks if the "unitsPurchased" array exists
                 * (.isArray) checks if unitsPurchased is an Array
                 * and (size()>0) checks if array is not empty
                 */
                if(unitsPurchased != null && unitsPurchased.isArray() && unitsPurchased.size()>0){
                    JsonNode purchaseUnit = unitsPurchased.get(0);

                    //Extracting Payment Information
                    // Extract amount value
                    /*The 'fieldname: payments' node contains details about the payments trasactions*/
                    JsonNode paymentsNode = purchaseUnit.get("payments");
                    /*The 'fieldname: captures' array within 'payments' contains information about the actual payment captures */
                    if (paymentsNode != null && paymentsNode.has("captures")) {
                        JsonNode capturesNode = paymentsNode.get("captures");
                        if (capturesNode.isArray() && capturesNode.size() > 0) {
                            JsonNode capture = capturesNode.get(0);
                            /*This extracts the 'fieldname: amount' ad 'fieldname: value' field, converting it to a double for later use. */
                            if (capture.has("amount") && capture.get("amount").has("value")) {
                                priceAmount = capture.get("amount").get("value").asDouble();
                            }
                        }
                    }
                    
                    //Extracting the Transaction Id
                    // Extract the captures array and check for the first capture
                    //The Transaction Id is a critical field that uniquely identifies the payment capture.
                    JsonNode paymentNode = purchaseUnit.get("payments");
                    if (paymentNode != null && paymentNode.has("captures") && paymentNode.get("captures").isArray() && paymentNode.get("captures").size()>0){
                        JsonNode nodeCapture = paymentNode.get("captures").get(0);
                        if(nodeCapture.has("id")){
                            transactionId=nodeCapture.get("id").asText();
                        }
                    }
                    // If the transaction ID is not found, throw an exception
                    if(transactionId == null){
                        throw new IllegalStateException("Missing transaction ID in PayPal response.");
                    }

                }

                // Extract payer email (check if field exists)
                /*The "fieldname: payer" object in the respose contains details about the customer who made the payment
                 * the "fieldname: email_address" field is extraced for record-keeping
                 * if the field is missing, a dfault value of "unknown" is used to avoid null issues
                 */
                String payerEmail= responseJson.has("payer") && responseJson.get("payer").has("email_address") 
                                    ? responseJson.get("payer").get("email_address").asText() : "unknown";
                
                String shippingAddress= responseJson.has("purchase_units") 
                                        && responseJson.get("purchase_units").isArray() 
                                        && responseJson.get("purchase_units").get(0).has("shipping")
                                        && responseJson.get("purchase_units").get(0).get("shipping").has("address") 
                                        ? responseJson.get("purchase_units").get(0).get("shipping").get("address").toString() 
                                        : "unknown";

                String customerName= responseJson.has("purchase_units")
                && responseJson.get("purchase_units").isArray()
                && responseJson.get("purchase_units").get(0).has("shipping")
                && responseJson.get("purchase_units").get(0).get("shipping").has("name")
                ? responseJson.get("purchase_units").get(0).get("shipping").get("name").toString()
                : "unknown";




                // Save transaction information to the database
                payPalTransactionModel transaction = new payPalTransactionModel();
                transaction.setOrderId(orderId);
                transaction.setStatus(status);
                transaction.setTransactionId(transactionId != null ? transactionId : "unknown");
                transaction.setCustomerName(customerName);
                transaction.setPayerEmail(payerEmail);
                transaction.setPriceAmount(priceAmount);
                transaction.setTimeOfTransaction(LocalDateTime.now());
                transaction.setShippingAddress(shippingAddress);

                //calls the 'saveTrans' method made earlier, in which the repo saves the transaction to database
                saveTrans(transaction);

                
                System.out.println(responseBody);
                return responseBody;
            }else{
                throw new RuntimeException("Failed to capture order. HTTP Status: " + response.getStatusCode());
            } 

        } catch(HttpClientErrorException e){
            throw new RuntimeException("Error parsing PayPal response: " + e.getMessage(), e);
        }
    }

}
