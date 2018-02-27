package hu.bartl.todo.e2e;

import hu.bartl.todo.model.Task;
import hu.bartl.todo.model.TaskResource;
import hu.bartl.todo.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class TaskManagementE2ETest {

    private static final String SAMPLE_DESCRIPTION = "sampleDescription";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void shouldPersistNewMessage() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> taskRequest = new HttpEntity<String>("{ \"description\":\"" + SAMPLE_DESCRIPTION + "\"}", headers);

        restTemplate.postForLocation("/tasks", taskRequest);
        Task createdTask = taskRepository.findAll().iterator().next();
        assertEquals(createdTask.getDescription(), SAMPLE_DESCRIPTION);
    }

    @Test
    public void shouldGetAlreadyPersistedTaskTask() {
        Task task = Task.builder().description(SAMPLE_DESCRIPTION).build();
        UUID taskId = taskRepository.save(task).getId();

        ResponseEntity<TaskResource> taskResource = restTemplate
                .getForEntity("/tasks/" + taskId.toString(), TaskResource.class);

        assertEquals(taskResource.getBody().getTaskId(), taskId);
    }
}
