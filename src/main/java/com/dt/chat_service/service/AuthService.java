package com.dt.chat_service.service;

import com.dt.chat_service.Security.JwtTokenProvider;
import com.dt.chat_service.dto.request.LoginRequest;
import com.dt.chat_service.dto.request.UserCreationRequest;
import com.dt.chat_service.dto.response.IntrospectResponse;
import com.dt.chat_service.dto.response.TokenResponse;
import com.dt.chat_service.entity.RefreshToken;
import com.dt.chat_service.entity.User;
import com.dt.chat_service.enums.UserStatus;
import com.dt.chat_service.exception.AppException;
import com.dt.chat_service.exception.ErrorCode;
import com.dt.chat_service.mapper.UserMapper;
import com.dt.chat_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    UserRepository userRepository;
    RefreshTokenService refreshTokenService;
    TokenBlacklistService tokenBlacklistService;
    JwtTokenProvider jwtTokenProvider;
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;


    // login
    public TokenResponse login(LoginRequest request, String deviceInfo) {
        // Xác thực username/password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user, deviceInfo);

        return new TokenResponse(accessToken, refreshToken);
    }

    //register
    public void register(UserCreationRequest request) {

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        userMapper.toUserResponse(
                userRepository.save(user));
    }

    // Refresh access token
    public TokenResponse refresh(String refreshToken) {
        RefreshToken stored = refreshTokenService.validate(refreshToken);
        User user = stored.getUser();

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = refreshTokenService.createRefreshToken(
                user, stored.getDeviceInfo());

        // Xóa refresh token cũ
        refreshTokenService.revoke(refreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    // logout
    public void logout(String accessToken, String refreshToken) {
        // Blacklist access token
        String jti = jwtTokenProvider.getJti(accessToken);
        Instant expiration = jwtTokenProvider.getExpiration(accessToken);
        tokenBlacklistService.blacklistToken(jti, expiration);

        // Xóa refresh token
        refreshTokenService.revoke(refreshToken);
    }

    public IntrospectResponse introspect(String accessToken) {
        boolean valid = jwtTokenProvider.isValid(accessToken)
                && !tokenBlacklistService.isBlacklisted(
                jwtTokenProvider.getJti(accessToken));

        return new IntrospectResponse(valid);
    }

}
