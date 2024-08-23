package com.example.TodoService.dto;

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
    private String title;
    private String description;
    private Date dueDate;
    private String priority;
    private boolean completed;

}

