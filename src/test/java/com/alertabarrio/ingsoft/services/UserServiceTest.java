package com.alertabarrio.ingsoft.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.models.dtos.UserSaveDTO;
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

}