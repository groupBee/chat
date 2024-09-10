package groupbee.groupbeewebsocket.repository;

import groupbee.groupbeewebsocket.entity.ChatRoomListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ChatRoomRepository extends JpaRepository<ChatRoomListEntity, Long> {
    ChatRoomListEntity findByChatRoomId(String chatRoomId);
}
