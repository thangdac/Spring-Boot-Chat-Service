package com.dt.chat_service.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {

    @NotBlank(message = "USERNAME_BLANK")
    @Size(min = 3, message = "USERNAME_INVALID")
    String name;

    @NotBlank(message = "DESCRIPTION_BLANK")
    String description;

    Set<String> permissions;
}
