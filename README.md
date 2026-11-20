# NotariaXPlorer - Plataforma Notarial Digital

Aplicación móvil desarrollada en **Kotlin** con **Jetpack Compose** siguiendo la arquitectura **MVVM** para la simulación de una plataforma de trámites notariales. El proyecto integra persistencia local (Room) y consumo de **API remota con Retrofit (Xano)**.

## Objetivo

Permitir a un usuario registrarse, iniciar sesión y gestionar solicitudes de trámites notariales (crear, listar, editar y eliminar) consumiendo una API backend, además de utilizar recursos nativos del dispositivo.

## Funcionalidades implementadas

- **Arquitectura MVVM** con separación de responsabilidades:
  - `UsuarioViewModel` + `UsuarioRepository`: autenticación/sesión y datos de usuario (Room).
  - `SolicitudViewModel` + `SolicitudRepository`: gestión de solicitudes y catálogo de tipos de trámite (API remota Xano).
- **Autenticación local**: registro e inicio de sesión contra base de datos Room, con persistencia de sesión mediante `SharedPreferences`.
- **CRUD de solicitudes vía API (Xano + Retrofit)** desde la interfaz:
  - Crear solicitud de trámite.
  - Listar solicitudes del usuario.
  - Editar solicitud (estado, prioridad, cantidad de copias).
  - Eliminar solicitud.
  - Manejo de estados de carga, éxito y error en UI.
- **Catálogo de tipos de trámite desde API** (`GET /tipo_tramite`) con fallback local en pantalla de solicitud.
- **Recursos nativos**:
  - Cámara: captura de foto (documento fotográfico) desde `HomeScreen`.
  - GPS/Ubicación: obtención de ubicación actual del dispositivo con permisos en tiempo de ejecución.
- **Navegación con Navigation Compose**.
- **UI con Material Design 3 y Jetpack Compose** (formularios, validaciones, diálogos, estados visuales).
- **Animación** en `SplashScreen`.
- **Permiso de Internet** para consumo de API.

## Estructura del proyecto

```text
com.duoc.notariaplataforma/
├── model/                  # Entidades, DAOs y base de datos Room
│   ├── Usuario.kt
│   ├── UsuarioDao.kt
│   ├── Solicitud.kt
│   ├── SolicitudDao.kt
│   └── NotariaDatabase.kt
├── remote/                 # Integración con API (Retrofit)
│   ├── api/
│   │   ├── RetrofitClient.kt          # Cliente Retrofit para Xano
│   │   └── XanoApiService.kt          # Endpoints Xano (solicitudes/tipos)
│   └── dto/
│       ├── SolicitudDto.kt
│       └── TipoTramiteDto.kt
├── repository/             # Acceso a datos (local/remoto)
│   ├── UsuarioRepository.kt
│   └── SolicitudRepository.kt
├── viewmodel/              # Lógica de presentación y estado
│   ├── UsuarioViewModel.kt
│   └── SolicitudViewModel.kt
├── util/
│   └── SesionManager.kt
├── ui/                     # Pantallas y navegación (Compose)
│   ├── SplashScreen.kt
│   ├── LoginScreen.kt
│   ├── RegistroScreen.kt
│   ├── HomeScreen.kt
│   ├── SolicitudTramiteScreen.kt
│   ├── MisSolicitudesScreen.kt
│   ├── NavegacionApp.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt
```

## Integración API (Xano)

- **Base URL:** `https://x8ki-letl-twmt.n7.xano.io/api:WSxJtlCC/`
- **Grupo/API:** `Apis legales notariax` (`WSxJtlCC`)

| Método | Endpoint | Descripción | Uso en la app |
|---|---|---|---|
| `GET` | `/solicitud` | Lista solicitudes (filtro opcional `usuario_correo`) | Cargar/listar y refrescar "Mis Solicitudes" |
| `POST` | `/solicitud` | Crea una solicitud | Enviar formulario de "Solicitar Trámite Notarial" |
| `GET` | `/solicitud/{solicitud_id}` | Obtiene una solicitud por ID | Disponible en capa API/repositorio (uso futuro/detalle) |
| `PATCH` | `/solicitud/{solicitud_id}` | Actualiza una solicitud | Editar solicitud desde "Mis Solicitudes" |
| `DELETE` | `/solicitud/{solicitud_id}` | Elimina una solicitud | Eliminar solicitud desde "Mis Solicitudes" |
| `GET` | `/tipo_tramite` | Lista tipos de trámite | Poblar catálogo en formulario de solicitud |
| `GET` | `/tipo_tramite/{tipo_tramite_id}` | Obtiene un tipo de trámite por ID | Definido en API (uso futuro) |

**Campos principales de solicitud (JSON):** `id`, `created_at`, `nombre_solicitante`, `tipo_tramite`, `prioridad`, `cantidad_copias`, `estado`, `fecha_registro`, `usuario_correo`.

## Pantallas principales

| Pantalla | Descripción |
|---|---|
| **SplashScreen** | Animación inicial y redirección según sesión activa. |
| **LoginScreen** | Inicio de sesión local (Room) y guardado de sesión. |
| **RegistroScreen** | Registro de usuario con validación y persistencia local. |
| **HomeScreen** | Menú principal: cámara, GPS, solicitud de trámite, mis solicitudes y cierre de sesión. |
| **SolicitudTramiteScreen** | Formulario para crear solicitud; carga tipos desde API y envía solicitud a Xano. |
| **MisSolicitudesScreen** | Lista solicitudes remotas del usuario; permite refrescar, editar y eliminar. |

## Tecnologías y dependencias principales

- **Kotlin**, **Jetpack Compose**, **Material 3**
- **MVVM**, **ViewModel**, **StateFlow/Coroutines**
- **Room** (KSP) para persistencia local
- **Retrofit + OkHttp + Gson** para consumo de API
- **Navigation Compose**
- **Google Play Services Location** (GPS)
- **Activity Result APIs / FileProvider** (cámara)

## Requisitos y ejecución

1. Abrir el proyecto `NOTARIA PRUEBA` en **Android Studio** (compatible con AGP/Kotlin configurados en el proyecto).
2. Sincronizar Gradle.
3. Ejecutar en emulador o dispositivo Android con conexión a internet (requerida para Xano).
4. Para probar GPS, habilitar servicios de ubicación del dispositivo/emulador y otorgar permisos.
5. Para probar cámara, otorgar permiso de cámara.

## Notas del estado actual

- El **login/registro/sesión** se gestionan localmente (Room + `SharedPreferences`).
- Las **solicitudes de trámite** se gestionan de forma remota en **Xano** (CRUD desde la app).
- La foto capturada se muestra en memoria (no se persiste en base de datos).

## Pendientes / entregables de evaluación (Parcial 4)

- Pruebas unitarias de lógica (objetivo de cobertura según rúbrica).
- APK firmado en release (`.jks`, configuración de firma) y evidencias.
- Ícono personalizado de la aplicación.
- Evidencias de colaboración/repositorio (Git) y gestión (Trello), según indicaciones del curso.
- Completar/ajustar documentación final (integrantes, capturas/evidencias y pasos adicionales si aplican).

## Autores

- Matias Duncker
- Diego Garrido

**Curso:** DSY1105 - Desarrollo de Aplicaciones Móviles (DUOC UC)  
**Caso:** NotariaX
