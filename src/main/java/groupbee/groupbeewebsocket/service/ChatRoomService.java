package groupbee.groupbeewebsocket.service;

import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import groupbee.groupbeewebsocket.dto.UserDto;
import groupbee.groupbeewebsocket.entity.ChatRoomListEntity;
import groupbee.groupbeewebsocket.entity.UserEntity;
import groupbee.groupbeewebsocket.repository.ChatRoomRepository;
import groupbee.groupbeewebsocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final List<ChatRoomDto> chatRoomList = new ArrayList<>(); // 메시지 저장소

    // 새로운 채팅방 생성
    public void createChatRoom(ChatRoomDto chatRoomDto) {
        String TOPIC = chatRoomDto.getTopic();

        ChatRoomListEntity chatRoomListEntity = new ChatRoomListEntity();
        chatRoomListEntity.setChatRoomId(chatRoomDto.getChatRoomId());
        chatRoomListEntity.setChatRoomName(chatRoomDto.getChatRoomName());
        chatRoomListEntity.setLastMessage(chatRoomDto.getLastMessage());
        chatRoomListEntity.setTopic(TOPIC);

        if(chatRoomDto.getLastActive() == null) {
            chatRoomDto.setLastActive(LocalDateTime.now());
        }

        if (TOPIC != null && !TOPIC.isEmpty()){
            chatRoomMap.put(chatRoomDto.getChatRoomId(), chatRoomDto);
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
            chatRoomListEntity.setParticipants(participants);

            // ChatListEntity 저장
            chatRoomRepository.save(chatRoomListEntity);
            System.out.println("Created new chat room: " + chatRoomDto);
        } else {
            log.error("Chat room id is empty : ChatRoomService");
        }
    }

    public List<ChatRoomDto> getAllChatRooms() {
        return new ArrayList<>(chatRoomMap.values()); // 모든 채팅방 반환
    }

    // 사용자가 속한 채팅방 필터링하여 반환
    public List<ChatRoomDto> getChatRoomsForUser(String userId) {
        List<ChatRoomDto> userChatRooms = new ArrayList<>();
        for (ChatRoomDto chatRoom : chatRoomMap.values()) {
            boolean isUserInRoom = chatRoom.getParticipants().stream()
                    .anyMatch(participant -> participant.getUserId().equals(userId));
            if (isUserInRoom) {
                userChatRooms.add(chatRoom);
            }
        }
        return userChatRooms;
    }

    public void exitChatRoom(String chatRoomId, String userId) {
        ChatRoomListEntity chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId);
        System.out.println(chatRoom);
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoomService exitChatRoom" + userId));
        chatRoom.getParticipants().remove(user);
        chatRoomRepository.save(chatRoom);
    }
}
