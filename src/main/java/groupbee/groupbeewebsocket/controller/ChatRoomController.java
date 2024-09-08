package groupbee.groupbeewebsocket.controller;

import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import groupbee.groupbeewebsocket.service.ChatRoomService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 채팅방 생성 엔드포인트
    @PostMapping("/chatting/create")
    public ChatRoomDto createChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        chatRoomService.createChatRoom(chatRoomDto);
        return chatRoomDto;
    }

    // 모든 채팅방 목록을 가져오는 엔드포인트
    @GetMapping("/list/chatroom")
    public List<ChatRoomDto> getChatRooms() {
        return new ArrayList<>(chatRoomService.getAllChatRooms());
    }
}
