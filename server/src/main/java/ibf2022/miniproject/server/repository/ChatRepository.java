package ibf2022.miniproject.server.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.miniproject.server.model.ChatMessage;

@Repository
public class ChatRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @SuppressWarnings("null")
    public ChatMessage saveChatMessage(ChatMessage message) {
        // Query query = Query.query(Criteria.where("chatID").is(message.getChatID()));
        ChatMessage result = mongoTemplate.insert(message, "chat_messages");
        return result;
    }

    public List<ChatMessage> getChatMessagesByID(String chatID) {
        Query query = Query.query(Criteria.where("chatID").is(chatID));
        List<ChatMessage> messages = mongoTemplate.find(query, ChatMessage.class, "chat_messages");
        return messages;
    }

	public List<ChatMessage> getAllConvos(String email) {
        String chatter1 = "^" + email + ",";
        String chatter2 = "," + email + ",\\d+$";
        Query query = Query.query(new Criteria().orOperator(Criteria.where("chatID").regex(chatter1, "i"), Criteria.where("chatID").regex(chatter2, "i")));
        List<ChatMessage> messages = mongoTemplate.find(query, ChatMessage.class, "chat_messages");
        return messages;
	}

    @SuppressWarnings("null")
    public List<ChatMessage> getAllConvosGroupedByChatID(String email) {
        Query query = Query.query(new Criteria().orOperator(Criteria.where("sender").regex(email, "i"), Criteria.where("recipient").regex(email, "i")));
        List<ChatMessage> messages = mongoTemplate.find(query, ChatMessage.class, "chat_messages");
        return messages;
	}

}
