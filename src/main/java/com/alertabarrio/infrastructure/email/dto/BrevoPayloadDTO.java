package com.alertabarrio.infrastructure.email.dto;

import java.util.List;

public record BrevoPayloadDTO(
    BrevoSenderDTO sender,
    List<BrevoToDTO> to,
    String subject,
    String htmlContent
) {}
