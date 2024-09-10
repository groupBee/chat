package groupbee.groupbeewebsocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.entity.ChatMessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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

    @KafkaListener(topics = "one", groupId = "chat-group")
    public void consume(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        try {
            // Kafka 에서 가져온 메시지를 ChatMessageDto 로 변환
            ChatMessageDto chatMessageDto = objectMapper.readValue(record.value(), ChatMessageDto.class);

            // 이전에 받은 메시지들을 리스트에 저장
            chatMessages.add(chatMessageDto);

            // WebSocket 으로 실시간 메시지 전송
            messagingTemplate.convertAndSend("/topic/messages/" + chatMessageDto.getChatRoomId(), chatMessageDto);
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생", e);
        }
    }
}
