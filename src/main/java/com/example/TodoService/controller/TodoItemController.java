package com.example.TodoService.controller;

import com.example.TodoService.dto.TodoItemDto;
import com.example.TodoService.service.TodoItemService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@AllArgsConstructor
public class TodoItemController {

    private final TodoItemService todoItemService;

    @PostMapping
    public ResponseEntity<TodoItemDto> createTodoItem(@RequestBody TodoItemDto todoItemDto) {
        TodoItemDto createdTodoItem = todoItemService.createTodoItem(todoItemDto);
        return new ResponseEntity<>(createdTodoItem, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TodoItemDto>> getAllTodoItems(
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {

        List<TodoItemDto> items = todoItemService.getAllTodoItems(priority, dueDate);
        return ResponseEntity.ok(items);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TodoItemDto> getTodoItemById(@PathVariable Long id) {
        try {
            TodoItemDto todoItemDto = todoItemService.getTodoItemById(id);
            return ResponseEntity.ok(todoItemDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoItemDto> updateTodoItem(@PathVariable Long id, @RequestBody TodoItemDto todoItemDto) {
        try {
            TodoItemDto updatedTodoItem = todoItemService.updateTodoItem(id, todoItemDto);
            return ResponseEntity.ok(updatedTodoItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long id) {
        try {
            todoItemService.deleteTodoItem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}