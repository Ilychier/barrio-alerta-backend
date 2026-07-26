# Barrio Alerta Backend — Technical Debt & Migration Notes

> Este archivo documenta deuda técnica, decisiones temporales y recordatorios
> para limpiar antes de producción. Se actualiza durante el refactor hexagonal.

---

## 1. 🔧 WebConfig.java — Auth Interceptor Artesanal

**Archivo:** `src/main/java/com/alertabarrio/ingsoft/config/WebConfig.java`

**Problema:** Usa un `AuthInterceptor` casero con lista hardcodeada de exclusiones.
Cada vez que se migra un controller hexagonal, hay que agregar su ruta a la lista
de `excludePathPatterns`. Esto es frágil y propenso a errores.

**Solución futura (Fase 5.8 del plan):** Reemplazar por `SecurityFilterChain` de
Spring Security con `AuthenticationEntryPoint` y `AccessDeniedHandler`
personalizados. Ver ADR `0003-spring-security-vs-interceptor.md`.

**Exclusiones actuales:**
```
/api/auth/login
/api/auth/register
/api/barrios/**
/api/categoria-descripciones/**
/api/categorias/**
/api/cuadrantes/**
/swagger-ui/**
/swagger-ui.html
/api-docs/**
```

**⚠️ Recordatorio:** Cuando se migre el último controller, eliminar
`AuthInterceptor.java`, `WebConfig.java`, y toda la lógica de auth artesanal.

---

## 2. 🧪 Tests — @AutoConfigureMockMvc en Spring Boot 4.x

**Problema:** Spring Boot 4.x movió `@AutoConfigureMockMvc` de
`org.springframework.boot.test.autoconfigure.web.servlet` a
`org.springframework.boot.webmvc.test.autoconfigure`.

**Impacto:** Todos los controller tests nuevos deben usar el import correcto.
Los tests legacy (si existen) pueden fallar si usan el import antiguo.

**Archivos afectados:** Todos los `*ControllerTest.java` en
`src/test/java/com/alertabarrio/adapters/rest/controller/`.

**Recordatorio:** Si en Fase 6 se migran tests legacy, verificar el import.

---

## 3. 🏷️ @UseCase / @Service — Anotaciones removidas temporalmente

**Problema:** Los use cases de las nuevas entidades (Barrio, CategoriaDescripcion,
User, Configuracion, Alerta, Evidencia) no tienen implementaciones de sus puertos
driven (adapters) todavía. Por eso se removieron las anotaciones `@UseCase`,
`@Service` y `@Transactional` de sus implementaciones.

**Impacto:** Estos use cases no son beans de Spring hasta la Fase 3 (adaptadores)
o Fase 4 (wiring completo). Los controller tests usan `@TestConfiguration` con
`@Primary` mocks, por lo que no necesitan los reales.

**Archivos afectados:** Todos los `*UseCaseImpl.java` en
`src/main/java/com/alertabarrio/application/usecase/` excepto Categoria y Cuadrante.

**Recordatorio en Fase 4:** Restaurar las anotaciones `@UseCase` (para CUD) y
`@Service @Transactional(readOnly = true)` (para queries) en TODOS los use cases.

---

## 4. 🗑️ Código Legacy Pendiente de Eliminación

A medida que se completa cada fase, el código legacy correspondiente debe eliminarse.
Este es el checklist actual:

| Fase | Código Legacy a Eliminar | Estado |
|------|--------------------------|--------|
| Fase 1 | `ingsoft/controllers/CategoriaController.java` | ✅ Eliminado |
| Fase 2.1 | `ingsoft/controllers/CuadranteController.java` | ✅ Eliminado |
| Fase 3 | `ingsoft/repositories/*.java` (todos) | ⏳ Pendiente |
| Fase 3 | `ingsoft/models/entities/*.java` (todos) | ⏳ Pendiente |
| Fase 4 | `ingsoft/services/*.java` (interfaces) | ⏳ Pendiente |
| Fase 4 | `ingsoft/services/implementation/*.java` | ⏳ Pendiente |
| Fase 5 | `ingsoft/controllers/*.java` (restantes) | ⏳ Pendiente |
| Fase 5 | `ingsoft/config/AuthInterceptor.java` | ⏳ Pendiente |
| Fase 5 | `ingsoft/config/WebConfig.java` | ⏳ Pendiente |
| Fase 6 | `ingsoft/exceptions/*.java` (si reemplazados) | ⏳ Pendiente |
| Fase 6 | `ingsoft/models/dtos/*.java` (si reemplazados) | ⏳ Pendiente |

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

- [ ] Reemplazar `WebConfig` + `AuthInterceptor` por `SecurityFilterChain`
- [ ] Restaurar anotaciones `@UseCase`/`@Service` en todos los use cases
- [ ] Eliminar todo el paquete `ingsoft/`
- [ ] Verificar imports de `@AutoConfigureMockMvc` en todos los tests
- [ ] Rotar API key de Brevo y limpiar historial git
- [ ] Verificar que `application-local.properties` esté en `.gitignore`
- [ ] Ejecutar `mvn clean test` completo y verificar 0 warnings
- [ ] Ejecutar ArchUnit tests de arquitectura (Fase 8)
