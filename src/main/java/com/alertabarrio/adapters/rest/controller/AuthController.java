package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.AuthResponseDTO;
import com.alertabarrio.adapters.rest.dto.LoginRequestDTO;
import com.alertabarrio.adapters.rest.dto.UsuarioRequestDTO;
import com.alertabarrio.adapters.rest.mapper.UsuarioDtoMapper;
import com.alertabarrio.application.command.LoginCommand;
import com.alertabarrio.domain.port.in.LoginUseCase;
import com.alertabarrio.domain.port.in.RegistrarYAutenticarUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegistrarYAutenticarUseCase registrarYAutenticarUseCase;
    private final UsuarioDtoMapper mapper;

    public AuthController(
            LoginUseCase loginUseCase,
            RegistrarYAutenticarUseCase registrarYAutenticarUseCase,
            UsuarioDtoMapper mapper) {
        this.loginUseCase = loginUseCase;
        this.registrarYAutenticarUseCase = registrarYAutenticarUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody UsuarioRequestDTO dto) {
        var result = registrarYAutenticarUseCase.execute(mapper.toCrearCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponseDTO(result.token(), mapper.toResponse(result.user())));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        var result = loginUseCase.execute(new LoginCommand(dto.email(), dto.password()));
        return ResponseEntity.ok(new AuthResponseDTO(result.token(), mapper.toResponse(result.user())));
    }
}
