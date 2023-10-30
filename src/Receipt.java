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

    public void generateAndSaveReceipt(Payment payment) {
        // Fill in the receipt information
        this.paymentID = Application.getInstance().getDataAdapter().getMaxPaymentID();
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



    public String generateReceiptText() {
        StringBuilder receiptText = new StringBuilder();
        receiptText.append("Payment ID: ").append(this.paymentID).append("\n");
        receiptText.append("Customer: ").append(this.customerName).append("\n");
        String[] dateTimeParts = paymentDateTime.split("\\.");
        String paymentDate = dateTimeParts[0];
        receiptText.append("Payment Date/Time: ").append(paymentDate).append("\n");
        receiptText.append("Ticket info: ").append("\n");
        receiptText.append("Event EventDate TicketPrice Quantity").append("\n");
        receiptText.append(this.ticketDetails).append("\n");


        receiptText.append("Payment Amount: $").append(this.paymentAmount).append("\n");
        receiptText.append("Delivery Address: ").append(this.deliveryAddress).append("\n");
        receiptText.append("Card Number: **** **** **** ").append(this.truncatedCardNumber).append("\n");

        return receiptText.toString();
    }

    // save the receipt to file
    private void saveReceiptToFile(String content, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
            System.out.println("Receipt saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving receipt to file: " + e.getMessage());
        }
    }
}
