package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.CambiarPasswordCommand;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.port.in.CambiarPasswordUseCase;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;

/**
 * Cambia la contraseña de un usuario.
 * <p>
 * Comportamiento según el estado del usuario (Strategy implícito por estado):
 * - {@code passwordTemporal=true}: no exige la clave actual (el JWT ya
 *   autentica; el usuario nunca vio la temporal). Cambia y limpia el flag.
 * - {@code passwordTemporal=false}: verifica la clave actual antes de cambiar.
 */
@UseCase
public class CambiarPasswordUseCaseImpl implements CambiarPasswordUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final UsuarioDomainMapper mapper;

    public CambiarPasswordUseCaseImpl(UsuarioRepositoryPort usuarioRepository,
                                      PasswordEncoderPort passwordEncoder,
                                      UsuarioDomainMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public UsuarioDTO execute(CambiarPasswordCommand command) {
        var usuario = usuarioRepository.findByEmail(command.identificador())
                .or(() -> usuarioRepository.findByPhone(command.identificador()))
                .orElseThrow(() -> new ResourceNotFoundException("User", command.identificador()));

        if (!usuario.isPasswordTemporal()) {
            if (command.passwordActual() == null
                    || !passwordEncoder.verificar(command.passwordActual(), usuario.getPassword())) {
                throw new IllegalArgumentException("La contraseña actual es incorrecta");
            }
        }

        String nuevoHash = passwordEncoder.hashear(command.passwordNueva());
        User actualizado = usuarioRepository.save(usuario.cambiarPassword(nuevoHash));
        return mapper.toDto(actualizado);
    }
}
