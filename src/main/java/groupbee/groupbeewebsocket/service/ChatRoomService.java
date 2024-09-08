package groupbee.groupbeewebsocket.service;

import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final KafkaTemplate<String, ChatRoomDto> kafkaTemplate;
    private final Map<String, ChatRoomDto> chatRoomMap = new ConcurrentHashMap<>();
    private static final String TOPIC = "chatroom-topic";

    // 특정 채팅방 ID로 채팅방 정보 가져오기
    public ChatRoomDto getChatRoomById(String chatRoomId) {
        return chatRoomMap.get(chatRoomId); // 메모리에서 가져옴 (추후 DB로 대체 가능)
    }

    // 새로운 채팅방 생성
    public void createChatRoom(ChatRoomDto chatRoomDto) {
        chatRoomMap.put(chatRoomDto.getChatRoomId(), chatRoomDto);
        kafkaTemplate.send(TOPIC, chatRoomDto);
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
