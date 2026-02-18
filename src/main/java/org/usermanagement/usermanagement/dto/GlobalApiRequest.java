package org.usermanagement.usermanagement.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalApiRequest<T> {
    @Valid
    private T data;
}
