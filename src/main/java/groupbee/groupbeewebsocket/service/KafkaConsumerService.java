package groupbee.groupbeewebsocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "one", groupId = "chat-group")
    public void consume(String message) {
        try {
            ChatMessageDto chatMessageDto = objectMapper.readValue(message, ChatMessageDto.class);
            //System.out.println("여기까지 온건가 닝겐또 : " + chatMessageDto);
            System.out.println("여기까지 온거다 닝겐또...: " + chatMessageDto.getChatRoomId());
            System.out.println("/topic/messages/"+chatMessageDto.getChatRoomId());
            messagingTemplate.convertAndSend("/topic/messages/" + chatMessageDto.getChatRoomId(), chatMessageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
