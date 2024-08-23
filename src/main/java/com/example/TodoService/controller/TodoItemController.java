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
        return new ResponseEntity<>(todoItemService.createTodoItem(todoItemDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TodoItemDto>> getAllTodoItems(
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {
        return ResponseEntity.ok(todoItemService.getAllTodoItems(priority, dueDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItemDto> getTodoItemById(@PathVariable Long id) {
        return ResponseEntity.ok(todoItemService.getTodoItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoItemDto> updateTodoItem(@PathVariable Long id, @RequestBody TodoItemDto todoItemDto) {
        return ResponseEntity.ok(todoItemService.updateTodoItem(id, todoItemDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long id) {
        todoItemService.deleteTodoItem(id);
        return ResponseEntity.noContent().build();
    }
}