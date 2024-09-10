package groupbee.groupbeewebsocket.service;

import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import groupbee.groupbeewebsocket.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final KafkaTemplate<String, ChatMessageDto> kafkaTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public void processMessage(ChatMessageDto message) {
        String kafkaTopic = message.getTopic();

        // 1:1 개인 메시지 처리
        if (kafkaTopic != null && !kafkaTopic.isEmpty()) {
            if (kafkaTopic.equals("one")){
                // Kafka 로 메시지를 전송 (KafkaTemplate 사용)
                kafkaTemplate.send("one-to-one-chat", message.getChatRoomId(), message);
                log.info("ChatService one to one : {}", kafkaTopic);
            } else if(kafkaTopic.equals("many")) {
                // Kafka 로 메시지를 전송 (KafkaTemplate 사용)
                kafkaTemplate.send("one-to-many-chat", message.getChatRoomId(), message);
                log.info("ChatService one to many: {}", kafkaTopic);
            }
        } else {
            log.error("ChatService processMessage 실패");
        }

        // 파일이 첨부된 경우 처리
        if (message.getFileUrl() != null && !message.getFileUrl().isEmpty()) {
            System.out.println("File attached: " + message.getFileUrl());
        }
    }
}
