package groupbee.groupbeewebsocket.service;

import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    // Kafka 토픽에서 메시지를 구독하고 처리
    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void consume(ChatMessageDto message) {
        System.out.println("Consumed message from Kafka: " + message);

        ChatRoomDto chatRoom = chatRoomService.getChatRoomById(message.getChatRoomId()); // 채팅방 정보 가져오기
        chatRoomService.updateLastMessage(message.getChatRoomId(), message.getContent()); // 마지막 메세지 업데이트
        chatService.processMessage(message, chatRoom); // 메세지 처리
    }
}
