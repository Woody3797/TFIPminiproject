package ibf2022.miniproject.server.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<ChatMessage> getChatMessages(String chatID) {
        return chatRepository.getChatMessages(chatID);
    }

	public List<ChatMessage> getAllConvos(String email) {
		return chatRepository.getAllConvos(email);
	}
    
}
