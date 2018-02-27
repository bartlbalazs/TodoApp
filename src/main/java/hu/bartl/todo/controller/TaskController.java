package hu.bartl.todo.controller;

import hu.bartl.todo.conversion.TaskResourceAssembler;
import hu.bartl.todo.model.Task;
import hu.bartl.todo.model.TaskDto;
import hu.bartl.todo.model.TaskResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("tasks")
public class TaskController {

    private TaskResourceAssembler taskResourceAssembler;

    public TaskController(TaskResourceAssembler taskResourceAssembler) {
        this.taskResourceAssembler = taskResourceAssembler;
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<TaskResource>> getAllTasks() {
        ArrayList<TaskResource> tasks = new ArrayList<>();
        return ok(tasks);
    }

    @GetMapping(value = "/{taskId}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TaskResource> getTask(@PathVariable String taskId) {
        Task sampleTask = new Task();
        sampleTask.setTaskId(randomUUID());
        sampleTask.setCreationTime();
        sampleTask.setDescription("sample");
        return ResponseEntity.ok(taskResourceAssembler.toResource(sampleTask));
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskDto taskDto) {
        return accepted().build();
    }
}
