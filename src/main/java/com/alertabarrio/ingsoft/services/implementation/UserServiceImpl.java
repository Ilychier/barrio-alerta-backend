package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.UserResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UserSaveDTO;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.models.entities.Barrio;
import org.mindrot.jbcrypt.BCrypt;
import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.repositories.BarrioRepository;
import com.alertabarrio.ingsoft.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BarrioRepository barrioRepository;

    public UserServiceImpl(UserRepository userRepository, BarrioRepository barrioRepository) {
        this.userRepository = userRepository;
        this.barrioRepository = barrioRepository;
    }

    @Override
    public UserResponseDTO save(UserSaveDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new ResourceConflictException("A user with the email '" + dto.email() + "' already exists.");
        }

        if (dto.password() == null || dto.password().isBlank()) {
            throw new IllegalArgumentException("Password is required for registration.");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        user.setAddress(dto.address());
        user.setPassword(BCrypt.hashpw(dto.password(), BCrypt.gensalt()));
        if (dto.barrioId() != null) {
            Barrio barrio = barrioRepository.findById(dto.barrioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Barrio", dto.barrioId()));
            user.setBarrio(barrio);
        }

        return mapToDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return mapToDTO(user);
    }

    @Override
    public UserResponseDTO update(Long id, UserSaveDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new ResourceConflictException("A user with the email '" + dto.email() + "' already exists.");
        }

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        user.setAddress(dto.address());
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(BCrypt.hashpw(dto.password(), BCrypt.gensalt()));
        }
        if (dto.barrioId() != null) {
            Barrio barrio = barrioRepository.findById(dto.barrioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Barrio", dto.barrioId()));
            user.setBarrio(barrio);
        } else {
            user.setBarrio(null);
        }

        return mapToDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO patch(Long id, UserSaveDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        if (dto.name() != null) user.setName(dto.name());
        if (dto.email() != null) {
            if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
                throw new ResourceConflictException("A user with the email '" + dto.email() + "' already exists.");
            }
            user.setEmail(dto.email());
        }
        if (dto.phone() != null) user.setPhone(dto.phone());
        if (dto.address() != null) user.setAddress(dto.address());
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(BCrypt.hashpw(dto.password(), BCrypt.gensalt()));
        }
        if (dto.barrioId() != null) {
            Barrio barrio = barrioRepository.findById(dto.barrioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Barrio", dto.barrioId()));
            user.setBarrio(barrio);
        }

        return mapToDTO(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public Page<UserResponseDTO> findAllPaginated(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::mapToDTO);
    }

    private UserResponseDTO mapToDTO(User entity) {
        return new UserResponseDTO(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getAddress(),
            entity.getBarrio() != null ? entity.getBarrio().getId() : null
        );
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("A user with the email '" + email + "' was not found."));
        return mapToDTO(user);
    }
}
