package groupbee.groupbeewebsocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
    private String chatRoomId; // 채팅방 고유 ID
    private String chatRoomName; // 채팅방 이름 (그룹 채팅일 경우)
    private List<UserDto> participants; // 채팅방에 속한 사용자 목록
    private String lastMessage; // 마지막 메시지 내용 (미리보기용)
    private Date lastActive; // 마지막 활성화 시간
    private boolean isGroupChat; // 그룹 채팅 여부
}