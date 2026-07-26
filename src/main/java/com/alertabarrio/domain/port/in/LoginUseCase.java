package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.LoginCommand;
import com.alertabarrio.application.dto.AuthResultDTO;

public interface LoginUseCase {
    AuthResultDTO execute(LoginCommand command);
}
