package hu.bartl.todo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Value("${todoapp.messaging.broker}")
    private String brokerName;

    @Value("${todoapp.messaging.application-prefix}")
    private String applicationPrefix;

    @Value("${todoapp.messaging.stomp-endpoint}")
    private String stompEndpoint;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/" + brokerName);
        config.setApplicationDestinationPrefixes("/" + applicationPrefix);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/" + stompEndpoint)
                .withSockJS();
    }

}
