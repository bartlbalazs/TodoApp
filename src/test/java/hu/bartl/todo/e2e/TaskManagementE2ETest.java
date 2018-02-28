package hu.bartl.todo.e2e;

import hu.bartl.todo.model.Task;
import hu.bartl.todo.model.TaskResource;
import hu.bartl.todo.repository.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class TaskManagementE2ETest {

    private static final String SAMPLE_DESCRIPTION = "sampleDescription";
    private static final int PERSISTED_TASKS_COUNT = 5;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void shouldPersistNewMessage() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> taskRequest = new HttpEntity<>("{ \"description\":\"" + SAMPLE_DESCRIPTION + "\"}", headers);

        restTemplate.postForLocation("/tasks", taskRequest);
        Task createdTask = taskRepository.findAll().iterator().next();
        assertEquals(createdTask.getDescription(), SAMPLE_DESCRIPTION);
    }

    @Test
    public void shouldGetAlreadyPersistedTaskTas() {
        Task task = Task.createWithDefaultValues(SAMPLE_DESCRIPTION);
        taskRepository.save(task);

        ResponseEntity<TaskResource> taskResource = restTemplate
                .getForEntity("/tasks/" + task.getId().toString(), TaskResource.class);

        assertEquals(taskResource.getBody().getTaskId(), task.getId());
    }

    @Test
    public void shouldGetAlreadyPersistedTaskTasks() {
        for (int i = 0; i < PERSISTED_TASKS_COUNT; i++) {
            Task task = Task.createWithDefaultValues(SAMPLE_DESCRIPTION);
            taskRepository.save(task);
        }
        ResponseEntity<List<TaskResource>> taskResource = restTemplate
                .exchange("/tasks", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<TaskResource>>() {
                });

        assertEquals(PERSISTED_TASKS_COUNT, taskResource.getBody().size());
    }

    @Before
    public void cleanUp() {
        taskRepository.deleteAll();
    }
}
