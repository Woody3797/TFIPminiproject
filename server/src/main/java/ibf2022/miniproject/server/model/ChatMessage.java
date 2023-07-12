package ibf2022.miniproject.server.model;

import java.time.LocalDateTime;

public class ChatMessage {

    private Long messageID;
    private Long chatID;
    private String sender;
    private String content;
    private LocalDateTime timestamp;
    
    public Long getMessageID() {
        return messageID;
    }
    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }
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
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public ChatMessage() {
    }
    
    public ChatMessage(Long messageID, Long chatID, String sender, String content, LocalDateTime timestamp) {
        this.messageID = messageID;
        this.chatID = chatID;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessage [messageID=" + messageID + ", chatID=" + chatID + ", sender=" + sender + ", content="
                + content + ", timestamp=" + timestamp + "]";
    }
    
}
