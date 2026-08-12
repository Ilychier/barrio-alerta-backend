package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.CiudadInfo;
import com.alertabarrio.domain.model.valueobject.CiudadId;

import java.util.Optional;

public interface CiudadInfoPort {
    Optional<CiudadInfo> findById(CiudadId id);
}
