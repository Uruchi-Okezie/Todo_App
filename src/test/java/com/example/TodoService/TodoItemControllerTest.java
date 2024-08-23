package com.example.TodoService;

import com.example.TodoService.controller.TodoItemController;
import com.example.TodoService.dto.TodoItemDto;
import com.example.TodoService.model.Priority;
import com.example.TodoService.service.TodoItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoItemController.class)
public class TodoItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoItemService todoItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private TodoItemDto sampleTodoItem;
    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        sampleTodoItem = TodoItemDto.builder()
                .id(1L)
                .title("Sample Todo")
                .description("Sample Description")
                .dueDate(new Date(System.currentTimeMillis() + 86400000))
                .priority(Priority.HIGH)
                .completed(false)
                .build();
    }

    @Test
    void testCreateTodoItem() throws Exception {
        when(todoItemService.createTodoItem(any(TodoItemDto.class))).thenReturn(sampleTodoItem);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTodoItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Todo"));
    }



    @Test
    void testCreateTodoItemWithMaxLengthTitle() throws Exception {
        String maxLengthTitle = "a".repeat(100); // Assuming 100 is max length
        sampleTodoItem.setTitle(maxLengthTitle);

        when(todoItemService.createTodoItem(any(TodoItemDto.class))).thenReturn(sampleTodoItem);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTodoItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(maxLengthTitle));
    }




    @Test
    void testCreateTodoItemWithMalformedJson() throws Exception {
        String malformedJson = "{ \"title\": \"Incomplete JSON";

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllTodoItems() throws Exception {
        when(todoItemService.getAllTodoItems(null, null)).thenReturn(Arrays.asList(sampleTodoItem));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Sample Todo"));
    }

    @Test
    void testGetAllTodoItemsWhenEmpty() throws Exception {
        when(todoItemService.getAllTodoItems(null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetAllTodoItemsWithFilters() throws Exception {
        when(todoItemService.getAllTodoItems(eq("HIGH"), any(java.time.LocalDate.class)))
                .thenReturn(Collections.singletonList(sampleTodoItem));

        mockMvc.perform(get("/api/todos")
                        .param("priority", "HIGH")
                        .param("dueDate", dateFormat.format(new Date())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority").value("HIGH"));
    }

    @Test
    void testGetTodoItemById() throws Exception {
        when(todoItemService.getTodoItemById(1L)).thenReturn(sampleTodoItem);

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void testUpdateTodoItem() throws Exception {
        when(todoItemService.updateTodoItem(eq(1L), any(TodoItemDto.class))).thenReturn(sampleTodoItem);

        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTodoItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }



    @Test
    void testDeleteTodoItem() throws Exception {
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }
}
