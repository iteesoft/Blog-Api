package com.its.blogapi;

import com.its.blogapi.dto.UserDto;
import com.its.blogapi.model.BlogUser;
import com.its.blogapi.repository.BlogUserRepository;
import com.its.blogapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private BlogUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldSavedUserSuccessFully() {
        BlogUser user = new BlogUser();
        UserDto loginUser = new UserDto("smith", "adejo", "smith@mail.com","1234");

        given(userRepository.findByEmail(user.getEmail())).willReturn(null);
        given(userRepository.save(user)).willReturn(user);
        when(userRepository.save(user)).then(invocation -> invocation.getArgument(0));
        ResponseEntity<?> savedUser = userService.createUser(loginUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        verify(userRepository).save(any(BlogUser.class));

    }
}
