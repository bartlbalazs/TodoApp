package hu.bartl.todo.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    private static final String SAMPLE_DESCRIPTION = "sampleDescription";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnEmptyTaskList() throws Exception {
        this.mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json("[]"));
    }

    @Test
    public void shouldReturnLocationHeaderAfterTaskCreated() throws Exception {
        this.mockMvc.perform(post("/tasks")
                .content("{ \"description\" : \"" + SAMPLE_DESCRIPTION + "\" }")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void shouldReturnSampleTask() throws Exception {
        this.mockMvc.perform(get("/tasks/any-id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("description", is("sample")))
                .andExpect(jsonPath("$", hasKey("_links")));
    }
}