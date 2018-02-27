package hu.bartl.todo.service;

import hu.bartl.todo.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    public void createTaskWithDescription(String description) {
        LOGGER.info("message arrived: " + description);
    }

    public Task getTask(UUID taskID) {
        return null;
    }

    public List<Task> getAllTasks() {
        return null;
    }
}
