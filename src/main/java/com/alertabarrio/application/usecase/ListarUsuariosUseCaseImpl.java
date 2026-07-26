package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.application.query.ListarUsuariosQuery;
import com.alertabarrio.domain.port.in.ListarUsuariosUseCase;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
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
    public Pagina<UsuarioDTO> execute(ListarUsuariosQuery query) {
        Pagina<com.alertabarrio.domain.model.User> usuariosPage = usuarioRepository.findAll(query.paginacion());
        return new Pagina<>(
                usuariosPage.contenido().stream().map(mapper::toDto).toList(),
                usuariosPage.pagina(),
                usuariosPage.tamanio(),
                usuariosPage.totalElementos(),
                usuariosPage.totalPaginas()
        );
    }
}
