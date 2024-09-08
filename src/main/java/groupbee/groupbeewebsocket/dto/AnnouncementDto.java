package groupbee.groupbeewebsocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDto {
    private String chatRoomId; // 공지가 속한 채팅방 ID
    private String announcementContent; // 공지 내용
    private String adminId; // 공지를 등록한 관리자 ID
    private Date postedDate; // 공지 등록 시간
}