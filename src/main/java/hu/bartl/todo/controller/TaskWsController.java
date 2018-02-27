package hu.bartl.todo.controller;

import hu.bartl.todo.model.TaskDto;
import hu.bartl.todo.service.TaskService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TaskWsController {

    private TaskService taskService;

    public TaskWsController(TaskService taskService) {
        this.taskService = taskService;
    }

    @MessageMapping("/createTask")
    public void createTask(TaskDto taskDto) throws Exception {
        taskService.createTaskWithDescription(taskDto.getDescription());
    }
}
