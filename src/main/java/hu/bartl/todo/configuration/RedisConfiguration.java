package hu.bartl.todo.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hu.bartl.todo.messaging.TaskMessageListener;
import hu.bartl.todo.messaging.TaskMessagePublisher;
import hu.bartl.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfiguration {

    @Value("${todoapp.messaging.taskchannel}")
    private String taskChannelName;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(taskChannelName));

        return container;
    }

    public ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(TaskMessageListener taskMessageListener) {
        return new MessageListenerAdapter(taskMessageListener, "receiveTaskMessage");
    }

    @Bean
    public TaskMessageListener taskMessageListener(TaskService taskService, ObjectMapper objectMapper) {
        return new TaskMessageListener(taskService, objectMapper);
    }

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public TaskMessagePublisher taskMessagePublisher(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        return new TaskMessagePublisher(redisTemplate, taskChannelName, objectMapper);
    }
}
