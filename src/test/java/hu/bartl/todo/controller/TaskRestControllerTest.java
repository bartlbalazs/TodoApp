package hu.bartl.todo.controller;

import hu.bartl.todo.conversion.TaskResourceAssembler;
import hu.bartl.todo.messaging.TaskMessagePublisher;
import hu.bartl.todo.model.Task;
import hu.bartl.todo.model.TaskResource;
import hu.bartl.todo.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskRestController.class)
public class TaskRestControllerTest {

    private static final String SAMPLE_UUID = "6a6d6728-115e-47f7-a23f-32b55182d55d";
    private static final String SAMPLE_DESCRIPTION = "sampleDescription";
    private static final int SAMPLE_TIMESTAMP = 0;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskMessagePublisher taskMessagePublisher;

    @MockBean
    private TaskResourceAssembler taskResourceAssembler;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Test
    public void shouldReturnAllTasksFromService() throws Exception {
        ArrayList<Task> sampleTasks = new ArrayList<>();
        sampleTasks.add(buildRandomTask());
        sampleTasks.add(buildRandomTask());
        when(taskService.getAllTasks()).thenReturn(sampleTasks);

        this.mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void shouldReturnLocationHeaderAfterPostRequest() throws Exception {
        TaskResource taskResource = new TaskResource();
        taskResource.setTaskId(randomUUID());
        taskResource.setCreatedAt(SAMPLE_TIMESTAMP);
        taskResource.setDescription(SAMPLE_DESCRIPTION);
        taskResource.add(new Link("sample", "self"));
        when(taskResourceAssembler.toResource(Mockito.any(Task.class))).thenReturn(taskResource);

        this.mockMvc.perform(post("/tasks")
                .content("{ \"description\" : \"" + SAMPLE_DESCRIPTION + "\" }")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", any(String.class)));
    }

    @Test
    public void shouldReturnTaskById() throws Exception {
        UUID taskID = UUID.fromString(SAMPLE_UUID);
        Instant sampleTimeStamp = Instant.ofEpochMilli(SAMPLE_TIMESTAMP);
        Task sampleTask = Task.builder().taskId(taskID).createdAt(sampleTimeStamp).description(SAMPLE_DESCRIPTION).build();

        TaskResource taskResource = new TaskResource();
        taskResource.setTaskId(taskID);
        taskResource.setCreatedAt(SAMPLE_TIMESTAMP);
        taskResource.setDescription(SAMPLE_DESCRIPTION);
        taskResource.add(new Link("sample", "self"));
        when(taskResourceAssembler.toResource(sampleTask)).thenReturn(taskResource);

        when(taskService.getTask(taskID)).thenReturn(Optional.of(sampleTask));

        this.mockMvc.perform(get("/tasks/" + SAMPLE_UUID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("taskId", is(SAMPLE_UUID)))
                .andExpect(jsonPath("createdAt", is(SAMPLE_TIMESTAMP)))
                .andExpect(jsonPath("description", is(SAMPLE_DESCRIPTION)))
                .andExpect(jsonPath("$", hasKey("_links")));
    }

    @Test
    public void shouldReturn404WhenTaskNotFound() throws Exception {
        when(taskService.getTask(UUID.fromString(SAMPLE_UUID))).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/tasks/" + SAMPLE_UUID))
                .andExpect(status().isNotFound());
    }

    private Task buildRandomTask() {
        return Task.builder()
                .taskId(randomUUID())
                .createdAt(Instant.now())
                .description("description")
                .build();
    }
}