package com.alertabarrio.ingsoft.controllers;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alertabarrio.ingsoft.exceptions.BadCredentialsException;
import com.alertabarrio.ingsoft.models.dtos.AuthResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.LoginRequestDTO;
import com.alertabarrio.ingsoft.models.dtos.UserResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UserSaveDTO;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.services.JwtService;
import com.alertabarrio.ingsoft.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthController(UserService userService, UserRepository userRepository, JwtService jwtService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody UserSaveDTO userSaveDTO) {
        UserResponseDTO savedUser = userService.save(userSaveDTO);
        String token = jwtService.generateToken(savedUser.email());
        return new ResponseEntity<>(new AuthResponseDTO(token, savedUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

        if (!BCrypt.checkpw(loginRequestDTO.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getAddress(),
            user.getBarrio() != null ? user.getBarrio().getId() : null
        );

        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponseDTO(token, userResponseDTO));
    }
}
