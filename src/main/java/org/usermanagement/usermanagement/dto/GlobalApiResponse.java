package org.usermanagement.usermanagement.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalApiResponse<T> {
    private String message;
    private String status;
    private T data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public static <T> GlobalApiResponse success(T data)
    {
        GlobalApiResponse<T> response = new GlobalApiResponse<>();
        response.setStatus("success");
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    public static <T> GlobalApiResponse<T> error(T data, String message){
        GlobalApiResponse<T> response = new GlobalApiResponse<>();
        response.setStatus("Failure");
        response.setData(data);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
}
