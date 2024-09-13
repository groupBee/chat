package groupbee.groupbeewebsocket.service;

import groupbee.groupbeewebsocket.dto.ChatMessageDto;
import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import groupbee.groupbeewebsocket.dto.UserDto;
import groupbee.groupbeewebsocket.entity.ChatRoomListEntity;
import groupbee.groupbeewebsocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final KafkaTemplate<String, ChatMessageDto> kafkaTemplate;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void processMessage(ChatMessageDto message) {
        String kafkaTopic = message.getTopic();

        // 1:1 개인 메시지 처리
        if (kafkaTopic != null && !kafkaTopic.isEmpty()) {
            if (kafkaTopic.equals("one")){
                // Kafka 로 메시지를 전송 (KafkaTemplate 사용)
                kafkaTemplate.send("one-to-one-chat", message.getChatRoomId(), message);
                log.info("ChatService one to one : {}", kafkaTopic);
            } else if(kafkaTopic.equals("many")) {
                // Kafka 로 메시지를 전송 (KafkaTemplate 사용)
                kafkaTemplate.send("one-to-many-chat", message.getChatRoomId(), message);
                log.info("ChatService one to many: {}", kafkaTopic);
            }
        } else {
            log.error("ChatService processMessage 실패");
        }

        // 파일이 첨부된 경우 처리
        if (message.getFileUrl() != null && !message.getFileUrl().isEmpty()) {
            System.out.println("File attached: " + message.getFileUrl());
        }
        updateChatRoomInfo(message);
    }

    private void updateChatRoomInfo(ChatMessageDto message) {
        ChatRoomListEntity chatRoom = chatRoomRepository.findByChatRoomId(message.getChatRoomId());

        // 채팅방이 null일 경우 예외 발생
        if (chatRoom == null) {
            throw new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + message.getChatRoomId());
        }

        // 마지막 메시지와 마지막 활성화 시간 업데이트
        chatRoom.setLastMessage(message.getContent());
        chatRoom.setLastActive(LocalDateTime.now());

        // UserEntity -> UserDto 변환 (Stream API 사용)
        List<UserDto> participantDtos = chatRoom.getParticipants()
                .stream()
                .map(participant -> new UserDto(participant.getUserId(), participant.getName(), participant.getProfile())) // UserEntity -> UserDto 변환
                .toList();

        // 읽지 않은 메시지 수 관리
        for (UserDto participantDto : participantDtos) {
            // 메시지 보낸 사용자는 제외
            if (!participantDto.getUserId().equals(message.getSenderId())) {
                // incrementUnreadCount 메서드 사용하여 읽지 않은 메시지 수 증가
                chatRoom.incrementUnreadCount(participantDto.getUserId());
            }
        }

        // 변경된 채팅방 정보를 저장
        chatRoomRepository.save(chatRoom);

        log.info("채팅방 정보 업데이트: 채팅방 ID = {}, 마지막 메시지 = {}, 마지막 활성화 시간 = {}",
                chatRoom.getChatRoomId(), chatRoom.getLastMessage(), chatRoom.getLastActive());
    }
}
