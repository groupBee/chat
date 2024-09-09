package groupbee.groupbeewebsocket.controller;

import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import groupbee.groupbeewebsocket.service.ChatService;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 클라이언트로부터 WebSocket 메시지를 받으면 Kafka 로 전송
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public void handleChatMessage(ChatMessageDto message) throws Exception {
        System.out.println(message);

        // 메시지를 WebSocket 으로 처리
        chatService.processMessage(message);
    }
}