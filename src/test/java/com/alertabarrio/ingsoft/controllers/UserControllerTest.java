package com.alertabarrio.ingsoft.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.UserResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UserSaveDTO;
import com.alertabarrio.ingsoft.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateUserSuccessfully() throws Exception {

        UserSaveDTO dto = new UserSaveDTO(
                "Juan Perez",
                "juan@test.com",
                "+573001112233",
                "Calle 1",
                1L
        );

        UserResponseDTO response = new UserResponseDTO(
                10L,
                "Juan Perez",
                "juan@test.com",
                "+573001112233",
                "Calle 1",
                1L
        );

        when(userService.save(any(UserSaveDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isCreated());

    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {

        String invalidJson = """
            {
                "name":"Juan Perez",
                "email":"correo-invalido",
                "phone":"+573001112233",
                "address":"Calle 1",
                "barrioId":1
            }
            """;

        mockMvc.perform(
                post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
        )
        .andExpect(status().isBadRequest());

    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {

        String invalidJson = """
            {
                "name":"",
                "email":"juan@test.com",
                "phone":"+573001112233",
                "address":"Calle 1",
                "barrioId":1
            }
            """;

        mockMvc.perform(
                post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
        )
        .andExpect(status().isBadRequest());

    }

    @Test
    void shouldReturnBadRequestWhenPhoneIsInvalid() throws Exception {

        String invalidJson = """
            {
                "name":"Juan Perez",
                "email":"juan@test.com",
                "phone":"ABC123",
                "address":"Calle 1",
                "barrioId":1
            }
            """;

        mockMvc.perform(
                post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
        )
        .andExpect(status().isBadRequest());

    }

    @Test
    void shouldGetUserByIdSuccessfully() throws Exception {

        UserResponseDTO response = new UserResponseDTO(
                1L,
                "Juan Perez",
                "juan@test.com",
                "+573001112233",
                "Calle 1",
                1L
        );

        when(userService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/usuarios/1")
        )
        .andExpect(status().isOk());

    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

        when(userService.findById(999L))
                .thenThrow(
                        new ResourceNotFoundException("User", 999L)
                );

        mockMvc.perform(
                get("/api/usuarios/999")
        )
        .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {

        doNothing().when(userService).delete(1L);

        mockMvc.perform(
                delete("/api/usuarios/1")
        )
        .andExpect(status().isNoContent());

    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingUser() throws Exception {

        doThrow(new ResourceNotFoundException("User", 999L))
                .when(userService)
                .delete(999L);

        mockMvc.perform(
                delete("/api/usuarios/999")
        )
        .andExpect(status().isNotFound());

    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {

        UserSaveDTO dto = new UserSaveDTO(
                "Juan Actualizado",
                "juan@test.com",
                "+573001112233",
                "Nueva direccion",
                1L
        );

        UserResponseDTO response = new UserResponseDTO(
                1L,
                "Juan Actualizado",
                "juan@test.com",
                "+573001112233",
                "Nueva direccion",
                1L
        );

        when(userService.update(eq(1L), any(UserSaveDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isOk());

    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingUser() throws Exception {

        UserSaveDTO dto = new UserSaveDTO(
                "Juan",
                "juan@test.com",
                "+573001112233",
                "Calle 1",
                1L
        );

        when(userService.update(eq(999L), any(UserSaveDTO.class)))
                .thenThrow(new ResourceNotFoundException("User", 999L));

        mockMvc.perform(
                put("/api/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isNotFound());

    }

    @Test
    void shouldPatchUserSuccessfully() throws Exception {

        UserSaveDTO dto = new UserSaveDTO(
                "Juan Actualizado",
                null,
                null,
                null,
                null
        );

        UserResponseDTO response = new UserResponseDTO(
                1L,
                "Juan Actualizado",
                "juan@test.com",
                "+573001112233",
                "Calle 1",
                1L
        );

        when(userService.patch(eq(1L), any(UserSaveDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                patch("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isOk());

    }

    @Test
    void shouldReturnNotFoundWhenPatchingNonExistingUser() throws Exception {

        UserSaveDTO dto = new UserSaveDTO(
                "Juan Actualizado",
                null,
                null,
                null,
                null
        );

        when(userService.patch(eq(999L), any(UserSaveDTO.class)))
                .thenThrow(new ResourceNotFoundException("User", 999L));

        mockMvc.perform(
                patch("/api/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isNotFound());

    }

    @Test
    void shouldGetUsersPageSuccessfully() throws Exception {

        UserResponseDTO user = new UserResponseDTO(
                1L,
                "Juan Perez",
                "juan@test.com",
                "+573001112233",
                "Calle 1",
                1L
        );

        Page<UserResponseDTO> page =
                new PageImpl<>(List.of(user));

        when(userService.findAllPaginated(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                get("/api/usuarios")
        )
        .andExpect(status().isOk());

    }

    @Test
    void shouldReturnEmptyPageWhenNoUsersExist() throws Exception {

        Page<UserResponseDTO> page =
                new PageImpl<>(List.of());

        when(userService.findAllPaginated(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                get("/api/usuarios")
        )
        .andExpect(status().isOk());

    }

}