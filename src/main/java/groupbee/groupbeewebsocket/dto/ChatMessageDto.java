package groupbee.groupbeewebsocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "groupbee_chat")
public class ChatMessageDto {
    private String chatRoomId;      // 채팅방 고유 ID
    private String senderId;        // 메시지를 보낸 사용자 ID
    private String senderName;  // 메시지를 보낸 사용자의 이름
    private List<UserDto> recipientId;     // 1:1 대화일 경우 상대방 ID
    private String content;         // 메시지 내용
    private String announcement;    // 공지사항일 경우 내용
    private String fileUrl;         // 첨부파일 경로 (선택적)
    private Date timestamp;         // 메시지 전송 시간
    private String topic;           // Kafka 토픽
}