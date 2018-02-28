package hu.bartl.todo.service;

import hu.bartl.todo.model.Task;
import hu.bartl.todo.repository.TaskRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TaskService {

    private static final Logger LOGGER = getLogger(TaskService.class);

    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void persistTask(Task task) {
        taskRepository.save(task);
    }

    public Optional<Task> getTask(UUID taskID) {
        return Optional.ofNullable(taskRepository.findOne(taskID));
    }

    public List<Task> getAllTasks() {
        List<Task> result = new ArrayList<>();
        taskRepository.findAll().forEach(result::add);
        return result;
    }
}
