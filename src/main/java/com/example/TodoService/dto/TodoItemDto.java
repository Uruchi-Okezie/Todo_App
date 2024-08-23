package com.example.TodoService.dto;

import com.example.TodoService.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TodoItemDto {
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    @NotNull(message = "Due date is required")
    private Date dueDate;
    @NotNull(message = "Priority is required")
    private Priority priority;
    private boolean completed;
}

