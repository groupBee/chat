package groupbee.groupbeewebsocket.controller;

import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import groupbee.groupbeewebsocket.service.ChatService;
import groupbee.groupbeewebsocket.service.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import groupbee.groupbeewebsocket.service.ChatRoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final KafkaConsumerService kafkaConsumerService;

    // 채팅방 생성 엔드포인트
    @PostMapping("/chatting/create")
    public ChatRoomDto createChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        String UUID = java.util.UUID.randomUUID().toString();
        chatRoomDto.setChatRoomId(UUID);
        System.out.println("컨트롤러에서 찍은거임 : " + chatRoomDto.toString());
        chatRoomService.createChatRoom(chatRoomDto);
        return chatRoomDto;
    }

    @GetMapping("/chatting/history")
    public List<ChatMessageDto> getChatHistory(@RequestParam String chatRoomId) {
        return kafkaConsumerService.getChatHistory(chatRoomId);
    }

    @GetMapping("/chatting/list/{userId}")
    public ResponseEntity<List<ChatRoomDto>> getUserChatRooms(@PathVariable String userId) {
        List<ChatRoomDto> userChatRooms = chatRoomService.getChatRoomsForUser(userId);
        return ResponseEntity.ok(userChatRooms);
    }
}
