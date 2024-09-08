package service;

import dto.ChatRoomDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatRoomService {
    // 메모리 상의 채팅방 목록을 관리하기 위한 예시
    private final Map<String, ChatRoomDto> chatRoomMap = new HashMap<>();

    // 특정 채팅방 ID로 채팅방 정보 가져오기
    public ChatRoomDto getChatRoomById(String chatRoomId) {
        return chatRoomMap.get(chatRoomId); // 메모리에서 가져옴 (추후 DB로 대체 가능)
    }

    // 새로운 채팅방 생성
    public void createChatRoom(ChatRoomDto chatRoomDto) {
        chatRoomMap.put(chatRoomDto.getChatRoomId(), chatRoomDto);
        System.out.println("Created new chat room: " + chatRoomDto);
    }

    // 채팅방에 메시지 기록 업데이트
    public void updateLastMessage(String chatRoomId, String lastMessage) {
        ChatRoomDto chatRoom = chatRoomMap.get(chatRoomId);
        if (chatRoom != null) {
            chatRoom.setLastMessage(lastMessage);
            chatRoom.setLastActive(new Date()); // 마지막 활성화 시간 업데이트
            System.out.println("Updated last message in chat room " + chatRoomId);
        }
    }

    public List<ChatRoomDto> getAllChatRooms() {
        return new ArrayList<>(chatRoomMap.values()); // 모든 채팅방 반환
    }
}
