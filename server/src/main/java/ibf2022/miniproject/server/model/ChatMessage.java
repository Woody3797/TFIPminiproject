package ibf2022.miniproject.server.model;

import java.time.LocalDateTime;

public class ChatMessage {

    private String chatID;
    private String sender;
    private String recipient;
    private String content;
    private Integer productID;
    private LocalDateTime timestamp;

    public String getChatID() {
        return chatID;
    }
    public void setChatID(String chatID) {
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
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessage [chatID=" + chatID + ", sender=" + sender + ", recipient=" + recipient + ", content="
                + content + ", productID=" + productID + ", timestamp=" + timestamp + "]";
    }
    
}
