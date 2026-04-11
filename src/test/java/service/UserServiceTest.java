package service;

import com.dt.chat_service.dto.response.UserResponse;
import com.dt.chat_service.entity.User;
import com.dt.chat_service.mapper.UserMapper;
import com.dt.chat_service.repository.UserRepository;
import com.dt.chat_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllUsers() {
        //given
        User user = new User();
        UserResponse userResponse = new UserResponse();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(userResponse, result.getFirst());

        verify(userRepository).findAll();
        verify(userMapper).toUserResponse(user);
    }

}
