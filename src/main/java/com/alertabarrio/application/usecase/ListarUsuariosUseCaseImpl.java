package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.application.query.ListarUsuariosQuery;
import com.alertabarrio.domain.port.in.ListarUsuariosUseCase;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarUsuariosUseCaseImpl implements ListarUsuariosUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final UsuarioDomainMapper mapper;

    public ListarUsuariosUseCaseImpl(UsuarioRepositoryPort usuarioRepository, UsuarioDomainMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<UsuarioDTO> execute(ListarUsuariosQuery query) {
        return usuarioRepository.findAll(query.pageable())
                .map(mapper::toDto);
    }
}
