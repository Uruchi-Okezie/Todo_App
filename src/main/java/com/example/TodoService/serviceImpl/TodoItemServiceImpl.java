package com.example.TodoService.serviceImpl;

import com.example.TodoService.dto.TodoItemDto;
import com.example.TodoService.exception.InvalidTodoItemException;
import com.example.TodoService.exception.TodoItemNotFoundException;
import com.example.TodoService.model.TodoItem;
import com.example.TodoService.repository.TodoItemRepository;
import com.example.TodoService.service.TodoItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemRepository todoItemRepository;

    @Override
    public TodoItemDto createTodoItem(TodoItemDto todoItemDto) {
        validateTodoItemDto(todoItemDto);
        TodoItem todoItem = convertToEntity(todoItemDto);
        TodoItem savedItem = todoItemRepository.save(todoItem);
        return convertToDto(savedItem);
    }

    @Override
    public List<TodoItemDto> getAllTodoItems(String priority, LocalDate dueDate) {
        List<TodoItem> items = todoItemRepository.findAll();
        return items.stream()
                .filter(item -> isPriorityMatch(item, priority))
                .filter(item -> isDueDateMatch(item, dueDate))
                .sorted(Comparator.comparing(TodoItem::getDueDate))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TodoItemDto getTodoItemById(Long id) {
        TodoItem todoItem = findTodoItemById(id);
        return convertToDto(todoItem);
    }

    @Override
    public TodoItemDto updateTodoItem(Long id, TodoItemDto todoItemDto) {
        validateTodoItemDto(todoItemDto);
        TodoItem existingTodoItem = findTodoItemById(id);
        updateTodoItemFields(existingTodoItem, todoItemDto);
        TodoItem updatedTodoItem = todoItemRepository.save(existingTodoItem);
        return convertToDto(updatedTodoItem);
    }

    @Override
    public void deleteTodoItem(Long id) {
        findTodoItemById(id); // Ensures the item exists
        todoItemRepository.deleteById(id);
    }

    private TodoItem findTodoItemById(Long id) {
        return todoItemRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException("Todo Item not found with ID: " + id));
    }

    private boolean isPriorityMatch(TodoItem item, String priority) {
        return priority == null || item.getPriority().equalsIgnoreCase(priority);
    }

    private boolean isDueDateMatch(TodoItem item, LocalDate dueDate) {
        return dueDate == null || item.getDueDate().equals(dueDate);
    }

    private void updateTodoItemFields(TodoItem existingTodoItem, TodoItemDto todoItemDto) {
        existingTodoItem.setTitle(todoItemDto.getTitle());
        existingTodoItem.setDescription(todoItemDto.getDescription());
        existingTodoItem.setDueDate(todoItemDto.getDueDate());
        existingTodoItem.setPriority(todoItemDto.getPriority());
        existingTodoItem.setCompleted(todoItemDto.isCompleted());
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

    private TodoItem convertToEntity(TodoItemDto todoItemDto) {
        return TodoItem.builder()
                .title(todoItemDto.getTitle())
                .description(todoItemDto.getDescription())
                .dueDate(todoItemDto.getDueDate())
                .priority(todoItemDto.getPriority())
                .completed(todoItemDto.isCompleted())
                .build();
    }

    private void validateTodoItemDto(TodoItemDto todoItemDto) {
        if (todoItemDto.getTitle() == null || todoItemDto.getTitle().trim().isEmpty()) {
            throw new InvalidTodoItemException("Todo item title cannot be empty");
        }
        if (todoItemDto.getDueDate() != null && todoItemDto.getDueDate().isBefore(LocalDate.now())) {
            throw new InvalidTodoItemException("Due date cannot be in the past");
        }

    }
}