package com.example.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ApiResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private Object data;
    private String path;

    public ApiResponse(int status, String message, Object data, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.data=data;
        this.path = path;
    }
}