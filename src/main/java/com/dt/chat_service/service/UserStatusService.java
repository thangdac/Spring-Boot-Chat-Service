package com.dt.chat_service.service;

import com.dt.chat_service.enums.UserStatus;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserStatusService {

    final Map<String, Integer> onlineUsers = new ConcurrentHashMap<>();

    public void connect(String userId) {
        onlineUsers.merge(userId, 1, Integer::sum);
    }

    public void disconnect(String userId) {
        onlineUsers.computeIfPresent(userId, (k, count) -> {
            int newCount = count - 1;
            return newCount <= 0 ? null : newCount; // null = xóa key
        });
    }

    public UserStatus getStatus(String userId) {
        return onlineUsers.containsKey(userId) ? UserStatus.ONLINE : UserStatus.OFFLINE;
    }

    public boolean isOnline(String userId) {
        return onlineUsers.containsKey(userId);
    }

    public Set<String> getOnlineUsers() {
        return Collections.unmodifiableSet(onlineUsers.keySet());
    }

}
