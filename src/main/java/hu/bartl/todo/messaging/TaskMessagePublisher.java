package hu.bartl.todo.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bartl.todo.model.Task;
import org.springframework.data.redis.core.StringRedisTemplate;

public class TaskMessagePublisher {

    private final StringRedisTemplate redisTemplate;
    private final String tackChannel;
    private final ObjectMapper objectMapper;

    public TaskMessagePublisher(StringRedisTemplate redisTemplate, String taskChannel, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.tackChannel = taskChannel;
        this.objectMapper = objectMapper;
    }

    public void publishTaskCreationMessage(Task task) throws JsonProcessingException {
        redisTemplate.convertAndSend( tackChannel, objectMapper.writeValueAsString(task));
    }
}
