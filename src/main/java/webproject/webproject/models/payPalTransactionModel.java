package webproject.webproject.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(schema="webproject", name="paypaltransactions")
public class payPalTransactionModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="orderid")
    private String orderId;

    @Column(name="transactionid")
    private String transactionId;

    @Column(name="status")
    private String status;

    @Column(name="customername")
    private String customerName;

    @Column(name="payeremail")
    private String payerEmail;

    @Column(name="priceamount")
    private double priceAmount;

    @Column(name="timeoftransaction")
    private LocalDateTime timeOfTransaction;

    @Column(name="shippingaddress")
    private String shippingAddress;

    public payPalTransactionModel(){
        super();
    }

    public payPalTransactionModel(int id, String orderId, String transactionId, String customerName ,String status, String payerEmail, double priceAmount, LocalDateTime timeOfTransaction, String shippingAddress){
        this.id = id;
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.status = status;
        this.payerEmail = payerEmail;
        this.priceAmount = priceAmount;
        this.timeOfTransaction = timeOfTransaction;
        this.shippingAddress = shippingAddress;
        this.customerName = customerName;
    }
}
