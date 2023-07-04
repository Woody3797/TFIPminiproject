package ibf2022.miniproject.server.websocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "converstation")
public class Chat {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String senderName;

  private String receiverName;


  public Chat() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
  @JsonIgnore
  private Set<ChatMessage> chatMessages;


  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  public Set<ChatMessage> getChatMessages() {
    return chatMessages;
  }

  public void setChatMessages(Set<ChatMessage> chatMessages) {
    this.chatMessages = chatMessages;
  }
}
