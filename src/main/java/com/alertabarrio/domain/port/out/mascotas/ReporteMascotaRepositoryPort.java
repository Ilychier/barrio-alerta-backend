package com.alertabarrio.domain.port.out.mascotas;

import com.alertabarrio.domain.model.mascotas.EstadoReporte;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.mascotas.TipoReporte;
import com.alertabarrio.domain.model.mascotas.valueobject.ReporteMascotaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.model.valueobject.UsuarioId;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de {@link ReporteMascota}.
 * <p>
 * Métodos específicos por caso de uso (P2): cada consulta del feed,
 * historias de rescate y mis reportes tiene su propio método. Esto
 * es más claro que un spec genérico para el volumen del MVP.
 */
public interface ReporteMascotaRepositoryPort {

    ReporteMascota save(ReporteMascota reporte);

    Optional<ReporteMascota> findById(ReporteMascotaId id);

    boolean existsById(ReporteMascotaId id);

    void deleteById(ReporteMascotaId id);

    /**
     * Feed público: filtra por estado, tipo de reporte y ciudad.
     * Cualquier filtro puede ser null para no filtrar.
     */
    Pagina<ReporteMascota> findByFilters(EstadoReporte estado, TipoReporte tipoReporte,
                                         Long ciudadId, Paginacion paginacion);

    /**
     * Historias de rescate: reportes con estado RESCUED.
     */
    Pagina<ReporteMascota> findByEstado(EstadoReporte estado, Paginacion paginacion);

    /**
     * Mis reportes: reportes de un usuario (incluye DELETED para que
     * el usuario vea sus propios reportes eliminados).
     */
    Pagina<ReporteMascota> findByUsuarioId(UsuarioId usuarioId, Paginacion paginacion);
}
