package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.mapper.ConfiguracionDomainMapper;
import com.alertabarrio.application.query.ListarConfiguracionesQuery;
import com.alertabarrio.domain.model.Configuracion;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarConfiguracionesUseCaseImpl")
class ListarConfiguracionesUseCaseImplTest {

    @Mock
    private ConfiguracionRepositoryPort configuracionRepository;

    @Mock
    private ConfiguracionDomainMapper mapper;

    @InjectMocks
    private ListarConfiguracionesUseCaseImpl useCase;

    @Test
    @DisplayName("devuelve página de configuraciones")
    void execute_devuelvePagina() {
        Pageable pageable = Pageable.ofSize(10);
        ListarConfiguracionesQuery query = new ListarConfiguracionesQuery(pageable);

        Configuracion config1 = Configuracion.reconstruir(1L, 1L, true, false);
        Configuracion config2 = Configuracion.reconstruir(2L, 2L, false, true);
        Page<Configuracion> page = new PageImpl<>(List.of(config1, config2));

        ConfiguracionDTO dto1 = new ConfiguracionDTO(1L, 1L, true, false);
        ConfiguracionDTO dto2 = new ConfiguracionDTO(2L, 2L, false, true);

        when(configuracionRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(config1)).thenReturn(dto1);
        when(mapper.toDto(config2)).thenReturn(dto2);

        Page<ConfiguracionDTO> resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        assertEquals(1L, resultado.getContent().get(0).id());
        assertEquals(2L, resultado.getContent().get(1).id());

        verify(configuracionRepository).findAll(pageable);
        verify(mapper).toDto(config1);
        verify(mapper).toDto(config2);
    }
}
