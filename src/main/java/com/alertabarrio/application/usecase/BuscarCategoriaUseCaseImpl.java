package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.application.mapper.CategoriaDomainMapper;
import com.alertabarrio.application.query.BuscarCategoriaQuery;
import com.alertabarrio.domain.model.valueobject.CategoriaId;
import com.alertabarrio.domain.port.in.BuscarCategoriaUseCase;
import com.alertabarrio.domain.port.out.CategoriaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BuscarCategoriaUseCaseImpl implements BuscarCategoriaUseCase {

    private final CategoriaRepositoryPort categoriaRepository;
    private final CategoriaDomainMapper mapper;

    public BuscarCategoriaUseCaseImpl(CategoriaRepositoryPort categoriaRepository, CategoriaDomainMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CategoriaDTO> execute(BuscarCategoriaQuery query) {
        return categoriaRepository.findById(new CategoriaId(query.id()))
                .map(mapper::toDto);
    }
}
