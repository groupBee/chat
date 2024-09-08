package groupbee.groupbeewebsocket.service;

import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import groupbee.groupbeewebsocket.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final String TOPIC = "chat-topic"; //Kafka 토픽
    private KafkaTemplate<String, ChatMessageDto> kafkaTemplate;
    private SimpMessagingTemplate messagingTemplate;

    // WebSocket 메시지를 Kafka 로 전송
    public void sendMessageToKafka(ChatMessageDto message) {
        kafkaTemplate.send(TOPIC, message); //Kafka 로 메세지 전송
        System.out.println("Sent message to Kafka: " + message);
    }

    // WebSocket 을 통해 메시지 전송 (개인 메시지, 채팅방 메시지, 공지사항)
    public void processMessage(ChatMessageDto message, ChatRoomDto chatRoom) {
        // 1:1 개인 메시지 처리
        if (message.getRecipientId() != null && !message.getRecipientId().isEmpty()) {
            sendPrivateMessage(message);
        }
        // 그룹 채팅일 경우
        else if (chatRoom.isGroupChat()) {
            sendGroupMessage(message, chatRoom.getParticipants());
        }
        // 공지사항 또는 채팅방 메시지 처리
        else if (message.getChatRoomId() != null && !message.getChatRoomId().isEmpty()) {
            sendRoomMessage(message);
        }

        // 파일이 첨부된 경우 처리
        if (message.getFileUrl() != null && !message.getFileUrl().isEmpty()) {
            System.out.println("File attached: " + message.getFileUrl());
            // 파일 처리 로직 추가 가능 (예: 클라이언트에 파일 URL 전송)
        }
    }

    // 1:1 메시지 전송
    private void sendPrivateMessage(ChatMessageDto message) {
        messagingTemplate.convertAndSendToUser(message.getRecipientId(), "/queue/messages", message);
        System.out.println("Sent private message to " + message.getRecipientId());
    }

    // 그룹 채팅 메시지 전송
    private void sendGroupMessage(ChatMessageDto message, List<UserDto> participants) {
        for (UserDto user : participants) {
            messagingTemplate.convertAndSendToUser(user.getUserId(), "/queue/messages", message);
        }
        System.out.println("Sent group message to all participants");
    }

    // 채팅방 메시지 또는 공지사항 전송
    private void sendRoomMessage(ChatMessageDto message) {
        messagingTemplate.convertAndSend("/topic/chat/" + message.getChatRoomId(), message);
        System.out.println("Sent message to room " + message.getChatRoomId());
    }
}
