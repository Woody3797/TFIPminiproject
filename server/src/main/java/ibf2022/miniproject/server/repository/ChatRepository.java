package ibf2022.miniproject.server.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import ibf2022.miniproject.server.model.ChatConvo;
import ibf2022.miniproject.server.model.ChatMessage;

@Repository
public class ChatRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ChatMessage saveChatMessage(ChatMessage message) {
        // Query query = Query.query(Criteria.where("chatID").is(message.getChatID()));
        ChatMessage result = mongoTemplate.insert(message, "chat_messages");

        return result;
    }

    public List<ChatMessage> getChatMessages(String chatID) {
        Query query = Query.query(Criteria.where("chatID").is(chatID));
        List<ChatMessage> messages = mongoTemplate.find(query, ChatMessage.class, "chat_messages");
        return messages;
    }

	public List<ChatMessage> getAllConvos(String email) {
        Query query = Query.query(Criteria.where("chatID").regex(email));
        List<ChatMessage> messages = mongoTemplate.find(query, ChatMessage.class, "chat_messages");
        return messages;
	}

    
}
