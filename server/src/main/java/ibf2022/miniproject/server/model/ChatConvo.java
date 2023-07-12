package ibf2022.miniproject.server.model;

public class ChatConvo {

    private Long chatID;
    private String buyer;
    private String seller;
    
    public Long getChatID() {
        return chatID;
    }
    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }
    public String getBuyer() {
        return buyer;
    }
    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
    public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }

    public ChatConvo() {
    }

    public ChatConvo(Long chatID, String buyer, String seller) {
        this.chatID = chatID;
        this.buyer = buyer;
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "ChatConvo [chatID=" + chatID + ", buyer=" + buyer + ", seller=" + seller + "]";
    }
    
}
