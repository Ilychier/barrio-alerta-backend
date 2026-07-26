package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.mapper.CuadranteDomainMapper;
import com.alertabarrio.application.query.ListarCuadrantesQuery;
import com.alertabarrio.domain.port.in.ListarCuadrantesUseCase;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarCuadrantesUseCaseImpl implements ListarCuadrantesUseCase {

    private final CuadranteRepositoryPort cuadranteRepository;
    private final CuadranteDomainMapper mapper;

    public ListarCuadrantesUseCaseImpl(CuadranteRepositoryPort cuadranteRepository, CuadranteDomainMapper mapper) {
        this.cuadranteRepository = cuadranteRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<CuadranteDTO> execute(ListarCuadrantesQuery query) {
        return cuadranteRepository.findAll(query.pageable())
                .map(mapper::toDto);
    }
}
