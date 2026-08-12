package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.*;
import com.alertabarrio.application.mapper.*;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.model.valueobject.CiudadId;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.model.valueobject.LocalidadId;
import com.alertabarrio.domain.port.in.ObtenerSesionBundleUseCase;
import com.alertabarrio.domain.port.out.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ObtenerSesionBundleUseCaseImpl implements ObtenerSesionBundleUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final BarrioRepositoryPort barrioRepository;
    private final CuadranteRepositoryPort cuadranteRepository;
    private final ConfiguracionRepositoryPort configuracionRepository;
    private final CiudadInfoPort ciudadInfoPort;
    private final LocalidadRepositoryPort localidadRepository;
    private final UsuarioDomainMapper usuarioMapper;
    private final BarrioDomainMapper barrioMapper;
    private final CuadranteDomainMapper cuadranteMapper;
    private final ConfiguracionDomainMapper configuracionMapper;

    public ObtenerSesionBundleUseCaseImpl(
            UsuarioRepositoryPort usuarioRepository,
            BarrioRepositoryPort barrioRepository,
            CuadranteRepositoryPort cuadranteRepository,
            ConfiguracionRepositoryPort configuracionRepository,
            CiudadInfoPort ciudadInfoPort,
            LocalidadRepositoryPort localidadRepository,
            UsuarioDomainMapper usuarioMapper,
            BarrioDomainMapper barrioMapper,
            CuadranteDomainMapper cuadranteMapper,
            ConfiguracionDomainMapper configuracionMapper) {
        this.usuarioRepository = usuarioRepository;
        this.barrioRepository = barrioRepository;
        this.cuadranteRepository = cuadranteRepository;
        this.configuracionRepository = configuracionRepository;
        this.ciudadInfoPort = ciudadInfoPort;
        this.localidadRepository = localidadRepository;
        this.usuarioMapper = usuarioMapper;
        this.barrioMapper = barrioMapper;
        this.cuadranteMapper = cuadranteMapper;
        this.configuracionMapper = configuracionMapper;
    }

    @Override
    public SesionDTO execute(String email) {
        var user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", email));
        UsuarioDTO userDto = usuarioMapper.toDto(user);

        BarrioDTO barrioDto = null;
        CuadranteDTO cuadranteDto = null;
        String ciudadNombre = null;
        String paisNombre = null;
        if (user.getBarrioId() != null) {
            barrioDto = barrioRepository.findById(new BarrioId(user.getBarrioId().value()))
                    .map(barrioMapper::toDto)
                    .orElse(null);
            if (barrioDto != null && barrioDto.cuadranteId() != null) {
                cuadranteDto = cuadranteRepository.findById(new CuadranteId(barrioDto.cuadranteId()))
                        .map(cuadranteMapper::toDto)
                        .orElse(null);
            }
            // Cadena geográfica: barrio -> localidad -> municipio (ciudad) -> nombre/pais
            if (barrioDto != null && barrioDto.localidadId() != null) {
                var localidad = localidadRepository.findById(new LocalidadId(barrioDto.localidadId())).orElse(null);
                if (localidad != null) {
                    var ciudad = ciudadInfoPort.findById(new CiudadId(localidad.getMunicipioId())).orElse(null);
                    if (ciudad != null) {
                        ciudadNombre = ciudad.nombre();
                        paisNombre = ciudad.pais();
                    }
                }
            }
        }

        ConfiguracionDTO configDto = configuracionRepository.findByUsuarioId(userDto.id())
                .map(configuracionMapper::toDto)
                .orElse(null);

        return new SesionDTO(null, userDto, barrioDto, cuadranteDto, configDto, ciudadNombre, paisNombre);
    }
}
