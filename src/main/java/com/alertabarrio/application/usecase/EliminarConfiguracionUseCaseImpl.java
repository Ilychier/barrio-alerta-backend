package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.EliminarConfiguracionCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.ConfiguracionId;
import com.alertabarrio.domain.port.in.EliminarConfiguracionUseCase;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;

@UseCase
public class EliminarConfiguracionUseCaseImpl implements EliminarConfiguracionUseCase {

    private final ConfiguracionRepositoryPort configuracionRepository;

    public EliminarConfiguracionUseCaseImpl(ConfiguracionRepositoryPort configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    @Override
    public void execute(EliminarConfiguracionCommand command) {
        ConfiguracionId id = new ConfiguracionId(command.id());
        if (!configuracionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Configuracion", command.id());
        }
        configuracionRepository.deleteById(id);
    }
}
