package com.alertabarrio.application.dto;

import java.time.LocalDateTime;

public record AlertaDTO(Long id, String descripcion, boolean esSos, LocalDateTime fechaHora, Long usuarioId, Long categoriaId) {
}
