package com.alertabarrio.ingsoft.services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alertabarrio.ingsoft.models.dtos.UserResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UserSaveDTO;

public interface UserService {

    UserResponseDTO save(UserSaveDTO dto);

    UserResponseDTO findById(Long id);

    UserResponseDTO update(Long id, UserSaveDTO dto);

    UserResponseDTO patch(Long id, UserSaveDTO dto);

    void delete(Long id);

    Page<UserResponseDTO> findAllPaginated(Pageable pageable);  

    UserResponseDTO findByEmail(String email);
     
}
