package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.LoginRequestDTO;
import com.alertabarrio.adapters.rest.dto.SesionResponseDTO;
import com.alertabarrio.adapters.rest.dto.UsuarioRequestDTO;
import com.alertabarrio.adapters.rest.mapper.BarrioDtoMapper;
import com.alertabarrio.adapters.rest.mapper.ConfiguracionDtoMapper;
import com.alertabarrio.adapters.rest.mapper.CuadranteDtoMapper;
import com.alertabarrio.adapters.rest.mapper.UsuarioDtoMapper;
import com.alertabarrio.application.command.LoginCommand;
import com.alertabarrio.domain.port.in.LoginUseCase;
import com.alertabarrio.domain.port.in.ObtenerSesionBundleUseCase;
import com.alertabarrio.domain.port.in.RegistrarYAutenticarUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegistrarYAutenticarUseCase registrarYAutenticarUseCase;
    private final ObtenerSesionBundleUseCase obtenerSesionBundleUseCase;
    private final UsuarioDtoMapper usuarioMapper;
    private final BarrioDtoMapper barrioMapper;
    private final CuadranteDtoMapper cuadranteMapper;
    private final ConfiguracionDtoMapper configuracionMapper;

    public AuthController(
            LoginUseCase loginUseCase,
            RegistrarYAutenticarUseCase registrarYAutenticarUseCase,
            ObtenerSesionBundleUseCase obtenerSesionBundleUseCase,
            UsuarioDtoMapper usuarioMapper,
            BarrioDtoMapper barrioMapper,
            CuadranteDtoMapper cuadranteMapper,
            ConfiguracionDtoMapper configuracionMapper) {
        this.loginUseCase = loginUseCase;
        this.registrarYAutenticarUseCase = registrarYAutenticarUseCase;
        this.obtenerSesionBundleUseCase = obtenerSesionBundleUseCase;
        this.usuarioMapper = usuarioMapper;
        this.barrioMapper = barrioMapper;
        this.cuadranteMapper = cuadranteMapper;
        this.configuracionMapper = configuracionMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<SesionResponseDTO> register(@Valid @RequestBody UsuarioRequestDTO dto) {
        var authResult = registrarYAutenticarUseCase.execute(usuarioMapper.toCrearCommand(dto));
        var bundle = obtenerSesionBundleUseCase.execute(authResult.user().email());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(authResult.token(), bundle));
    }

    @PostMapping("/login")
    public ResponseEntity<SesionResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        var authResult = loginUseCase.execute(new LoginCommand(dto.email(), dto.password()));
        var bundle = obtenerSesionBundleUseCase.execute(authResult.user().email());
        return ResponseEntity.ok(toResponse(authResult.token(), bundle));
    }

    @GetMapping("/me")
    public ResponseEntity<SesionResponseDTO> me(HttpServletRequest request) {
        String email = (String) request.getAttribute("currentUserEmail");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var bundle = obtenerSesionBundleUseCase.execute(email);
        return ResponseEntity.ok(toResponse(null, bundle));
    }

    private SesionResponseDTO toResponse(String token, com.alertabarrio.application.dto.SesionDTO bundle) {
        return new SesionResponseDTO(
                token,
                bundle.user() != null ? usuarioMapper.toResponse(bundle.user()) : null,
                bundle.barrio() != null ? barrioMapper.toResponse(bundle.barrio()) : null,
                bundle.cuadrante() != null ? cuadranteMapper.toResponse(bundle.cuadrante()) : null,
                bundle.configuracion() != null ? configuracionMapper.toResponse(bundle.configuracion()) : null
        );
    }
}
