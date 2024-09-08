package groupbee.groupbeewebsocket.controller;

import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import groupbee.groupbeewebsocket.service.ChatService;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 클라이언트로부터 WebSocket 메시지를 받으면 Kafka 로 전송
    @MessageMapping("/chat")
    public void handleChatMessage(ChatMessageDto message, ChatRoomDto chatRoom) throws Exception {
        System.out.println(message);
        // 메시지를 Kafka 로 전송 (모든 메시지는 Kafka 로 저장)
        chatService.sendMessageToKafka(message);

        // 메시지를 WebSocket 으로 처리
        chatService.processMessage(message, chatRoom);
    }
}
