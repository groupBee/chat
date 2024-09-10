package groupbee.groupbeewebsocket.controller;

import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import groupbee.groupbeewebsocket.dto.UserDto;
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
        chatRoomService.createChatRoom(chatRoomDto);
        return chatRoomDto;
    }

    @GetMapping("/chatting/history")
    public List<ChatMessageDto> getChatHistory(@RequestParam String chatRoomId) {
        return kafkaConsumerService.getChatHistory(chatRoomId);
    }

    @PostMapping("/chatting/list")
    public ResponseEntity<List<ChatRoomDto>> getUserChatRooms(@RequestBody UserDto userDto) {
        String userId = userDto.getUserId();
        List<ChatRoomDto> userChatRooms = chatRoomService.getChatRoomsForUser(userId);
        return ResponseEntity.ok(userChatRooms);
    }

    @DeleteMapping("/chatting/delete")
    public void exitChatRoom(@RequestParam String chatRoomId,
                             @RequestParam String userId) {
        chatRoomService.exitChatRoom(chatRoomId, userId);
    }

    @PostMapping("update/chatRoomName")
    public void changeChattingRoomName(@RequestBody ChatRoomDto chatRoomDto) {

    }

}
