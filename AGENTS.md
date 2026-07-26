# Barrio Alerta Backend — Technical Debt & Migration Notes

> Este archivo documenta deuda técnica, decisiones temporales y recordatorios
> para limpiar antes de producción. Se actualiza durante el refactor hexagonal.

---

## 1. 🔧 WebConfig.java — Auth Interceptor

**Archivo:** `src/main/java/com/alertabarrio/infrastructure/config/WebConfig.java`

**Estado:** ✅ Migrado en Fase 5. El WebConfig legacy fue eliminado. El nuevo vive en
`infrastructure/config/` y el `AuthInterceptor` en `infrastructure/security/`.

**Exclusiones actuales (públicas, sin auth):**
```
/api/auth/**
/api/email/send-email
/swagger-ui/**
/swagger-ui.html
/api-docs/**
```

**Nota:** Spring Security no está en el proyecto (no es dependencia). Se mantiene
el interceptor artesanal con `TokenServicePort` (puerto hexagonal) en lugar de
`SecurityFilterChain`. Documentar en ADR si se decide agregar Spring Security.

---

## 2. 🧪 Tests — @AutoConfigureMockMvc en Spring Boot 4.x

**Problema:** Spring Boot 4.x movió `@AutoConfigureMockMvc` de
`org.springframework.boot.test.autoconfigure.web.servlet` a
`org.springframework.boot.webmvc.test.autoconfigure`.

**Estado:** Los controller tests con MockMvc se eliminaron en Fase 5 (sobreingeniería).
Si se vuelven a crear, usar el import correcto.

**Estrategia de tests actual (post-Fase 5):**
- ✅ Tests de dominio puros (sin Spring) — validan factories, invariantes, VOs
- ✅ Tests de integración adapters + H2 — validan CRUD de cada modelo a nivel persistencia
- ✅ Smoke test de contexto Spring
- ❌ Tests de use cases con Mockito — eliminados (frágiles, baja señal)
- ❌ Tests de controller con use cases mockeados — eliminados (mock del mock)
- ⏳ Tests E2E de API (CRUD + login + register) — pendientes de decisión del usuario

---

## 3. 🏷️ @UseCase / @Service — Anotaciones restauradas

**Estado:** ✅ Resuelto en Fase 4. Todos los use cases tienen sus anotaciones:
- `@UseCase` para CUD (Crear, Actualizar, Parchear, Eliminar)
- `@Service @Transactional(readOnly = true)` para queries (Buscar, Listar)

---

## 4. 🗑️ Código Legacy — TODO ELIMINADO

**Estado:** ✅ Fase 6 completada. Todo el paquete `com.alertabarrio.ingsoft` fue eliminado.
No quedan referencias a código legacy en el proyecto.

**Archivos eliminados (55):**
- 8 controllers legacy
- 9 services interfaces + 9 service implementations
- 8 JPA repositories
- 8 JPA entities
- 22 DTOs
- 5 exceptions (incluyendo GlobalExceptionHandler)
- 3 config (WebConfig, AuthInterceptor, CorsConfig)
- 1 JwtService
- 1 BrevoService
- 1 test (IngsoftApplicationTests → reemplazado por BarrioAlertaApplicationTests)
- 1 clase principal (IngsoftApplication → renombrada a BarrioAlertaApplication)

---

## 5. 📦 Dependencias Temporales en pom.xml

**MapStruct + Lombok:** El orden de `annotationProcessorPaths` es crítico:
1. Lombok
2. lombok-mapstruct-binding
3. mapstruct-processor

Si se actualizan versiones, verificar que el orden se mantenga.

**H2 para tests:** Dependencia `com.h2database:h2:test` agregada en Fase -1.
Es solo para tests, no debe estar en scope `compile`.

---

## 5.1. 📦 Fuga Spring Data Eliminada — Pagina/Paginacion propios

**Estado:** ✅ Resuelto en corrección post-evaluación arquitectónica.
**Estado adicional (ronda 2):** ✅ HttpStatus eliminado del dominio. Pagina con Jackson. Sort aplicado desde Paginacion. RegistrarYAutenticarUseCase creado.

**Problemas corregidos:**
1. **Fuga Spring Data en puertos:** 16 archivos en `domain/port/out/` y `domain/port/in/` importaban
`org.springframework.data.domain.Page` y `Pageable`, violando la pureza hexagonal.
2. **Fuga HttpStatus en excepciones:** 13 archivos en `domain/exception/` importaban
`org.springframework.http.HttpStatus`, acoplando el dominio a HTTP.
3. **Controllers retornaban Page<Spring>:** La firma pública exponía `org.springframework.data.domain.Page`
en la respuesta JSON.
4. **Sort descartado:** `Paginacion.orden/direccion` no se aplicaba en los adaptadores.
5. **AuthController orquestaba 2 use cases:** El register invocaba secuencialmente
registrarUsuarioUseCase + loginUseCase, violando que el controller no orquesta lógica.

**Soluciones:**
- `domain/model/valueobject/Pagina<T>` — record con anotaciones Jackson (@JsonProperty) para
  serializar como `content`, `page`, `size`, `totalElements`, `totalPages` (compatible con frontend)
- `domain/model/valueobject/Paginacion` — record con pagina, tamanio, orden, direccion
- `domain/exception/CodigoError` — enum puro del dominio: VALIDACION, NO_ENCONTRADO, CONFLICTO, NO_AUTORIZADO, ERROR_EXTERNO
- `domain/exception/DomainException` — ahora usa `CodigoError` en vez de `HttpStatus`
- 13 excepciones de dominio actualizadas para usar `CodigoError`
- `GlobalExceptionHandler` — traduce `CodigoError` → `HttpStatus` vía switch (anti-corruption layer en adapters REST)
- `infrastructure/persistence/adapter/PaginacionHelper` — helper que traduce `Paginacion` → `Pageable`
  con Sort aplicado desde `paginacion.orden()` y `paginacion.direccion()`
- 8 RepositoryAdapters usan `PaginacionHelper.toPageable()` en vez de `PageRequest.of()`
- 10 Controllers retornan `ResponseEntity<Pagina<ResponseDTO>>` en vez de `ResponseEntity<Page<ResponseDTO>>`
- `domain/port/in/RegistrarYAutenticarUseCase` + `RegistrarYAutenticarUseCaseImpl` encapsulan
  la orquestación registro + login
- `AuthController.register` ahora usa `RegistrarYAutenticarUseCase` — el controller solo traduce

**Resultado:** Dominio 100% libre de Spring (0 imports de org.springframework en todo `domain/`).

---

## 6. 🔐 Secrets en application-local.properties

**Archivo:** `src/main/resources/application-local.properties` (en `.gitignore`)

**Propiedades:** `brevo.api.key`, `brevo.sender.email`, `brevo.sender.name`

**Placeholders en `application.properties`:**
```properties
brevo.api.key=${BREVO_API_KEY:changeme}
brevo.sender.email=${BREVO_SENDER_EMAIL:changeme@localhost}
brevo.sender.name=${BREVO_SENDER_NAME:Alerta Barrio}
```

**⚠️ Recordatorio:** La API key original ya está en el historial de git.
Considerar rotación en Brevo y limpieza con `git filter-repo`.

---

## 7. 🧠 Decisiones Arquitectónicas Diferidas

- **`Configuracion.getOrCreateConfig`**: El legacy auto-crea config con defaults
  si no existe. El nuevo dominio NO replica este comportamiento — retorna
  `Optional.empty()`. Decidir en Fase 4 si se implementa un caso de uso
  `ObtenerOCrearConfiguracionUseCase` o se cambia el comportamiento del frontend.

- **`BarrioResponseDTO` anidado**: El legacy devolvía `CuadranteResponseDTO`
  anidado dentro de `BarrioResponseDTO`. El nuevo diseño usa solo
  `Long cuadranteId`. Verificar compatibilidad con frontend en Fase 5.

- **`AlertaResponseDTO` anidado**: Similar, el legacy devolvía
  `CategoriaResponseDTO` anidado. El nuevo diseño usa solo `Long categoriaId`.

---

## 8. 📋 Checklist Pre-Producción

- [x] Reemplazar `WebConfig` + `AuthInterceptor` legacy por versiones hexagonales
- [x] Restaurar anotaciones `@UseCase`/`@Service` en todos los use cases
- [x] Migrar controllers a `adapters/rest/controller/` (10 controllers: Categoria, Cuadrante, Barrio, Usuario, Configuracion, Alerta, Evidencia, CategoriaDescripcion, Auth, Email)
- [x] Crear `GlobalExceptionHandler` en `adapters/rest/exception/`
- [x] Crear `CorsConfig` en `adapters/rest/config/`
- [x] Eliminar todo el paquete `ingsoft/` (Fase 6)
- [x] Crear ADRs en `docs/adr/` (8 archivos)
- [x] Actualizar `README.md` con arquitectura hexagonal
- [ ] Rotar API key de Brevo y limpiar historial git (API key expuesta en commit fc608e7 `Add Brevo email sending endpoint`)
- [ ] Verificar que `application-local.properties` esté en `.gitignore`
- [ ] Ejecutar `mvn clean test` completo y verificar 0 warnings
- [ ] Ejecutar ArchUnit tests de arquitectura (Fase 8)
- [ ] Considerar agregar `spring-boot-starter-security` y reemplazar interceptor por `SecurityFilterChain`
