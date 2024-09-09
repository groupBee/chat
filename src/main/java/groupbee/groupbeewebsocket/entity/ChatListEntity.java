package groupbee.groupbeewebsocket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_list")
public class ChatListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(nullable = false, unique = true)
    private String chatRoomId; // 채팅방 고유 ID

    @Column(nullable = false)
    private String chatRoomName; // 채팅방 이름 (그룹 채팅일 경우)

    // Many-to-Many 관계 설정
    @ManyToMany
    @JoinTable(
            name = "chat_room_users", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "chat_room_id"), // ChatRoom 에서 조인될 컬럼
            inverseJoinColumns = @JoinColumn(name = "user_id") // User 에서 조인될 컬럼
    )
    private List<UserEntity> participants; // 채팅방에 속한 사용자 목록

    private String lastMessage; // 마지막 메시지 내용 (미리보기용)

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActive; // 마지막 활성화 시간

    @Column(nullable = false)
    private String topic; // 그룹 채팅 여부
}
