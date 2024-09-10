package groupbee.groupbeewebsocket.entity;

import groupbee.groupbeewebsocket.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("chat")
public class ChatMessageEntity {
    @Id
    private String id;               // MongoDB 문서 고유 ID (자동 생성)

    private String senderId;         // 메시지를 보낸 사용자 ID
    private String senderName;       // 메시지를 보낸 사용자의 닉네임
    private List<UserDto> recipientId; // 1:1 대화일 경우 상대방 ID 목록
    private String content;          // 메시지 내용
    private String announcement;     // 공지사항일 경우 내용
    private String fileUrl;          // 첨부파일 경로 (선택적)
    private LocalDateTime timestamp;          // 메시지 전송 시간
    private String topic;            // Kafka 토픽
    // private List<Map<String, Integer>> isReadCount;  // 읽음 상태 관리용 (이 부분은 주석처리)
}
