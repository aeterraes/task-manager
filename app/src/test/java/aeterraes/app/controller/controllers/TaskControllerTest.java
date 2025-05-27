package aeterraes.app.controller.controllers;

import aeterraes.app.controller.dto.TaskDTO;
import aeterraes.app.dataaccess.entity.Task;
import aeterraes.app.dataaccess.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = { "topic-name" },
        brokerProperties = { "listeners=PLAINTEXT://localhost:9093", "port=9093" })
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
        getExampleTasks();
        taskRepository.saveAll(List.of(task1, task2));
    }

    @Test
    void getAllTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("title"))
                .andExpect(jsonPath("$[1].title").value("title2"));
    }

    @Test
    void getTaskById() throws Exception {
        mockMvc.perform(get("/tasks/" + task1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.status").value("status"));
    }

    @Test
    void createTask() throws Exception {
        TaskDTO newTask = new TaskDTO();
        newTask.setTitle("I tried to be perfect");
        newTask.setDescription("But nothing was worth it");
        newTask.setUserId(1);
        newTask.setStatus("I don't believe it makes me real");
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newTask)))
                .andExpect(status().isOk());
        Assertions.assertEquals(3, taskRepository.count());
    }

    @Test
    void updateTask() throws Exception {
        TaskDTO updatedTask = new TaskDTO();
        updatedTask.setTitle("I thought it'd be easy");
        updatedTask.setDescription("But no one believes me");
        updatedTask.setUserId(1);
        updatedTask.setStatus("I meant all the things I said");
        mockMvc.perform(put("/tasks/" + task1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("I thought it'd be easy"))
                .andExpect(jsonPath("$.status").value("I meant all the things I said"));
    }

    @Test
    void updateTaskNotFound() throws Exception {
        TaskDTO updatedTask = new TaskDTO();
        updatedTask.setTitle("I would love to go");
        updatedTask.setDescription("Back to the old house");
        updatedTask.setUserId(1);
        updatedTask.setStatus("But I never will");
        mockMvc.perform(put("/tasks/1337")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTask)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/" + task1.getId()))
                .andExpect(status().isOk());
        Assertions.assertFalse(taskRepository.findById(task1.getId()).isPresent());
    }

    private void getExampleTasks() {
        task1 = new Task();
        task1.setTitle("title");
        task1.setDescription("description");
        task1.setStatus("status");
        task1.setUserId(1);

        task2 = new Task();
        task2.setTitle("title2");
        task2.setDescription("description2");
        task2.setStatus("status2");
        task2.setUserId(1);
    }

    private static String asJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("pupupu", e);
        }
    }
}