package hu.bartl.todo.controller;

import hu.bartl.todo.conversion.TaskResourceAssembler;
import hu.bartl.todo.messaging.TaskMessagePublisher;
import hu.bartl.todo.model.Task;
import hu.bartl.todo.model.TaskDto;
import hu.bartl.todo.model.TaskResource;
import hu.bartl.todo.service.TaskService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TaskWsController {

    private TaskMessagePublisher taskMessagePublisher;
    private TaskResourceAssembler taskResourceAssembler;

    public TaskWsController(TaskMessagePublisher taskMessagePublisher, TaskResourceAssembler taskResourceAssembler) {
        this.taskMessagePublisher = taskMessagePublisher;
        this.taskResourceAssembler = taskResourceAssembler;
    }

    @MessageMapping("/createTask")
    @SendTo("/task/created")
    public TaskResource createTask(TaskDto taskDto) throws Exception {
        Task task = Task.createWithDefaultValues(taskDto.getDescription());
        taskMessagePublisher.publishTaskCreationMessage(task);
        return taskResourceAssembler.toResource(task);
    }
}
