package controller;

import dto.ChatMessageDto;
import dto.ChatRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import service.ChatService;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 클라이언트로부터 WebSocket 메시지를 받으면 Kafka 로 전송
    @MessageMapping("/chat")
    public void handleChatMessage(ChatMessageDto message, ChatRoomDto chatRoom) throws Exception {

        // 메시지를 Kafka 로 전송 (모든 메시지는 Kafka 로 저장)
        chatService.sendMessageToKafka(message);

        // 메시지를 WebSocket 으로 처리
        chatService.processMessage(message, chatRoom);
    }
}
