package com.example.TodoService.exception;

public class InvalidTodoItemException extends RuntimeException {
    public InvalidTodoItemException(String message) {
        super(message);
    }
}