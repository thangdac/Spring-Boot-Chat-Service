package com.dt.chat_service.dto.response;

import java.util.Set;

import com.dt.chat_service.entity.Permission;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {

    String name;
    String description;
    Set<Permission> permissions;
}
