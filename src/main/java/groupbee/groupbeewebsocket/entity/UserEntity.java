package groupbee.groupbeewebsocket.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(nullable = false, unique = true)
    private String userId; // api/employee/info -> UUID

    @Column(nullable = false)
    private String name; // api/employee/info -> name

    @Column
    private String profile;
}
