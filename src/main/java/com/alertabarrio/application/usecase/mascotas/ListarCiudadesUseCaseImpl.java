package com.alertabarrio.application.usecase.mascotas;

import com.alertabarrio.application.dto.mascotas.CiudadDTO;
import com.alertabarrio.application.mapper.mascotas.CiudadDomainMapper;
import com.alertabarrio.domain.model.mascotas.Ciudad;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.in.mascotas.ListarCiudadesUseCase;
import com.alertabarrio.domain.port.out.mascotas.CiudadRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListarCiudadesUseCaseImpl implements ListarCiudadesUseCase {

    private final CiudadRepositoryPort ciudadRepository;
    private final CiudadDomainMapper mapper;

    public ListarCiudadesUseCaseImpl(CiudadRepositoryPort ciudadRepository, CiudadDomainMapper mapper) {
        this.ciudadRepository = ciudadRepository;
        this.mapper = mapper;
    }

    @Override
    public Pagina<CiudadDTO> execute(Paginacion paginacion) {
        Pagina<Ciudad> pagina = ciudadRepository.findAll(paginacion);
        return new Pagina<>(
                pagina.contenido().stream().map(mapper::toDto).toList(),
                pagina.pagina(),
                pagina.tamanio(),
                pagina.totalElementos(),
                pagina.totalPaginas()
        );
    }
}
