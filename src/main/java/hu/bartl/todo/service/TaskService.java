package hu.bartl.todo.service;

import hu.bartl.todo.model.Task;
import hu.bartl.todo.repository.TaskRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TaskService {

    private static final Logger LOGGER = getLogger(TaskService.class);

    @Value("${todoapp.messaging.broker}")
    private String brokerName;

    private TaskRepository taskRepository;
    private SimpMessagingTemplate messagingTemplate;

    public TaskService(TaskRepository taskRepository, SimpMessagingTemplate messagingTemplate) {
        this.taskRepository = taskRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void createTaskWithDescription(String description) {
        Task task = Task.builder().description(description).build();
        taskRepository.save(task);
        LOGGER.info("Task persisted: {}", task);
        messagingTemplate.convertAndSend("/" + brokerName + "/created", task);
    }

    public Task getTask(UUID taskID) {
        return taskRepository.findOne(taskID);
    }

    public List<Task> getAllTasks() {
        List<Task> result = new ArrayList<>();
        taskRepository.findAll().forEach(result::add);
        return result;
    }
}
