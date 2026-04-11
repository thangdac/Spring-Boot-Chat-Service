package com.dt.chat_service.dto.response;

import com.dt.chat_service.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserStatusResponse {

    private String userId;
    private UserStatus status;

}
