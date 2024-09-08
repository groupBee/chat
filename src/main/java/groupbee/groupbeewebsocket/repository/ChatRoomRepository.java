package groupbee.groupbeewebsocket.repository;

import groupbee.groupbeewebsocket.dto.ChatRoomDto;
import groupbee.groupbeewebsocket.entity.ChatListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ChatRoomRepository extends JpaRepository<ChatListEntity, String> {
}
