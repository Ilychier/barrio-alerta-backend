package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CategoriaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Categoria;
import com.alertabarrio.ingsoft.repositories.CategoriaRepository;
import com.alertabarrio.ingsoft.services.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public CategoriaResponseDTO save(CategoriaSaveDTO dto) {
        if (categoriaRepository.existsByNombre(dto.nombre())) {
            throw new ResourceConflictException("A category with the name '" + dto.nombre() + "' already exists.");
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(dto.nombre());
        categoria.setIconoReferencia(dto.iconoReferencia());

        return mapToDTO(categoriaRepository.save(categoria));
    }

    @Override
    public CategoriaResponseDTO findById(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return mapToDTO(categoria);
    }

    @Override
    public CategoriaResponseDTO update(Long id, CategoriaSaveDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        if (!categoria.getNombre().equals(dto.nombre()) && categoriaRepository.existsByNombre(dto.nombre())) {
            throw new ResourceConflictException("A category with the name '" + dto.nombre() + "' already exists.");
        }

        categoria.setNombre(dto.nombre());
        categoria.setIconoReferencia(dto.iconoReferencia());

        return mapToDTO(categoriaRepository.save(categoria));
    }

    @Override
    public CategoriaResponseDTO patch(Long id, CategoriaSaveDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        if (dto.nombre() != null) {
            if (!categoria.getNombre().equals(dto.nombre()) && categoriaRepository.existsByNombre(dto.nombre())) {
                throw new ResourceConflictException("A category with the name '" + dto.nombre() + "' already exists.");
            }
            categoria.setNombre(dto.nombre());
        }
        if (dto.iconoReferencia() != null) categoria.setIconoReferencia(dto.iconoReferencia());

        return mapToDTO(categoriaRepository.save(categoria));
    }

    @Override
    public void delete(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria", id);
        }
        categoriaRepository.deleteById(id);
    }

    @Override
    public Page<CategoriaResponseDTO> findAllPaginated(Pageable pageable) {
        return categoriaRepository.findAll(pageable).map(this::mapToDTO);
    }

    private CategoriaResponseDTO mapToDTO(Categoria entity) {
        return new CategoriaResponseDTO(
            entity.getId(),
            entity.getNombre(),
            entity.getIconoReferencia()
        );
    }
}