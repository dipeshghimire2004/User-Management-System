package org.usermanagement.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateDto {

    private String firstName;

    private String lastName;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Phone number must be valid")
    private String phoneNumber;

}
