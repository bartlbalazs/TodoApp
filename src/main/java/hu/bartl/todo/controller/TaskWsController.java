package hu.bartl.todo.controller;

import hu.bartl.todo.conversion.TaskResourceAssembler;
import hu.bartl.todo.model.Task;
import hu.bartl.todo.model.TaskDto;
import hu.bartl.todo.model.TaskResource;
import hu.bartl.todo.service.TaskService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TaskWsController {

    private TaskService taskService;
    private TaskResourceAssembler taskResourceAssembler;

    public TaskWsController(TaskService taskService, TaskResourceAssembler taskResourceAssembler) {
        this.taskService = taskService;
        this.taskResourceAssembler = taskResourceAssembler;
    }

    @MessageMapping("/createTask")
    @SendTo("/task/created")
    public TaskResource createTask(TaskDto taskDto) throws Exception {
        Task task = Task.createWithDefaultValues(taskDto.getDescription());
        taskService.persistTask(task);
        return taskResourceAssembler.toResource(task);
    }
}
