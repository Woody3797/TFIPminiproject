package ibf2022.miniproject.server.model;

import java.util.ArrayList;
import java.util.List;

public class ChatConvo {

    private String chatID;
    private Integer productID;
    private List<ChatMessage> messages = new ArrayList<>();
    
    public String getChatID() {
        return chatID;
    }
    public void setChatID(String chatID) {
        this.chatID = chatID;
    }
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
    public List<ChatMessage> getMessages() {
        return messages;
    }
    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public ChatConvo() {
    }

    public ChatConvo(String chatID, Integer productID, List<ChatMessage> messages) {
        this.chatID = chatID;
        this.productID = productID;
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ChatConvo [chatID=" + chatID + ", productID=" + productID + ", messages=" + messages + "]";
    }
}
