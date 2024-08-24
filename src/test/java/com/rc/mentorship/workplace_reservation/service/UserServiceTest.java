package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.request.UserCreateRequest;
import com.rc.mentorship.workplace_reservation.dto.request.UserUpdateRequest;
import com.rc.mentorship.workplace_reservation.dto.response.UserResponse;
import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.exception.NotFoundException;
import com.rc.mentorship.workplace_reservation.mapper.UserMapper;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import com.rc.mentorship.workplace_reservation.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private final UUID mockId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private User user;
    private UserResponse response;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId(mockId);

        response = new UserResponse();
        response.setId(mockId);
    }

    @Test
    void findAllByRole_NoRoleFilter_ReturningPageOf3() {
        PageRequest pageRequest = mock(PageRequest.class);
        Page<User> userPage = new PageImpl<>(List.of(new User(), new User(), new User()));
        when(userRepository.findAllByRoleIfPresent(null, pageRequest)).thenReturn(userPage);

        Page<UserResponse> result = userService.findAll(pageRequest, null);

        assertThat(result).hasSize(3);
    }

    @Test
    void findById_HasUserById_ReturningUser() {
        when(userRepository.findById(mockId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(response);

        UserResponse result = userService.findById(mockId);

        assertThat(result).isNotNull().isEqualTo(response);
    }

    @Test
    void findById_NoUserById_ThrowingNotFound() {
        when(userRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(mockId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_SimpleValues_ReturningCreatedUser() {
        UserCreateRequest request = new UserCreateRequest();
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(response);

        UserResponse result = userService.create(request);

        assertThat(result).isNotNull().isEqualTo(response);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void update_SimpleValues_ReturningUpdatedUser() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setId(mockId);
        when(userRepository.findById(mockId)).thenReturn(Optional.of(mock(User.class)));
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(response);

        UserResponse result = userService.update(request);

        assertThat(result).isNotNull().isEqualTo(response);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void update_NoUserToUpdate_ThrowingNotFound() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setId(mockId);
        when(userRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(request))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_SimpleValues_Deleted() {
        userService.delete(mockId);

        verify(userRepository, only()).deleteById(mockId);
    }
}
