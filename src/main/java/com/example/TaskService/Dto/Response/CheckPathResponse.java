package com.example.TaskService.Dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.Stack;
@Getter
@Setter
public class CheckPathResponse {
    Boolean success;
    Stack<String> stack;
}
