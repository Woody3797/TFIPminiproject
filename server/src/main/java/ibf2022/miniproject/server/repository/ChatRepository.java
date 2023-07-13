package ibf2022.miniproject.server.repository;

import static ibf2022.miniproject.server.repository.SQLQueries.*;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ibf2022.miniproject.server.model.ChatConvo;
import ibf2022.miniproject.server.model.ChatMessage;

@Repository
public class ChatRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("null")
    public Integer saveChatConvo(String sender, String recipient, Integer productID) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement statement = conn.prepareStatement(SAVE_CHAT_CONVO, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, sender);
            statement.setString(2, recipient);
            statement.setInt(3, productID);
            return statement;
        }, keyHolder);
        Integer chatID = keyHolder.getKey().intValue();
        return chatID;
    }

    public ChatConvo getChatConvo(String sender, String recipient, Integer productID) {
        ChatConvo convo = jdbcTemplate.queryForObject(GET_CHAT_CONVO_BY_SENDER_RECIPIENT, new BeanPropertyRowMapper<>(ChatConvo.class), sender, recipient, productID);
        System.out.println(convo);

        return convo;
    }

    public void saveChatMessage(ChatMessage message) {
        
    }
    
}
