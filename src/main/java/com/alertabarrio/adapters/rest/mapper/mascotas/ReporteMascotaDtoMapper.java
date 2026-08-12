package com.alertabarrio.adapters.rest.mapper.mascotas;

import com.alertabarrio.adapters.rest.dto.mascotas.*;
import com.alertabarrio.application.command.mascotas.*;
import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.application.query.mascotas.BuscarReporteMascotaQuery;
import com.alertabarrio.application.query.mascotas.ListarReportesMascotaQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReporteMascotaDtoMapper {

    CrearReporteMascotaCommand toCrearCommand(CrearReporteMascotaRequestDTO dto);

    ActualizarReporteMascotaCommand toActualizarCommand(Long id, ActualizarReporteMascotaRequestDTO dto);

    CambiarEstadoReporteMascotaCommand toCambiarEstadoCommand(Long id, CambiarEstadoReporteMascotaRequestDTO dto);

    EliminarReporteMascotaCommand toEliminarCommand(Long id);

    BuscarReporteMascotaQuery toBuscarQuery(Long id);

    default ListarReportesMascotaQuery toListarQuery(Paginacion paginacion, String estado, String tipoReporte, Long ciudadId) {
        return new ListarReportesMascotaQuery(paginacion, estado, tipoReporte, ciudadId);
    }

    ReporteMascotaResponseDTO toResponse(ReporteMascotaDTO dto);

    /**
     * Convierte a response público, ocultando el teléfono cuando el estado
     * es RESCUED (Ley 1581 de 2012 — reducción de exposición de datos personales).
     * <p>
     * Los nombres de catálogos (tipoMascota, ciudad) NO se resuelven aquí:
     * el frontend ya carga los catálogos completos (GET /public/ciudades,
     * GET /public/tipos-mascota) y hace el lookup en cliente. KISS: evita
     * cargar 1.156 ciudades por cada request del feed.
     */
    default ReporteMascotaPublicoResponseDTO toPublicoResponse(ReporteMascotaDTO dto) {
        boolean ocultarTelefono = "RESCUED".equals(dto.estado());
        return new ReporteMascotaPublicoResponseDTO(
                dto.id(),
                dto.tipoReporte(),
                dto.tipoMascotaId(),
                dto.ciudadId(),
                dto.ubicacion(),
                ocultarTelefono ? null : dto.telefono(),
                dto.descripcion(),
                dto.estado(),
                dto.createdAt()
        );
    }

    default Pagina<ReporteMascotaPublicoResponseDTO> toPublicoResponsePage(Pagina<ReporteMascotaDTO> pagina) {
        return new Pagina<>(
                pagina.contenido().stream().map(this::toPublicoResponse).toList(),
                pagina.pagina(), pagina.tamanio(), pagina.totalElementos(), pagina.totalPaginas());
    }
}
