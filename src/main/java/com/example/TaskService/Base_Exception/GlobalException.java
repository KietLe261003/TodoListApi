package com.example.TaskService.Base_Exception;


import com.example.TaskService.Dto.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> appException(AppException e) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(e.errorCode.getCode())
                .message(e.errorCode.getMessage())
                .build();
        return ResponseEntity.status(e.errorCode.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> runtimeException(RuntimeException e) {
        ApiResponse apiResponse= ApiResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
