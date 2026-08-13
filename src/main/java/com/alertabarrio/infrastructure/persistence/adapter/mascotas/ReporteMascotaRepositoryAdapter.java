package com.alertabarrio.infrastructure.persistence.adapter.mascotas;

import com.alertabarrio.domain.model.mascotas.EstadoReporte;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.mascotas.TipoReporte;
import com.alertabarrio.domain.model.mascotas.valueobject.ReporteMascotaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.ReporteMascotaEntity;
import com.alertabarrio.infrastructure.persistence.mapper.mascotas.ReporteMascotaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.mascotas.ReporteMascotaJpaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReporteMascotaRepositoryAdapter implements ReporteMascotaRepositoryPort {

    private static final String ESTADO_DELETED = "DELETED";

    private final ReporteMascotaJpaRepository jpaRepository;
    private final ReporteMascotaEntityMapper mapper;

    public ReporteMascotaRepositoryAdapter(ReporteMascotaJpaRepository jpaRepository,
                                           ReporteMascotaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ReporteMascota save(ReporteMascota reporte) {
        ReporteMascotaEntity entity = mapper.toEntity(reporte);
        if (reporte.getId() != null) {
            entity.setId(reporte.getId().value());
        }
        ReporteMascotaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ReporteMascota> findById(ReporteMascotaId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(ReporteMascotaId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(ReporteMascotaId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public Pagina<ReporteMascota> findByFilters(EstadoReporte estado, TipoReporte tipoReporte,
                                                Long ciudadId, String busqueda, Paginacion paginacion) {
        Pageable pageable = MascotaPaginacionHelper.toPageable(paginacion);
        Page<ReporteMascotaEntity> page = jpaRepository.findAll(conFiltros(estado, tipoReporte, ciudadId, busqueda), pageable);
        return toPagina(page);
    }

    @Override
    public Pagina<ReporteMascota> findByEstado(EstadoReporte estado, Paginacion paginacion) {
        Pageable pageable = MascotaPaginacionHelper.toPageable(paginacion);
        Page<ReporteMascotaEntity> page = jpaRepository.findByEstado(estado.name(), pageable);
        return toPagina(page);
    }

    @Override
    public Pagina<ReporteMascota> findByUsuarioId(UsuarioId usuarioId, Paginacion paginacion) {
        Pageable pageable = MascotaPaginacionHelper.toPageable(paginacion);
        Page<ReporteMascotaEntity> page = jpaRepository.findByUsuario_Id(usuarioId.value(), pageable);
        return toPagina(page);
    }

    /**
     * Construye la Specification del feed público. Combina filtros con AND:
     * <ul>
     *   <li>Estado: igualdad (si es null, excluye DELETED — feed público)</li>
     *   <li>Tipo de reporte: igualdad</li>
     *   <li>Ciudad: igualdad sobre ciudad.id</li>
     *   <li>Búsqueda: cada token (palabra) debe coincidir en descripcion O ubicacion
     *       (case-insensitive, LIKE %token%). Los tokens se combinan con AND.</li>
     * </ul>
     * Cumple OCP: agregar un filtro nuevo es añadir un {@code if} aquí.
     */
    static Specification<ReporteMascotaEntity> conFiltros(EstadoReporte estado, TipoReporte tipoReporte,
                                                          Long ciudadId, String busqueda) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (estado != null) {
                predicates.add(cb.equal(root.get("estado"), estado.name()));
            } else {
                // Feed público: los reportes DELETED (soft delete) no se muestran
                predicates.add(cb.notEqual(root.get("estado"), ESTADO_DELETED));
            }
            if (tipoReporte != null) {
                predicates.add(cb.equal(root.get("tipoReporte"), tipoReporte.name()));
            }
            if (ciudadId != null) {
                predicates.add(cb.equal(root.get("ciudad").get("id"), ciudadId));
            }
            if (StringUtils.hasText(busqueda)) {
                for (String token : tokensDe(busqueda)) {
                    String pattern = "%" + token + "%";
                    Predicate descripcion = cb.like(cb.lower(root.get("descripcion")), pattern);
                    Predicate ubicacion = cb.like(cb.lower(root.get("ubicacion")), pattern);
                    predicates.add(cb.or(descripcion, ubicacion));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Tokeniza la búsqueda en palabras minúsculas y escapa caracteres
     * comodín de SQL LIKE (%, _) para que el usuario no pueda inyectarlos.
     */
    private static List<String> tokensDe(String busqueda) {
        List<String> tokens = new ArrayList<>();
        for (String palabra : busqueda.trim().toLowerCase().split("\\s+")) {
            tokens.add(palabra.replace("%", "\\%").replace("_", "\\_"));
        }
        return tokens;
    }

    private Pagina<ReporteMascota> toPagina(Page<ReporteMascotaEntity> page) {
        return new Pagina<>(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
