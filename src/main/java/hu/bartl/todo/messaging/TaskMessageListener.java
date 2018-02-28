package hu.bartl.todo.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bartl.todo.model.Task;
import hu.bartl.todo.service.TaskService;

import java.io.IOException;

public class TaskMessageListener {

    private TaskService taskService;
    private ObjectMapper objectMapper;

    public TaskMessageListener(TaskService taskService, ObjectMapper objectMapper) {
        this.taskService = taskService;
        this.objectMapper = objectMapper;
    }

    public void receiveTaskMessage(String taskMessage) throws IOException {
        taskService.persistTask(objectMapper.readValue(taskMessage, Task.class));
    }
}
