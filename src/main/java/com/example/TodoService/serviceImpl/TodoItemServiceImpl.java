package com.example.TodoService.serviceImpl;

import com.example.TodoService.dto.TodoItemDto;
import com.example.TodoService.model.TodoItem;
import com.example.TodoService.repository.TodoItemRepository;
import com.example.TodoService.service.TodoItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemRepository todoItemRepository;

    @Override
    public TodoItemDto createTodoItem(TodoItemDto todoItemDto) {
        TodoItem todoItem = TodoItem.builder()
                .title(todoItemDto.getTitle())
                .description(todoItemDto.getDescription())
                .dueDate(todoItemDto.getDueDate())
                .priority(todoItemDto.getPriority())
                .completed(todoItemDto.isCompleted())
                .build();
        todoItem = todoItemRepository.save(todoItem);
        return convertToDto(todoItem);
    }

    @Override
    public List<TodoItemDto> getAllTodoItems(String priority, LocalDate dueDate) {
        List<TodoItem> items = todoItemRepository.findAll();
        return items.stream()
                .filter(item -> (priority == null || item.getPriority().equalsIgnoreCase(priority)))
                .filter(item -> (dueDate == null || item.getDueDate().equals(dueDate)))
                .sorted(Comparator.comparing(TodoItem::getDueDate)) // Example sorting by due date
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public TodoItemDto getTodoItemById(Long id) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo Item not found with ID: " + id));
        return convertToDto(todoItem);
    }

    @Override
    public TodoItemDto updateTodoItem(Long id, TodoItemDto todoItemDto) {
        TodoItem existingTodoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo Item not found with ID: " + id));
        existingTodoItem.setTitle(todoItemDto.getTitle());
        existingTodoItem.setDescription(todoItemDto.getDescription());
        existingTodoItem.setDueDate(todoItemDto.getDueDate());
        existingTodoItem.setPriority(todoItemDto.getPriority());
        existingTodoItem.setCompleted(todoItemDto.isCompleted());
        TodoItem updatedTodoItem = todoItemRepository.save(existingTodoItem);
        return convertToDto(updatedTodoItem);
    }

    @Override
    public void deleteTodoItem(Long id) {
        todoItemRepository.deleteById(id);
    }

    private TodoItemDto convertToDto(TodoItem todoItem) {
        return TodoItemDto.builder()
                .id(todoItem.getId())
                .title(todoItem.getTitle())
                .description(todoItem.getDescription())
                .dueDate(todoItem.getDueDate())
                .priority(todoItem.getPriority())
                .completed(todoItem.isCompleted())
                .build();
    }
}