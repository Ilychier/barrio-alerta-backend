package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.dto.mascotas.TipoMascotaDTO;
import com.alertabarrio.application.mapper.mascotas.TipoMascotaDomainMapper;
import com.alertabarrio.domain.port.in.mascotas.ListarTiposMascotaUseCase;
import com.alertabarrio.domain.port.out.mascotas.TipoMascotaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListarTiposMascotaUseCaseImpl implements ListarTiposMascotaUseCase {

    private final TipoMascotaRepositoryPort tipoMascotaRepository;
    private final TipoMascotaDomainMapper mapper;

    public ListarTiposMascotaUseCaseImpl(TipoMascotaRepositoryPort tipoMascotaRepository,
                                         TipoMascotaDomainMapper mapper) {
        this.tipoMascotaRepository = tipoMascotaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TipoMascotaDTO> execute() {
        return tipoMascotaRepository.findAllActivos().stream()
                .map(mapper::toDto)
                .toList();
    }
}
