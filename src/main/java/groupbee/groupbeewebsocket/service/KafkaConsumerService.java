package groupbee.groupbeewebsocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.entity.ChatMessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.checkerframework.checker.units.qual.K;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<ChatMessageDto> chatMessages = new ArrayList<>();

    @KafkaListener(topics = "one-to-one-chat", groupId = "chat-group")
    public void consumeOne(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        try {
            // Kafka 에서 가져온 메시지를 ChatMessageDto 로 변환
            ChatMessageDto chatMessageDto = objectMapper.readValue(record.value(), ChatMessageDto.class);
            // 이전에 받은 메시지들을 리스트에 저장
            chatMessages.add(chatMessageDto);
            // WebSocket 으로 실시간 메시지 전송
            messagingTemplate.convertAndSend("/topic/messages/" + chatMessageDto.getChatRoomId(), chatMessageDto);
        } catch (Exception e) {
            log.error("KafkaConsumerService 단일 메시지 처리 중 오류 발생", e);
        }
    }

    @KafkaListener(topics = "one-to-many-chat", groupId = "chat-group")
    public void consumeMany(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        try {
            // Kafka 에서 가져온 메시지를 ChatMessageDto 로 변환
            ChatMessageDto chatMessageDto = objectMapper.readValue(record.value(), ChatMessageDto.class);

            // 메시지를 WebSocket 을 통해 해당 채팅방의 모든 구독자에게 브로드캐스트
            messagingTemplate.convertAndSend("/topic/group/" + chatMessageDto.getChatRoomId(), chatMessageDto);

        } catch (Exception e) {
            log.error("KafkaConsumerService 다중 메시지 처리 중 오류 발생", e);
        }
    }

    public List<ChatMessageDto> getChatHistory(String chatRoomId) {
        List<ChatMessageDto> filteredMessages = new ArrayList<>();
        for (ChatMessageDto message : chatMessages) {
            if (message.getChatRoomId().equals(chatRoomId)) {
                filteredMessages.add(message);
            }
        }
        return filteredMessages;
    }
}
