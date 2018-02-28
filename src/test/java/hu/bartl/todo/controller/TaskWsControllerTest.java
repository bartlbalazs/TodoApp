package hu.bartl.todo.controller;

import hu.bartl.todo.itegration.EmbeddedRedis;
import hu.bartl.todo.model.Task;
import hu.bartl.todo.model.TaskDto;
import hu.bartl.todo.model.TaskResource;
import hu.bartl.todo.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class TaskWsControllerTest {

    private static final String SAMPLE_DESCRIPTION = "sampleDescription";

    @Value("${local.server.port}")
    private int port;

    @MockBean
    private TaskService taskService;

    @MockBean
    private EmbeddedRedis redis;

    private WebSocketStompClient stompClient;

    @Before
    public void setup() {
        stompClient = new WebSocketStompClient(
                new SockJsClient(asList((new WebSocketTransport(new StandardWebSocketClient())))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void shouldClientConnectToTopic() throws Exception {
        StompSession session = getStompSession();
        assertNotNull(session);
    }

    @Test
    public void shouldSendMessageToClient() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setDescription(SAMPLE_DESCRIPTION);

        StompFrameHandler stompFrameHandler = Mockito.spy(StompFrameHandler.class);

        StompSession stompSession = getStompSession();
        stompSession.subscribe("/task/created", stompFrameHandler);

        stompSession.send("/app/createTask", taskDto);

        verify(stompFrameHandler, timeout(1000)).getPayloadType(any());
    }

    @Test
    public void shouldReceiveMessageFromClient() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setDescription(SAMPLE_DESCRIPTION);

        StompSession stompSession = getStompSession();
        stompSession.send("/app/createTask", taskDto);

        verify(taskService, timeout(1000)).persistTask(any(Task.class));
    }

    private StompSession getStompSession() throws Exception {
        StompSession stompSession = stompClient.connect("ws://localhost:" + port + "/gs-guide-websocket", new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);
        return stompSession;

    }
}