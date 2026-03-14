package com.dt.chat_service.entity;

import com.dt.chat_service.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)

    String id;
    String username;
    String email;
    LocalDate dob;
    String password;

    @Enumerated(EnumType.STRING)
    UserStatus status;

    // Relationships
    @ManyToMany
    Set<Role> roles;
}
