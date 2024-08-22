package com.example.TodoService.service;

import com.example.TodoService.dto.TodoItemDto;
import com.example.TodoService.model.TodoItem;

import java.time.LocalDate;
import java.util.List;

public interface TodoItemService {
    TodoItemDto createTodoItem(TodoItemDto todoItemDto);
    TodoItemDto getTodoItemById(Long id);
    List<TodoItemDto> getAllTodoItems(String priority, LocalDate dueDate);

    TodoItemDto updateTodoItem(Long id, TodoItemDto todoItemDto);
    void deleteTodoItem(Long id);
}
