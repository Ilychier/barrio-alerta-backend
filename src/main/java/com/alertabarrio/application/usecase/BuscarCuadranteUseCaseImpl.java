package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.mapper.CuadranteDomainMapper;
import com.alertabarrio.application.query.BuscarCuadranteQuery;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.port.in.BuscarCuadranteUseCase;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BuscarCuadranteUseCaseImpl implements BuscarCuadranteUseCase {

    private final CuadranteRepositoryPort cuadranteRepository;
    private final CuadranteDomainMapper mapper;

    public BuscarCuadranteUseCaseImpl(CuadranteRepositoryPort cuadranteRepository, CuadranteDomainMapper mapper) {
        this.cuadranteRepository = cuadranteRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CuadranteDTO> execute(BuscarCuadranteQuery query) {
        return cuadranteRepository.findById(new CuadranteId(query.id()))
                .map(mapper::toDto);
    }
}
