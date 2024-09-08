package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name; // 사용자가 입력한 닉네임
    private String userId; // 고유 사용자 ID (예: UUID로 생성 가능)
}