package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.application.query.ListarBarriosQuery;
import com.alertabarrio.domain.port.in.ListarBarriosUseCase;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarBarriosUseCaseImpl implements ListarBarriosUseCase {

    private final BarrioRepositoryPort barrioRepository;
    private final BarrioDomainMapper mapper;

    public ListarBarriosUseCaseImpl(BarrioRepositoryPort barrioRepository, BarrioDomainMapper mapper) {
        this.barrioRepository = barrioRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<BarrioDTO> execute(ListarBarriosQuery query) {
        return barrioRepository.findAll(query.pageable())
                .map(mapper::toDto);
    }
}
