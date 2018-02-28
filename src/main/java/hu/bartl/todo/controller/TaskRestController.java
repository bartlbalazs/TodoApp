package hu.bartl.todo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.bartl.todo.conversion.TaskResourceAssembler;
import hu.bartl.todo.exception.ResourceNotFoundException;
import hu.bartl.todo.messaging.TaskMessagePublisher;
import hu.bartl.todo.model.Task;
import hu.bartl.todo.model.TaskDto;
import hu.bartl.todo.model.TaskResource;
import hu.bartl.todo.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("tasks")
public class TaskRestController {

    private TaskService taskService;
    private TaskMessagePublisher taskMessagePublisher;
    private TaskResourceAssembler taskResourceAssembler;
    private SimpMessagingTemplate messagingTemplate;

    public TaskRestController(TaskService taskService, TaskMessagePublisher taskMessagePublisher, TaskResourceAssembler taskResourceAssembler, SimpMessagingTemplate messagingTemplate) {
        this.taskService = taskService;
        this.taskMessagePublisher = taskMessagePublisher;
        this.taskResourceAssembler = taskResourceAssembler;
        this.messagingTemplate = messagingTemplate;
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
        Optional<Task> optionalTask = taskService.getTask(UUID.fromString(taskId));
        if (optionalTask.isPresent()) {
            return ok(taskResourceAssembler.toResource(optionalTask.get()));
        }
        throw new ResourceNotFoundException(taskId);
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskDto taskDto) throws URISyntaxException, JsonProcessingException {
        Task task = Task.createWithDefaultValues(taskDto.getDescription());
        taskMessagePublisher.publishTaskCreationMessage(task);
        TaskResource taskResource = taskResourceAssembler.toResource(task);
        messagingTemplate.convertAndSend("/task/created", taskResource);
        return created(getTaskURI(taskResource)).build();
    }

    private URI getTaskURI(TaskResource taskResource) throws URISyntaxException {
        String selfLink = taskResource.getLink("self").getHref();
        return new URI(selfLink);
    }
}
