package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.application.query.BuscarBarrioQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.port.in.BuscarBarrioUseCase;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BuscarBarrioUseCaseImpl implements BuscarBarrioUseCase {

    private final BarrioRepositoryPort barrioRepository;
    private final BarrioDomainMapper mapper;

    public BuscarBarrioUseCaseImpl(BarrioRepositoryPort barrioRepository, BarrioDomainMapper mapper) {
        this.barrioRepository = barrioRepository;
        this.mapper = mapper;
    }

    @Override
    public BarrioDTO execute(BuscarBarrioQuery query) {
        return barrioRepository.findById(new BarrioId(query.id()))
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Barrio", query.id()));
    }
}
