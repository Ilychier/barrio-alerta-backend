package com.alertabarrio.application.command;

public record EnviarEmailCommand(String toEmail, String asunto, String contenido) {
}
