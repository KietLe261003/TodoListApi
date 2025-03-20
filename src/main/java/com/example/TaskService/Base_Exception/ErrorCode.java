package com.example.TaskService.Base_Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ErrorCode {
    NotFoundTask(1000,"Not found Task", org.springframework.http.HttpStatus.NOT_FOUND),
    NotFoundDependencyTask(1000,"Not found Dependency Task", org.springframework.http.HttpStatus.NOT_FOUND),
    WrongCircularDependencies(1001," Wrong Circular Dependencies", org.springframework.http.HttpStatus.BAD_REQUEST),
    DependenciesCreated(1001,"Dependencies created in task", org.springframework.http.HttpStatus.BAD_REQUEST),
    ;
    Integer code;
    String message;
    HttpStatus HttpStatus;
    ErrorCode(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.HttpStatus = httpStatus;
    }

}
