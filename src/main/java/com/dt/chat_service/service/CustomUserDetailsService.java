package com.dt.chat_service.service;

import com.dt.chat_service.exception.AppException;
import com.dt.chat_service.exception.ErrorCode;
import com.dt.chat_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public UserDetails loadUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

}
