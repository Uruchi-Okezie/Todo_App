package com.example.TodoService;

import com.example.TodoService.controller.TodoItemController;
import com.example.TodoService.dto.TodoItemDto;
import com.example.TodoService.exception.InvalidTodoItemException;
import com.example.TodoService.exception.TodoItemNotFoundException;
import com.example.TodoService.service.TodoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoItemControllerTest {

    @Mock
    private TodoItemService todoItemService;

    @InjectMocks
    private TodoItemController todoItemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTodoItem() {
        TodoItemDto inputDto = TodoItemDto.builder()
                .title("Test Todo")
                .description("Test Description")
                .dueDate(LocalDate.now().plusDays(1))
                .priority("HIGH")
                .completed(false)
                .build();

        TodoItemDto outputDto = TodoItemDto.builder()
                .id(1L)
                .title("Test Todo")
                .description("Test Description")
                .dueDate(LocalDate.now().plusDays(1))
                .priority("HIGH")
                .completed(false)
                .build();

        when(todoItemService.createTodoItem(inputDto)).thenReturn(outputDto);

        ResponseEntity<TodoItemDto> response = todoItemController.createTodoItem(inputDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(outputDto, response.getBody());
        verify(todoItemService, times(1)).createTodoItem(inputDto);
    }

    @Test
    void testGetAllTodoItems() {
        String priority = "HIGH";
        LocalDate dueDate = LocalDate.now();

        List<TodoItemDto> todoItems = Arrays.asList(
                TodoItemDto.builder().id(1L).title("Todo 1").priority("HIGH").dueDate(LocalDate.now()).build(),
                TodoItemDto.builder().id(2L).title("Todo 2").priority("HIGH").dueDate(LocalDate.now()).build()
        );

        when(todoItemService.getAllTodoItems(priority, dueDate)).thenReturn(todoItems);

        ResponseEntity<List<TodoItemDto>> response = todoItemController.getAllTodoItems(priority, dueDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(todoItems, response.getBody());
        verify(todoItemService, times(1)).getAllTodoItems(priority, dueDate);
    }

    @Test
    void testGetTodoItemById() {
        Long id = 1L;
        TodoItemDto todoItem = TodoItemDto.builder()
                .id(id)
                .title("Test Todo")
                .description("Test Description")
                .dueDate(LocalDate.now())
                .priority("HIGH")
                .completed(false)
                .build();

        when(todoItemService.getTodoItemById(id)).thenReturn(todoItem);

        ResponseEntity<TodoItemDto> response = todoItemController.getTodoItemById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(todoItem, response.getBody());
        verify(todoItemService, times(1)).getTodoItemById(id);
    }

    @Test
    void testUpdateTodoItem() {
        Long id = 1L;
        TodoItemDto inputDto = TodoItemDto.builder()
                .title("Updated Todo")
                .description("Updated Description")
                .dueDate(LocalDate.now().plusDays(2))
                .priority("LOW")
                .completed(true)
                .build();

        TodoItemDto outputDto = TodoItemDto.builder()
                .id(id)
                .title("Updated Todo")
                .description("Updated Description")
                .dueDate(LocalDate.now().plusDays(2))
                .priority("LOW")
                .completed(true)
                .build();

        when(todoItemService.updateTodoItem(id, inputDto)).thenReturn(outputDto);

        ResponseEntity<TodoItemDto> response = todoItemController.updateTodoItem(id, inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(outputDto, response.getBody());
        verify(todoItemService, times(1)).updateTodoItem(id, inputDto);
    }

    @Test
    void testDeleteTodoItem() {
        Long id = 1L;

        ResponseEntity<Void> response = todoItemController.deleteTodoItem(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(todoItemService, times(1)).deleteTodoItem(id);
    }
    @Test
    void testCreateTodoItem_WithInvalidData() {
        TodoItemDto inputDto = TodoItemDto.builder()
                .title("")  // Invalid: empty title
                .dueDate(LocalDate.now().minusDays(1))  // Invalid: past due date
                .build();

        when(todoItemService.createTodoItem(inputDto)).thenThrow(new InvalidTodoItemException("Invalid todo item data"));

        assertThrows(InvalidTodoItemException.class, () -> todoItemController.createTodoItem(inputDto));
        verify(todoItemService, times(1)).createTodoItem(inputDto);
    }

    @Test
    void testGetTodoItemById_NonExistent() {
        Long id = 999L;
        when(todoItemService.getTodoItemById(id)).thenThrow(new TodoItemNotFoundException("Todo item not found"));

        assertThrows(TodoItemNotFoundException.class, () -> todoItemController.getTodoItemById(id));
        verify(todoItemService, times(1)).getTodoItemById(id);
    }

    @Test
    void testUpdateTodoItem_NonExistent() {
        Long id = 999L;
        TodoItemDto inputDto = TodoItemDto.builder()
                .title("Updated Todo")
                .description("Updated Description")
                .dueDate(LocalDate.now().plusDays(2))
                .priority("LOW")
                .completed(true)
                .build();

        when(todoItemService.updateTodoItem(id, inputDto)).thenThrow(new TodoItemNotFoundException("Todo item not found"));

        assertThrows(TodoItemNotFoundException.class, () -> todoItemController.updateTodoItem(id, inputDto));
        verify(todoItemService, times(1)).updateTodoItem(id, inputDto);
    }

    @Test
    void testDeleteTodoItem_NonExistent() {
        Long id = 999L;
        doThrow(new TodoItemNotFoundException("Todo item not found")).when(todoItemService).deleteTodoItem(id);

        assertThrows(TodoItemNotFoundException.class, () -> todoItemController.deleteTodoItem(id));
        verify(todoItemService, times(1)).deleteTodoItem(id);
    }

    @Test
    void testGetAllTodoItems_WithInvalidPriority() {
        String invalidPriority = "INVALID_PRIORITY";
        LocalDate dueDate = LocalDate.now();

        when(todoItemService.getAllTodoItems(invalidPriority, dueDate)).thenThrow(new InvalidTodoItemException("Invalid priority"));

        assertThrows(InvalidTodoItemException.class, () -> todoItemController.getAllTodoItems(invalidPriority, dueDate));
        verify(todoItemService, times(1)).getAllTodoItems(invalidPriority, dueDate);
    }

    @Test
    void testGetAllTodoItems_WithFutureDueDate() {
        String priority = "HIGH";
        LocalDate futureDate = LocalDate.now().plusYears(100);  // Edge case: very far future date

        List<TodoItemDto> emptyList = Arrays.asList();
        when(todoItemService.getAllTodoItems(priority, futureDate)).thenReturn(emptyList);

        ResponseEntity<List<TodoItemDto>> response = todoItemController.getAllTodoItems(priority, futureDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(todoItemService, times(1)).getAllTodoItems(priority, futureDate);
    }
}