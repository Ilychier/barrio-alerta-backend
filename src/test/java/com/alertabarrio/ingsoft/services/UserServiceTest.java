package com.alertabarrio.ingsoft.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.UserResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UserSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Barrio;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.repositories.BarrioRepository;
import com.alertabarrio.ingsoft.services.implementation.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BarrioRepository barrioRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserSaveDTO dto = new UserSaveDTO(
            "Juan Perez",
            "juan@test.com",
            "+573001112233",
            "Calle 1",
            1L
        );

        when(userRepository.existsByEmail("juan@test.com"))
            .thenReturn(true);

        assertThrows(
            ResourceConflictException.class,
            () -> userService.save(dto)
        );
    }

    @Test
    void shouldSaveUserSuccessfully() {

        UserSaveDTO dto = new UserSaveDTO(
                "Juan Perez",
                "juan@test.com",
                "+573001112233",
                "Calle 1",
                1L
        );

        Barrio barrio = new Barrio();
        barrio.setId(1L);

        User savedUser = new User();
        savedUser.setId(10L);
        savedUser.setName(dto.name());
        savedUser.setEmail(dto.email());
        savedUser.setPhone(dto.phone());
        savedUser.setAddress(dto.address());
        savedUser.setBarrio(barrio);

        when(userRepository.existsByEmail(dto.email()))
                .thenReturn(false);

        when(barrioRepository.findById(1L))
                .thenReturn(Optional.of(barrio));

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        UserResponseDTO result = userService.save(dto);

        assertEquals(10L, result.id());
        assertEquals("Juan Perez", result.name());
        assertEquals("juan@test.com", result.email());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenBarrioDoesNotExist() {

        UserSaveDTO dto = new UserSaveDTO(
                "Juan Perez",
                "juan@test.com",
                "+573001112233",
                "Calle 1",
                999L
        );

        when(userRepository.existsByEmail(dto.email()))
                .thenReturn(false);

        when(barrioRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.save(dto)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldFindUserByIdSuccessfully() {

        Barrio barrio = new Barrio();
        barrio.setId(1L);

        User user = new User();
        user.setId(10L);
        user.setName("Juan Perez");
        user.setEmail("juan@test.com");
        user.setPhone("+573001112233");
        user.setAddress("Calle 1");
        user.setBarrio(barrio);

        when(userRepository.findById(10L))
                .thenReturn(Optional.of(user));

        UserResponseDTO result = userService.findById(10L);

        assertEquals(10L, result.id());
        assertEquals("Juan Perez", result.name());
        assertEquals("juan@test.com", result.email());
        assertEquals(1L, result.barrioId());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findById(999L)
        );

        ResourceNotFoundException exception =
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> userService.findById(999L)
            );

        assertEquals(
                "User with ID 999 not found in the database.",
                exception.getMessage()
        );

    }

    @Test
    void shouldDeleteUserSuccessfully() {

        when(userRepository.existsById(10L))
                .thenReturn(true);

        userService.delete(10L);

        verify(userRepository).deleteById(10L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {

        when(userRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.delete(999L)
        );

        verify(userRepository, never())
            .deleteById(anyLong());

    }

}