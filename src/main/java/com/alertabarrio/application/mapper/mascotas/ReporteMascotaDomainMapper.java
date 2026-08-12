package com.alertabarrio.application.mapper.mascotas;

import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReporteMascotaDomainMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "tipoReporte", source = "tipoReporte")
    @Mapping(target = "tipoMascotaId", source = "tipoMascotaId.value")
    @Mapping(target = "ciudadId", source = "ciudadId.value")
    @Mapping(target = "telefono", source = "telefono.value")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "usuarioId", source = "usuarioId.value")
    ReporteMascotaDTO toDto(ReporteMascota reporte);
}
