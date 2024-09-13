package groupbee.groupbeewebsocket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // 알 수 없는 필드 무시
public class UserDto {
    private String name; // 사용자가 입력한 닉네임
    private String userId; // 고유 사용자 ID (예: UUID로 생성 가능)
    private String profile;
}