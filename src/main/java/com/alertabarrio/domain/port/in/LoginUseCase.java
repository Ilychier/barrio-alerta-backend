package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.LoginCommand;

public interface LoginUseCase {
    String execute(LoginCommand command);
}
