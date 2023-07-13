package ibf2022.miniproject.server.model;

public class ChatConvo {

    private Long chatID;
    private String sender;
    private String recipient;
    private Integer productID;
    
    public Long getChatID() {
        return chatID;
    }
    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public ChatConvo() {
    }

    public ChatConvo(Long chatID, String sender, String recipient, Integer productID) {
        this.chatID = chatID;
        this.sender = sender;
        this.recipient = recipient;
        this.productID = productID;
    }
    
    @Override
    public String toString() {
        return "ChatConvo [chatID=" + chatID + ", sender=" + sender + ", recipient=" + recipient + ", productID="
                + productID + "]";
    }
    
}
