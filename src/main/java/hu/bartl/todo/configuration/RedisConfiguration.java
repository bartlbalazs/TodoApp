package hu.bartl.todo.configuration;

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

    @Value("${todoapp.messagint.taskchannel}")
    private String taskChannelName;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(taskChannelName));

        return container;
    }

    @Bean
    public TaskMessageListener taskMessageListener(TaskService taskService) {
        return new TaskMessageListener(taskService);
    }

    @Bean
    public TaskMessagePublisher taskMessagePublisher(StringRedisTemplate redisTemplate) {
        return new TaskMessagePublisher(redisTemplate, taskChannelName);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(TaskMessageListener taskMessageListener) {
        return new MessageListenerAdapter(taskMessageListener, "receiveTaskMessage");
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
