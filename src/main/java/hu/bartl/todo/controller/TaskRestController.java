package hu.bartl.todo.controller;

import hu.bartl.todo.conversion.TaskResourceAssembler;
import hu.bartl.todo.messaging.TaskMessagePublisher;
import hu.bartl.todo.model.Task;
import hu.bartl.todo.model.TaskDto;
import hu.bartl.todo.model.TaskResource;
import hu.bartl.todo.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("tasks")
public class TaskRestController {

    private TaskService taskService;
    private TaskMessagePublisher taskMessagePublisher;
    private TaskResourceAssembler taskResourceAssembler;

    public TaskRestController(TaskService taskService, TaskMessagePublisher taskMessagePublisher, TaskResourceAssembler taskResourceAssembler) {
        this.taskService = taskService;
        this.taskMessagePublisher = taskMessagePublisher;
        this.taskResourceAssembler = taskResourceAssembler;
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<TaskResource>> getAllTasks() {
        List<TaskResource> taskResources = taskService.getAllTasks()
                .stream()
                .map(taskResourceAssembler::toResource)
                .collect(toList());
        return ok(taskResources);
    }

    @GetMapping(value = "/{taskId}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TaskResource> getTask(@PathVariable String taskId) {
        Task task = taskService.getTask(UUID.fromString(taskId));
        return ok(taskResourceAssembler.toResource(task));
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskDto taskDto) {
        taskMessagePublisher.publishTaskCreationMessage(taskDto.getDescription());
        return accepted().build();
    }
}
