package com.alertabarrio.ingsoft.models.dtos;

public record EmailRequestDTO(
    String toEmail,
    String subject,
    String body
) {}
