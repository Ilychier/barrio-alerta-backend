package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.UsuarioRequestDTO;
import com.alertabarrio.adapters.rest.dto.UsuarioResponseDTO;
import com.alertabarrio.adapters.rest.mapper.UsuarioDtoMapper;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.query.BuscarUsuarioPorEmailQuery;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.in.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final ActualizarUsuarioUseCase actualizarUsuarioUseCase;
    private final ParchearUsuarioUseCase parchearUsuarioUseCase;
    private final EliminarUsuarioUseCase eliminarUsuarioUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;
    private final UsuarioDtoMapper mapper;

    public UsuarioController(
            RegistrarUsuarioUseCase registrarUsuarioUseCase,
            ActualizarUsuarioUseCase actualizarUsuarioUseCase,
            ParchearUsuarioUseCase parchearUsuarioUseCase,
            EliminarUsuarioUseCase eliminarUsuarioUseCase,
            BuscarUsuarioUseCase buscarUsuarioUseCase,
            ListarUsuariosUseCase listarUsuariosUseCase,
            BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase,
            UsuarioDtoMapper mapper) {
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
        this.actualizarUsuarioUseCase = actualizarUsuarioUseCase;
        this.parchearUsuarioUseCase = parchearUsuarioUseCase;
        this.eliminarUsuarioUseCase = eliminarUsuarioUseCase;
        this.buscarUsuarioUseCase = buscarUsuarioUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.buscarUsuarioPorEmailUseCase = buscarUsuarioPorEmailUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioDTO result = registrarUsuarioUseCase.execute(mapper.toCrearCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> findMe(HttpServletRequest request) {
        String email = (String) request.getAttribute("currentUserEmail");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UsuarioDTO result = buscarUsuarioPorEmailUseCase.execute(new BuscarUsuarioPorEmailQuery(email));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id) {
        UsuarioDTO result = buscarUsuarioUseCase.execute(mapper.toBuscarQuery(id));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioDTO result = actualizarUsuarioUseCase.execute(mapper.toActualizarCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> patch(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        UsuarioDTO result = parchearUsuarioUseCase.execute(mapper.toParchearCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eliminarUsuarioUseCase.execute(mapper.toEliminarCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Pagina<UsuarioResponseDTO>> findAllPaginated(
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        String orden = pageable.getSort().stream().findFirst()
                .map(Sort.Order::getProperty).orElse(null);
        String direccion = pageable.getSort().stream().findFirst()
                .map(o -> o.getDirection().name().toLowerCase()).orElse(null);
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(), orden, direccion);
        Pagina<UsuarioDTO> result = listarUsuariosUseCase.execute(mapper.toListarQuery(paginacion));
        return ResponseEntity.ok(new Pagina<>(
                result.contenido().stream().map(mapper::toResponse).toList(),
                result.pagina(), result.tamanio(), result.totalElementos(), result.totalPaginas()));
    }
}
