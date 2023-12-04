import java.io.FileWriter;
import java.io.IOException;

public class Receipt {
    private int paymentID;
    private String customerName;
    private String paymentDateTime;
    private double paymentAmount;
    private String truncatedCardNumber;
    private String deliveryAddress;

    private String ticketDetails;
    // Assume you have getters and setters for each of the fields

    public void setRecieptPaymentID(int paymentID){
        this.paymentID = paymentID;
    }

    public void generateAndSaveReceipt(Payment payment) {
        // Fill in the receipt information
        this.paymentID = payment.getPaymentID();
        this.customerName = payment.getCustomerInfo();  // Assuming Payment has a method getCustomerInfo()
        this.paymentDateTime = payment.getPaymentDate();
        this.paymentAmount = payment.getPaymentAmount();
        this.deliveryAddress = payment.getDeliveryAddress();
        this.ticketDetails = payment.getTicketDetails();

        // For card details, if the Payment object contains credit card information:
        if (payment.getCreditCard() != null) {
            String cardNumber = payment.getCreditCard().getCardNumber();
            this.truncatedCardNumber = cardNumber.substring(cardNumber.length() - 4);
        }
        // generate the receipt content
//        String receiptContent = generateReceiptText();
//
//        // save the receipt to file
//        String filePath = "E:\\TAMUcoursework\\CSCE606\\Receipts\\Receipt_" + this.paymentID + ".txt";
//        saveReceiptToFile(receiptContent, filePath);
    }





}
