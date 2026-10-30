# NotariaXPlorer - Plataforma Notarial Digital

Aplicación móvil desarrollada en **Kotlin** con **Jetpack Compose** siguiendo la arquitectura **MVVM (Model-View-ViewModel)** para la simulación de una plataforma digital de trámites notariales.

## Objetivo

Simular un sistema donde un usuario pueda registrarse, iniciar sesión, solicitar trámites notariales, adjuntar evidencia fotográfica, obtener su ubicación y consultar sus solicitudes, aplicando los conceptos aprendidos en desarrollo móvil.

## Características Implementadas

- [x] **Arquitectura MVVM** - Separación clara entre Model, View, ViewModel y Repository
- [x] **Formulario** - Pantalla de solicitud de trámite con múltiples elementos
- [x] **Validación de campos** - Validación en Login, Registro y Formulario de Solicitud
- [x] **Imágenes** - Logo de NotariaX y captura de fotos con cámara nativa
- [x] **Animación** - Animación fade-in y slide-in en SplashScreen
- [x] **Modal (AlertDialog)** - Diálogos de confirmación, error, bienvenida y ubicación
- [x] **Persistencia con Room** - Base de datos local para Usuarios y Solicitudes
- [x] **Navegación entre vistas** - Navigation Compose con rutas definidas
- [x] **Recurso nativo** - Cámara fotográfica
- [x] **Persistencia de sesión** - Sesión guardada con SharedPreferences (mantiene sesión al cerrar app)
- [x] **Mis Solicitudes** - Listado de solicitudes guardadas con estado "En Proceso"
- [x] **Diseño profesional** - Paleta de colores corporativa adecuada a una notaría

## Estructura del Proyecto

```text
com.duoc.notariaplataforma/
├── model/          # Entidades, DAOs y Base de Datos Room
│   ├── Usuario.kt
│   ├── UsuarioDao.kt
│   ├── Solicitud.kt
│   ├── SolicitudDao.kt
│   └── NotariaDatabase.kt
├── repository/     # Repositorio para abstracción de datos
│   └── UsuarioRepository.kt
├── viewmodel/      # Lógica de presentación y gestión de estados
│   └── UsuarioViewModel.kt
├── util/           # Utilidades (Gestor de sesión)
│   └── SesionManager.kt
├── ui/             # Pantallas y componentes Compose
│   ├── SplashScreen.kt
│   ├── LoginScreen.kt
│   ├── RegistroScreen.kt
│   ├── HomeScreen.kt
│   ├── SolicitudTramiteScreen.kt
│   ├── MisSolicitudesScreen.kt
│   ├── NavegacionApp.kt
│   └──theme/      # Tema, colores y tipografía
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
└── MainActivity.kt
```

## Pantallas

| Pantalla | Descripción |
|---|---|
| **SplashScreen** | Pantalla de bienvenida con animación. Verifica si existe sesión activa para redirigir automáticamente. |
| **LoginScreen** | Inicio de sesión con validación contra base de datos Room. Guarda sesión con SharedPreferences. |
| **RegistroScreen** | Registro de nuevo usuario con validación de campos, contraseñas coincidentes y persistencia en Room. |
| **HomeScreen** | Menú principal con acceso a servicios. Muestra usuario logueado, permite tomar foto, obtener ubicación, solicitar trámite y ver mis solicitudes. |
| **SolicitudTramiteScreen** | Formulario completo con TextField, Dropdown, RadioButtons, Checkbox, Slider y modal de confirmación. Guarda la solicitud en Room. |
| **MisSolicitudesScreen** | Lista de solicitudes registradas con su tipo, prioridad, copias, solicitante y estado "En Proceso". |

## Tecnologías Utilizadas

- **Kotlin** - Lenguaje principal
- **Jetpack Compose** - UI declarativa
- **Material Design 3** - Componentes visuales modernos
- **MVVM Architecture** - Patrón arquitectónico
- **Room Database** - Persistencia local
- **Navigation Compose** - Navegación entre pantallas
- **Kotlin Coroutines & Flow** - Programación asincrónica y estados reactivos
- **ViewModel & LiveData/StateFlow** - Gestión de estados
- **SharedPreferences** - Persistencia de sesión
- **CameraX/ActivityResultContracts** - Captura de fotos
- **Google Play Services Location** - Obtención de ubicación GPS
- **FileProvider** - Intercambio seguro de archivos con cámara

## Notas

- Las solicitudes se guardan localmente con estado **"En Proceso"** de forma estática.
- La foto capturada se muestra en memoria únicamente (no se guarda en Room).
- Al iniciar sesión, se guarda el correo en SharedPreferences para mantener la sesión activa.
- Al cerrar sesión se limpia completamente la sesión.

## Autor

Proyecto desarrollado para el curso **DSY1105 - Desarrollo de Aplicaciones Móviles** - DUOC UC

Por: Matias Duncker y Diego Garrido

**Caso:** NotariaX
