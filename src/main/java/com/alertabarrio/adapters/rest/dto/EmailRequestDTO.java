package com.alertabarrio.adapters.rest.dto;

public record EmailRequestDTO(
        String toEmail,
        String subject,
        String body
) {
}
