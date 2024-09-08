package groupbee.groupbeewebsocket.service;

import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import groupbee.groupbeewebsocket.dto.UserDto;
import groupbee.groupbeewebsocket.entity.ChatListEntity;
import groupbee.groupbeewebsocket.entity.UserEntity;
import groupbee.groupbeewebsocket.repository.ChatRoomRepository;
import groupbee.groupbeewebsocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final KafkaTemplate<String, ChatRoomDto> kafkaTemplate;
    private final Map<String, ChatRoomDto> chatRoomMap = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 특정 채팅방 ID로 채팅방 정보 가져오기
    public ChatRoomDto getChatRoomById(String chatRoomId) {
        return chatRoomMap.get(chatRoomId); // 메모리에서 가져옴 (추후 DB로 대체 가능)
    }

    // 새로운 채팅방 생성
    public void createChatRoom(ChatRoomDto chatRoomDto) {
        String TOPIC = chatRoomDto.getTopic();
        String chatRoomId = UUID.randomUUID().toString();

        chatRoomDto.setChatRoomId(chatRoomId);
        ChatListEntity chatListEntity = new ChatListEntity();
        chatListEntity.setChatRoomId(chatRoomId); // 채팅방 ID 생성
        chatListEntity.setChatRoomName(chatRoomDto.getChatRoomName());
        chatListEntity.setLastMessage(chatRoomDto.getLastMessage());
        chatListEntity.setLastActive(chatRoomDto.getLastActive());
        chatListEntity.setTopic(TOPIC);

        if (TOPIC != null && !TOPIC.isEmpty()){
            chatRoomMap.put(chatRoomId, chatRoomDto);
            kafkaTemplate.send(TOPIC, chatRoomDto);

            // participants 리스트를 UserEntity로 변환하여 ChatListEntity에 추가
            List<UserEntity> participants = new ArrayList<>();
            for (UserDto participantDto : chatRoomDto.getParticipants()) {
                // 만약 해당 사용자가 이미 DB에 있다면, 그 사용자를 가져오고, 없다면 새로 추가
                UserEntity userEntity = userRepository.findByUserId(participantDto.getUserId())
                        .orElseGet(() -> {
                            UserEntity newUser = new UserEntity();
                            newUser.setUserId(participantDto.getUserId());
                            newUser.setName(participantDto.getName());
                            return userRepository.save(newUser);  // 새 사용자 저장
                        });
                participants.add(userEntity);
            }
            chatListEntity.setParticipants(participants);

            // ChatListEntity 저장
            chatRoomRepository.save(chatListEntity);
            System.out.println("Created new chat room: " + chatRoomDto);
        } else {
            log.error("Chat room id is empty : ChatRoomService");
        }
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
