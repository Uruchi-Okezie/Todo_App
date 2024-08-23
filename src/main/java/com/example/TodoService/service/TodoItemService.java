package com.example.TodoService.service;

import com.example.TodoService.dto.TodoItemDto;
import com.example.TodoService.model.TodoItem;

import java.time.LocalDate;
import java.util.List;

public interface TodoItemService {

    TodoItemDto createTodoItem(TodoItemDto todoItemDto);

    List<TodoItemDto> getAllTodoItems(String priority, LocalDate dueDate, String sortBy);

    TodoItemDto getTodoItemById(Long id);

    TodoItemDto updateTodoItem(Long id, TodoItemDto todoItemDto);

    void deleteTodoItem(Long id);
}

