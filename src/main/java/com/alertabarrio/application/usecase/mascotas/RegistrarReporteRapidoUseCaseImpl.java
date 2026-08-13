package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.mascotas.RegistrarReporteRapidoCommand;
import com.alertabarrio.application.dto.mascotas.ReporteRapidoResultDTO;
import com.alertabarrio.application.mapper.mascotas.ReporteMascotaDomainMapper;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.port.in.mascotas.RegistrarReporteRapidoUseCase;
import com.alertabarrio.domain.port.out.ClaveTemporalPort;
import com.alertabarrio.domain.port.out.EmailSinteticoPort;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.TokenServicePort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;

import java.time.Clock;

/**
 * Facade transaccional del registro rápido de emergencia (BC Mascotas).
 * <p>
 * Orquesta dos bounded contexts (Usuario + Mascotas) en una sola transacción
 * ({@code @UseCase} = {@code @Transactional(rollbackFor = Exception.class)}):
 * si el reporte falla, el usuario no queda huérfano.
 * <p>
 * Idempotencia por {@code phonePersonal}:
 * - No existe → crea usuario rápido (clave temporal autogenerada, email
 *   sintético) + reporte + JWT (auto-login).
 * - Existe y es temporal → reutiliza usuario + reporte + JWT (auto-login
 *   para que pueda cambiar su clave).
 * - Existe y no es temporal → reutiliza usuario + reporte, SIN JWT (debe
 *   loguear manualmente con su clave real).
 */
@UseCase
public class RegistrarReporteRapidoUseCaseImpl implements RegistrarReporteRapidoUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final ReporteMascotaRepositoryPort reporteRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenServicePort tokenService;
    private final EmailSinteticoPort emailSintetico;
    private final ClaveTemporalPort claveTemporal;
    private final ReporteMascotaDomainMapper mapper;
    private final Clock clock;

    public RegistrarReporteRapidoUseCaseImpl(
            UsuarioRepositoryPort usuarioRepository,
            ReporteMascotaRepositoryPort reporteRepository,
            PasswordEncoderPort passwordEncoder,
            TokenServicePort tokenService,
            EmailSinteticoPort emailSintetico,
            ClaveTemporalPort claveTemporal,
            ReporteMascotaDomainMapper mapper,
            Clock clock) {
        this.usuarioRepository = usuarioRepository;
        this.reporteRepository = reporteRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.emailSintetico = emailSintetico;
        this.claveTemporal = claveTemporal;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Override
    public ReporteRapidoResultDTO execute(RegistrarReporteRapidoCommand command) {
        User usuario = usuarioRepository.findByPhone(command.phonePersonal()).orElse(null);
        boolean esNuevo = false;
        boolean passwordTemporal;

        if (usuario == null) {
            String clave = claveTemporal.generar();
            String hash = passwordEncoder.hashear(clave);
            String emailSintetico = this.emailSintetico.generar(command.phonePersonal());
            usuario = usuarioRepository.save(User.crearRapido(command.phonePersonal(), hash, emailSintetico));
            esNuevo = true;
            passwordTemporal = true;
        } else {
            passwordTemporal = usuario.isPasswordTemporal();
        }

        ReporteMascota reporte = ReporteMascota.crear(
                command.tipoReporte(),
                command.tipoMascotaId(),
                command.ciudadId(),
                command.ubicacion(),
                command.telefonoContacto(),
                command.descripcion(),
                usuario.getId().value(),
                command.otroTipoMascota(),
                command.fotoUrl(),
                clock);
        ReporteMascota guardado = reporteRepository.save(reporte);

        // Auto-login solo si el usuario puede autenticarse sin clave conocida
        // (nuevo o temporal). Si tiene clave real, debe loguear manualmente.
        String token = passwordTemporal ? tokenService.generarToken(usuario.getEmail()) : null;

        return new ReporteRapidoResultDTO(mapper.toDto(guardado), token, passwordTemporal, esNuevo);
    }
}
