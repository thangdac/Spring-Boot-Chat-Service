package com.dt.chat_service.utils;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;

import com.dt.chat_service.entity.User;

public class SecurityUtils {

    // static method — dùng được ở bất kỳ đâu mà không cần inject
    public static UUID getCurrentUserId() {
        User user =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
