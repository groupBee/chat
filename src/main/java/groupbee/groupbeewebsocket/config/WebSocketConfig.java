package groupbee.groupbeewebsocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /*
        특정 사용저애개 메시지를 보낼 때 일반적으로 /user/{userName}/queue 와 같은 경로를 사용
        이 경로를 자동으로 라우팅 해주는 접두사 => setUserDestinationPrefix 로 설정

        클라이언트는 서버에 특정 사용자에게 보내는 메시지를 전송할 때 /app/private 와 같은 경로로 메시지를 보낼때
        서버는 이 메시지를 특정 사용자에게 전달하기 위해 경로를 /user/{userName}/queue/private 로 변환하여 해당 사용자에게만 메시지를 전달

        /topic : 브로드캐스트 방식의 메시지 전달을 위한 경로
        다수의 클라이언트가 구독할 수 있는 공용채팅, 공지사항 같은 메시지를 전달할 때 사용
        여러 클라이언트가 동일한 /topic 경로를 구독하고 있을 때, 서버에서 해당 경로로 메시지를 발행하면 구독중인 모든 클라이언트에게 메시지 전달

        /queue : 개별 사용자 또는 개인 채팅을 위한 경로
        서버에서 특정 사용자에게 직접 메세지를 보내야 할 때 사용
        서버에서 사용자에게 알림을 보내거나, 특정 작업에 대한 결과를 알려줄 때 (알림)
        -> 클라이언트가 요청하지 않았지만 서버가 중요한 정보를 실시간으로 전달해야 하는 상황일 때 사용

        /user : 클라이언트끼리 1:1 메시지를 보낼 때 사용
        사용자가 다른 사용자에게 1:1 메시지를 보낼때 사용
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    /*
    클라이언트가 WebSocket 에 연결하기 위한 엔드포인트 설정
    /ws 엔드포인트로 WebSocket 연결을 시도, SockJS 를 지원, WebSocket 미지원 환경에서도 동작하게 한다.
 */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*"); // postman 에서 사용하기 위해 SockJS 제거
    }
}
