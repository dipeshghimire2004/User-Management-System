package org.usermanagement.usermanagement.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@SuperBuilder
@NoArgsConstructor
@SQLRestriction("is_active=true")
@SQLDelete(sql="UPDATE user_entity SET is_active = false WHERE id=?")
public class UserEntity extends BaseEntity {

    @NotBlank(message = "First name must not be blank.")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name must not be blank.")
    private String lastName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Phone number must be valid")
    private String phoneNumber;

}
