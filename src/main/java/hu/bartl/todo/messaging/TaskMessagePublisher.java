package hu.bartl.todo.messaging;

import org.springframework.data.redis.core.StringRedisTemplate;

public class TaskMessagePublisher {

    private final StringRedisTemplate redisTemplate;
    private final String tackChannel;

    public TaskMessagePublisher(StringRedisTemplate redisTemplate, String taskChannel) {
        this.redisTemplate = redisTemplate;
        this.tackChannel = taskChannel;
    }

    public void publishTaskCreationMessage(String message) {
        redisTemplate.convertAndSend(tackChannel, message);
    }
}
