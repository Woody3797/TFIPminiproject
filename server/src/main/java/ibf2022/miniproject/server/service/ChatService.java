package ibf2022.miniproject.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.miniproject.server.model.ChatConvo;
import ibf2022.miniproject.server.model.ChatMessage;
import ibf2022.miniproject.server.repository.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;


    public ChatMessage saveChatMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return chatRepository.saveChatMessage(message);
    }

    public List<ChatMessage> getChatMessagesByID(String chatID) {
        return chatRepository.getChatMessagesByID(chatID);
    }

	public List<ChatMessage> getAllConvos(String email) {
		return chatRepository.getAllConvos(email);
	}

    public List<ChatConvo> getAllConvos2(String email) {
		List<ChatMessage> messages = chatRepository.getAllConvosGroupedByChatID(email);
        List<ChatConvo> conversations = new ArrayList<>();
        if (messages.size() != 0) {
            Map<String, List<ChatMessage>> groupedMessages = messages.stream().collect(Collectors.groupingBy(ChatMessage::getChatID));
            
            groupedMessages.keySet().forEach(key -> {
                ChatConvo convo = new ChatConvo();
                convo.setChatID(key);
                convo.setProductID(groupedMessages.get(key).get(0).getProductID());
                convo.setMessages(groupedMessages.get(key));
                conversations.add(convo);
            });
        }
        return conversations;
	}
    
}
