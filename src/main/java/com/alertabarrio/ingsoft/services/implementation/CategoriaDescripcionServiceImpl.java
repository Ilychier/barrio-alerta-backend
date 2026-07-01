package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Categoria;
import com.alertabarrio.ingsoft.models.entities.CategoriaDescripcion;
import com.alertabarrio.ingsoft.repositories.CategoriaRepository;
import com.alertabarrio.ingsoft.repositories.CategoriaDescripcionRepository;
import com.alertabarrio.ingsoft.services.CategoriaDescripcionService;

@Service
public class CategoriaDescripcionServiceImpl implements CategoriaDescripcionService {

    private final CategoriaDescripcionRepository categoriaDescripcionRepository;
    private final CategoriaRepository categoriaRepository;

    public CategoriaDescripcionServiceImpl(
            CategoriaDescripcionRepository categoriaDescripcionRepository,
            CategoriaRepository categoriaRepository) {
        this.categoriaDescripcionRepository = categoriaDescripcionRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public CategoriaDescripcionResponseDTO save(CategoriaDescripcionSaveDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", dto.categoriaId()));

        CategoriaDescripcion cd = new CategoriaDescripcion();
        cd.setDescripcion(dto.descripcion());
        cd.setCategoria(categoria);

        return mapToDTO(categoriaDescripcionRepository.save(cd));
    }

    @Override
    public CategoriaDescripcionResponseDTO findById(Long id) {
        CategoriaDescripcion cd = categoriaDescripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CategoriaDescripcion", id));
        return mapToDTO(cd);
    }

    @Override
    public CategoriaDescripcionResponseDTO update(Long id, CategoriaDescripcionSaveDTO dto) {
        CategoriaDescripcion cd = categoriaDescripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CategoriaDescripcion", id));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", dto.categoriaId()));

        cd.setDescripcion(dto.descripcion());
        cd.setCategoria(categoria);

        return mapToDTO(categoriaDescripcionRepository.save(cd));
    }

    @Override
    public CategoriaDescripcionResponseDTO patch(Long id, CategoriaDescripcionSaveDTO dto) {
        CategoriaDescripcion cd = categoriaDescripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CategoriaDescripcion", id));

        if (dto.descripcion() != null) {
            cd.setDescripcion(dto.descripcion());
        }

        if (dto.categoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", dto.categoriaId()));
            cd.setCategoria(categoria);
        }

        return mapToDTO(categoriaDescripcionRepository.save(cd));
    }

    @Override
    public void delete(Long id) {
        if (!categoriaDescripcionRepository.existsById(id)) {
            throw new ResourceNotFoundException("CategoriaDescripcion", id);
        }
        categoriaDescripcionRepository.deleteById(id);
    }

    @Override
    public Page<CategoriaDescripcionResponseDTO> findAllPaginated(Long categoriaId, Pageable pageable) {
        if (categoriaId != null) {
            return categoriaDescripcionRepository.findByCategoriaId(categoriaId, pageable).map(this::mapToDTO);
        }
        return categoriaDescripcionRepository.findAll(pageable).map(this::mapToDTO);
    }

    private CategoriaDescripcionResponseDTO mapToDTO(CategoriaDescripcion entity) {
        return new CategoriaDescripcionResponseDTO(
            entity.getId(),
            entity.getDescripcion(),
            entity.getCategoria() != null ? entity.getCategoria().getId() : null
        );
    }
}
