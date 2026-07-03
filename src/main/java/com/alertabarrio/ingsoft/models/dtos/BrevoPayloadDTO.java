package com.alertabarrio.ingsoft.models.dtos;

import java.util.List;

public record BrevoPayloadDTO(
    BrevoSenderDTO sender,
    List to,
    String subject,
    String htmlContent
) {}
