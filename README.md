# Helipagos Android App

Aplicación Android nativa desarrollada en Kotlin con Jetpack Compose para gestionar solicitudes de pago utilizando la API de Helipagos.

## 🏗️ Arquitectura

El proyecto implementa **Clean Architecture** con separación clara de responsabilidades:

```
app/src/main/java/com/example/helipagos_android/
├── di/                           # Módulos de inyección de dependencias (Hilt)
├── features/paymentrequests/     # Feature module para payment requests
│   ├── data/                     # Data layer
│   │   ├── api/                  # Interfaces Retrofit
│   │   ├── model/                # DTOs y mappers
│   │   └── repository/           # Implementación Repository
│   ├── domain/                   # Domain layer
│   │   ├── model/                # Entidades de dominio
│   │   ├── repository/           # Interface Repository
│   │   └── usecases/             # Casos de uso
│   └── ui/                       # UI layer
│       ├── list/                 # Pantalla lista
│       ├── detail/               # Pantalla detalle
│       ├── create/               # Formulario creación
│       └── navigation/           # Setup navegación
├── common/ui/                    # Componentes reutilizables
├── ui/theme/                     # Theming Material3
└── utils/                        # Utilidades
```

## 🚀 Tecnologías Utilizadas

### Core

- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - Framework UI moderno
- **Material Design 3** - Sistema de diseño

### Arquitectura

- **Clean Architecture** - Separación de capas
- **MVVM** - Patrón arquitectural
- **StateFlow** - Gestión de estado reactivo
- **Hilt** - Inyección de dependencias

### Networking

- **Retrofit** - Cliente HTTP
- **OkHttp** - HTTP client con interceptores
- **kotlinx.serialization** - Serialización JSON

### Concurrencia

- **Kotlin Coroutines** - Programación asíncrona
- **Flow** - Streams de datos reactivos

### Testing

- **JUnit** - Testing framework
- **Mockk** - Mocking library
- **Turbine** - Testing para Flow
- **Compose Testing** - UI testing

## 📱 Funcionalidades Implementadas

### ✅ Funcionalidades Principales

1. **Lista de Solicitudes de Pago**

   - Consumo de API con paginación
   - LazyColumn para performance
   - Estados de loading, error y vacío
   - Pull-to-refresh
   - Manejo de errores con retry

2. **Detalle de Solicitud**

   - Navegación desde lista
   - Pantalla dedicada con información completa
   - Estado gestionado por ViewModel

3. **Crear Solicitud de Pago**
   - Formulario con validaciones reactivas
   - Amount: obligatorio, numérico > 0
   - Description: obligatorio, mínimo 5 caracteres
   - Actualización optimista de lista

### ✅ Funcionalidades Bonus

4. **Paginación Avanzada**

   - Controles Anterior/Siguiente
   - Indicador de página actual
   - Manejo de estados de página

5. **Validaciones Avanzadas**

   - Errores inline en campos
   - Submit deshabilitado si inválido
   - Estado reactivo con derivedStateOf

6. **Cancelación de Peticiones**
   - flatMapLatest para cancelar requests pendientes
   - Gestión adecuada del ciclo de vida

## 🛠️ Setup del Proyecto

### Prerrequisitos

- Android Studio Arctic Fox o superior
- JDK 11 o superior
- Android SDK 24+

### Instalación

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/tu-usuario/helipagos-android.git
   cd helipagos-android
   ```

2. **Abrir en Android Studio**

   - File → Open → Seleccionar carpeta del proyecto
   - Esperar sincronización de Gradle

3. **Configurar Base URL** (opcional)

   - La URL base está configurada en `build.gradle.kts`:

   ```kotlin
   buildConfigField("String", "BASE_URL", "\"https://sandbox.helipagos.com/\"")
   ```

4. **Build y ejecutar**
   ```bash
   ./gradlew assembleDebug
   ```

## 🧪 Testing

### Ejecutar Tests Unitarios

```bash
./gradlew testDebugUnitTest
```

### Ejecutar Tests de UI

```bash
./gradlew connectedDebugAndroidTest
```

### Cobertura de Tests

- **ViewModel Tests**: Lógica de estado y casos de uso
- **Repository Tests**: Interacciones con API
- **Compose UI Tests**: Renderizado y interacciones

## 📋 API Integration

### Autenticación

La API requiere autenticación Bearer Token:

```
Authorization: Bearer token
```

El token está configurado en `build.gradle.kts` y es añadido automáticamente por el interceptor de autenticación.

### Endpoints Utilizados

```kotlin
// Lista paginada de payment requests
GET /solicitud_pago/v1/page/solicitud_pago?pageNumber={page}&pageSize={limit}

// Detalle de payment request específico
GET /solicitud_pago/v1/page/solicitud_pago?id={id}&pageNumber={page}&pageSize={limit}

// Crear nuevo payment request
POST /solicitud_pago/v1/checkout/solicitud_pago
Body: {
   "importe": Int,
   "fecha_vto": String,
   "descripcion": String,
   "recargo": Double? (optional),
   "fecha_2do_vto": String? (optional),
   "referencia_externa": String? (optional),
   "referencia_externa_2": String? (optional),
   "url_redirect": String? (optional),
   "webhook": String? (optional),
   "qr": Boolean? (optional)
}
```

### Modelos de Datos

- **PaymentRequest**: Entidad de dominio
- **PaymentRequestDto**: DTO para API
- **Mappers**: Transformación DTO ↔ Domain

## 🎨 UX/UI Features

### Material Design 3

- Theming consistente
- Tokens de design
- Componentes adaptativos

### Estados de Loading

- Skeleton screens
- Progress indicators
- Pull-to-refresh

### Gestión de Errores

- Error states informativos
- Retry mechanisms
- Validaciones en tiempo real

### Responsive Design

- Layouts adaptativos
- Componentes reutilizables
- Accessibility considerations

## 🔧 Configuración de Build

### Variantes de Build

- **Debug**: Logging habilitado, base URL sandbox
- **Release**: Ofuscación, optimizaciones

### Dependencias Principales

```kotlin
// Compose BOM para versiones consistentes
implementation(platform("androidx.compose:compose-bom:2024.09.03"))

// Core Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")

// Hilt para DI
implementation("com.google.dagger:hilt-android:2.49")

// Retrofit para networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")

// Coroutines para concurrencia
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

## 📦 Entregables

### APK Debug

Disponible en `app/build/outputs/apk/debug/` después del build

### Funcionalidades Completadas

- ✅ Lista de payment requests con paginación
- ✅ Detalle de payment request
- ✅ Crear payment request con validaciones
- ✅ Loading states y error handling
- ✅ Paginación con controles
- ✅ Validaciones avanzadas
- ✅ Cancelación de requests
- ✅ Tests unitarios y de UI
- ✅ Arquitectura modular

## 🏗️ Decisiones Arquitecturales

### Clean Architecture

- **Separación clara** entre UI, Domain y Data layers
- **Testabilidad** mejorada con inyección de dependencias
- **Escalabilidad** para futuras features

### State Management

- **StateFlow** para estado observable
- **UiState pattern** para estados consistentes
- **Unidirectional data flow**

### Error Handling

- **Result wrapper** para manejo consistente
- **Network exceptions** tipadas
- **User-friendly error messages**

## 🚀 Próximos Pasos

### Mejoras Potenciales

- Room database para cache local
- Paging 3 library para paginación avanzada
- Offline-first architecture
- Push notifications
- Biometric authentication

### Performance

- Image optimization
- Memory leak prevention
- Background task optimization

## Contacto

Para consultas sobre la implementación o arquitectura del proyecto:

- **Desarrollador**: Lautaro Correa
- **Email**: lautico123456789@gmail.com
- **[GitHub](https://github.com/lautico)**

---

_Desarrollado como prueba técnica para Helipagos - Octubre 2025_
