# LickLib - Library Manager Sandbox
---

> **Version:** 0.1.0 (Development Snapshot)  
> **Stand:** März 2026  
> **Status:** Lernprojekt / In Entwicklung

Ein Spring Boot Backend zur Verwaltung von Guitar Licks (kurze Gitarrenphrasen/Riffs), entwickelt zu Lernzwecken. Das Projekt demonstriert moderne Java-Backend-Entwicklung mit RESTful APIs, PostgreSQL-Persistierung und umfassenden Tests.

---

## 📋 Inhaltsverzeichnis

- [Überblick](#überblick)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architektur](#architektur)
- [Projektstruktur](#projektstruktur)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Datenbank-Migration](#datenbank-migration)
- [Tests ausführen](#tests-ausführen)
- [API-Endpunkte](#api-endpunkte)
- [Frontend-Integration](#frontend-integration)
- [Entwicklungskonzepte](#entwicklungskonzepte)
- [Bekannte Einschränkungen](#bekannte-einschränkungen)
- [Roadmap](#roadmap)

---

## 🎯 Überblick

**LickLib** ist eine Backend-Anwendung, die es ermöglicht:
- Guitar Licks (Titel, Artist, Beschreibung, Dauer) zu verwalten
- Benutzer anzulegen und zuzuordnen
- Tracks nach Benutzernamen zu filtern
- RESTful CRUD-Operationen durchzuführen

Das Projekt dient primär **Lernzwecken** und demonstriert:
- Layered Architecture (Controller → Service → Repository)
- Spring Boot Best Practices
- Test-Driven Development (Unit- und Integration-Tests)
- Datenbank-Migrationen mit Flyway
- CORS-Konfiguration für Frontend-Integration

---

## ✨ Features

- ✅ **User Management**: Anlegen, Aktualisieren, Löschen von Benutzern
- ✅ **Track Management**: Verwalten von Guitar Licks mit Metadaten
- ✅ **Filterung**: Tracks nach Benutzernamen durchsuchen
- ✅ **Validierung**: Jakarta Validation für DTOs
- ✅ **CORS Support**: Vorbereitet für Angular/React-Frontends
- ✅ **PostgreSQL**: Persistierung mit JPA/Hibernate
- ✅ **Flyway**: Versionskontrollierte DB-Migrationen
- ✅ **Testcontainers**: Integration-Tests mit echter PostgreSQL-Instanz

---

## 🛠 Tech Stack

### Backend
- **Java 21** (oder kompatible Version)
- **Spring Boot 3.x**
    - Spring Web (REST)
    - Spring Data JPA (Hibernate)
    - Spring Validation
- **PostgreSQL 15** (Production Database)
- **Flyway** (DB Migration)
- **Lombok** (Boilerplate Reduction)

### Testing
- **JUnit 5** (Jupiter)
- **Mockito** (Unit Tests)
- **AssertJ** (Fluent Assertions)
- **MockMvc** (Controller Tests)
- **Testcontainers** (PostgreSQL Integration Tests)

### Build Tool
- **Gradle** (mit Kotlin DSL oder Groovy)

---

## 🏗 Architektur

LickLib folgt einer klassischen **3-Schichten-Architektur**:

```
┌─────────────────────────────────────────┐
│          REST Controllers               │  ← HTTP-Endpunkte
│  (TrackController, UserController)      │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│          Service Layer                  │  ← Business Logic
│  (TrackService, UserService)            │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│          Repository Layer               │  ← Datenzugriff
│  (TrackRepository, UserRepository)      │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│          PostgreSQL Database            │
└─────────────────────────────────────────┘
```

### Komponenten-Übersicht

#### 1. **Controller Layer** (`controller/`)
- Empfängt HTTP-Requests
- Validiert Input via Jakarta Validation
- Delegiert an Services
- Gibt DTOs zurück (nie Entity-Objekte direkt)

#### 2. **Service Layer** (`service/`)
- Enthält Business Logic
- Koordiniert Repository-Aufrufe
- Mapped zwischen Entities und DTOs
- Transaktionssteuerung (`@Transactional`)

#### 3. **Repository Layer** (`repository/`)
- Spring Data JPA Interfaces
- Automatische Query-Generierung (z.B. `findByCreatorUsername`)
- Direkte Interaktion mit der Datenbank

#### 4. **Model Layer** (`model/`)
- JPA Entities (`User`, `Track`)
- Mapping zu DB-Tabellen
- Beziehungen (z.B. `@ManyToOne` zwischen Track und User)

#### 5. **DTO Layer** (`dto/`)
- **Request DTOs**: Daten vom Client (Validierung)
- **Response DTOs**: Daten zum Client (ohne sensible Infos)
- Entkopplung von API-Struktur und DB-Schema

---

## 📂 Projektstruktur

```
src/
├── main/
│   ├── java/de/seitz/licklib/
│   │   ├── config/
│   │   │   └── WebConfig.java              # CORS-Konfiguration
│   │   ├── controller/
│   │   │   ├── TrackController.java        # REST-Endpunkte für Tracks
│   │   │   └── UserController.java         # REST-Endpunkte für User
│   │   ├── dto/
│   │   │   ├── track/
│   │   │   │   ├── TrackCreateDTO.java     # Input für neue Tracks
│   │   │   │   ├── TrackUpdateDTO.java     # Partial Updates
│   │   │   │   └── TrackResponseDTO.java   # Output für Client
│   │   │   └── user/
│   │   │       ├── UserRequestDTO.java
│   │   │       └── UserResponseDTO.java
│   │   ├── model/
│   │   │   ├── Track.java                  # JPA Entity
│   │   │   └── User.java                   # JPA Entity
│   │   ├── repository/
│   │   │   ├── TrackRepository.java        # Spring Data JPA
│   │   │   └── UserRepository.java
│   │   ├── service/
│   │   │   ├── TrackService.java           # Business Logic
│   │   │   └── UserService.java
│   │   └── LicklibApplication.java         # Spring Boot Entry Point
│   └── resources/
│       ├── application.yml                 # Spring Config
│       └── db/migration/                   # Flyway Migrations
│           ├── V1__init_schema_kek.sql
│           ├── V2__updated_schema.sql
│           ├── V3__added_mockdata.sql
│           ├── V4__added_more_mockdata.sql
│           ├── V5__updated_track_schema.sql
│           ├── V6__fix_schemas.sql
│           └── V7__delete_dups.sql
└── test/
    ├── java/de/seitz/licklib/
    │   ├── controller/
    │   │   └── TrackControllerTest.java    # MockMvc Tests
    │   ├── repository/
    │   │   ├── TrackRepositoryTest.java    # Testcontainers
    │   │   └── UserRepositoryTest.java
    │   ├── service/
    │   │   ├── TrackServiceTest.java       # Mockito Unit Tests
    │   │   └── UserServiceTest.java
    │   └── LicklibApplicationTests.java
    └── resources/
        └── application.yml                 # Test-spezifische Config
```

### Wichtige Verzeichnisse

| Pfad | Zweck |
|------|-------|
| `config/` | Spring-Konfigurationsklassen (CORS, Security, etc.) |
| `dto/` | Data Transfer Objects (API-Schnittstelle) |
| `model/` | JPA-Entities (DB-Tabellen) |
| `repository/` | Datenzugriffsschicht (Spring Data JPA) |
| `service/` | Geschäftslogik |
| `controller/` | REST-API-Endpunkte |
| `db/migration/` | SQL-Migrationen (Flyway) |

---

## ⚙️ Prerequisites

### Software-Anforderungen

- **Java Development Kit (JDK)**: Version 17 oder höher
  ```bash
  java -version
  ```

- **Gradle**: Version 7.x+ (oder nutze den Wrapper `./gradlew`)
  ```bash
  gradle -version
  ```

- **PostgreSQL**: Version 13+
  ```bash
  psql --version
  ```

- **Docker** (optional, für Testcontainers):
  ```bash
  docker --version
  ```

### Empfohlene IDEs
- IntelliJ IDEA (Ultimate oder Community)
- Eclipse mit Spring Tools
- VS Code mit Java/Spring Extensions

---

## 🚀 Installation & Setup

### 1. Repository klonen
```bash
git clone <repository-url>
cd licklib
```

### 2. PostgreSQL-Datenbank einrichten

#### Variante A: Manuell
```bash
# PostgreSQL starten (Beispiel macOS)
brew services start postgresql

# Datenbank und User erstellen
psql postgres
```

```sql
CREATE DATABASE licklib;
CREATE USER myuser WITH PASSWORD 'secret';
GRANT ALL PRIVILEGES ON DATABASE licklib TO myuser;
\q
```

#### Variante B: Docker Compose
```bash
docker-compose up -d
```

**Erwartete DB-Konfiguration** (siehe `application.yml`):
- Host: `localhost:5432`
- Database: `licklib`
- User: `myuser`
- Password: `secret`

### 3. Application Properties prüfen

Datei: `src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/licklib
    username: myuser
    password: secret
  flyway:
    enabled: true
```

### 4. Projekt bauen
```bash
./gradlew clean build
```

### 5. Anwendung starten
```bash
./gradlew bootRun
```

Die Anwendung läuft auf: **http://localhost:8080**

### 6. Smoke Test
```bash
curl http://localhost:8080/api/users/findAll
```

Erwartete Antwort (Beispiel):
```json
[
  {
    "id": "d290f1ee-6c54-4b01-90e6-d701748f0851",
    "username": "Alex",
    "email": "alex@example.com"
  }
]
```

---

## 🗄 Datenbank-Migration

LickLib nutzt **Flyway** für versionskontrollierte Schema-Änderungen.

### Migrations-Flow

1. **Beim ersten Start** führt Flyway automatisch alle Skripte in `db/migration/` aus
2. **Reihenfolge** wird durch Versionsnummern bestimmt (`V1__`, `V2__`, ...)
3. **Tracking** in Tabelle `flyway_schema_history`

### Neue Migration erstellen

```bash
# Datei erstellen
touch src/main/resources/db/migration/V8__add_user_role.sql
```

```sql
-- V8__add_user_role.sql
ALTER TABLE app_user ADD COLUMN role VARCHAR(20) DEFAULT 'USER';
```

Beim nächsten Start wird diese automatisch angewendet.

### Flyway-Befehle (manuell)

```bash
# Status prüfen
./gradlew flywayInfo

# Migrationen ausführen
./gradlew flywayMigrate

# Letzte Migration rückgängig (VORSICHT!)
./gradlew flywayUndo
```

---

## 🧪 Tests ausführen

### Alle Tests
```bash
./gradlew test
```

### Spezifische Test-Klasse
```bash
./gradlew test --tests TrackServiceTest
```

### Test-Coverage-Report
```bash
./gradlew jacocoTestReport
# Report: build/reports/jacoco/test/html/index.html
```

### Test-Arten im Projekt

#### 1. **Unit Tests** (Service Layer)
- Nutzen Mockito für Repository-Mocks
- Testen Business Logic isoliert
- Beispiel: `TrackServiceTest`, `UserServiceTest`

```java
@ExtendWith(MockitoExtension.class)
class TrackServiceTest {
    @Mock private TrackRepository trackRepository;
    @InjectMocks private TrackService trackService;
    // ...
}
```

#### 2. **Integration Tests** (Repository Layer)
- Nutzen Testcontainers für echte PostgreSQL-Instanz
- Testen JPA-Queries gegen reale DB
- Beispiel: `TrackRepositoryTest`

```java
@DataJpaTest
@Testcontainers
class TrackRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
    // ...
}
```

#### 3. **Controller Tests** (Web Layer)
- Nutzen MockMvc für HTTP-Request-Simulation
- Testen JSON-Serialisierung und Validierung
- Beispiel: `TrackControllerTest`

```java
@WebMvcTest(TrackController.class)
class TrackControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private TrackService trackService;
    // ...
}
```

---

## 📡 API-Endpunkte

Base URL: `http://localhost:8080/api`

### Tracks

| Method | Endpoint | Beschreibung | Body |
|--------|----------|--------------|------|
| `GET` | `/tracks/{id}` | Track per ID abrufen | - |
| `GET` | `/tracks/` | Alle Tracks abrufen | - |
| `GET` | `/tracks/search/{username}` | Tracks eines Users | - |
| `POST` | `/tracks/` | Neuen Track erstellen | `TrackCreateDTO` |
| `PATCH` | `/tracks/{id}` | Track aktualisieren | `TrackUpdateDTO` |
| `DELETE` | `/tracks/{id}` | Track löschen | - |

### Users

| Method | Endpoint | Beschreibung | Body |
|--------|----------|--------------|------|
| `GET` | `/users/{id}` | User per ID abrufen | - |
| `GET` | `/users/findAll` | Alle User abrufen | - |
| `GET` | `/users/search/{username}` | User per Name suchen | - |
| `POST` | `/users/` | Neuen User erstellen | `UserRequestDTO` |
| `PATCH` | `/users/{id}` | User aktualisieren | `UserRequestDTO` |
| `DELETE` | `/users/{id}` | User löschen | - |

### Beispiel-Requests

#### Track erstellen
```bash
curl -X POST http://localhost:8080/api/tracks/ \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Pentatonic Burn",
    "description": "Fast alternate picking in E minor",
    "artist": "Zakk Wylde",
    "size": 1500,
    "duration": 20,
    "creatorId": "d290f1ee-6c54-4b01-90e6-d701748f0851"
  }'
```

#### User erstellen
```bash
curl -X POST http://localhost:8080/api/users/ \
  -H "Content-Type: application/json" \
  -d '{
    "username": "shredder",
    "email": "shredder@example.com"
  }'
```

---

## 🎨 Frontend-Integration

Das Backend ist vorbereitet für zwei Frontend-Varianten:

### 1. Angular Frontend
- **Port**: 4200
- **CORS**: Bereits konfiguriert in `WebConfig.java`
- **Endpunkte**: Siehe API-Dokumentation oben

### 2. React/Vite Frontend
- **Port**: 5173
- **CORS**: Bereits konfiguriert in `WebConfig.java`

---

## 🅰️ Angular Frontend

### Übersicht

**Port**: `http://localhost:4200`  
**Framework**: Angular 17+ (Standalone Components)  
**State Management**: RxJS Observables / Signals  
**Styling**: Angular Material oder Tailwind CSS

### Schnellstart

```bash
# Angular CLI installieren
npm install -g @angular/cli
 
# Projekt erstellen
ng new licklib-frontend
cd licklib-frontend
 
# Optional: Angular Material
ng add @angular/material
 
# Starten
ng serve
```

### Architektur-Konzepte

#### 1. **Layered Structure**
```
src/app/
├── core/          # Services, Guards, Interceptors (Singletons)
├── shared/        # Wiederverwendbare Components & Pipes
└── features/      # Feature Modules (tracks/, users/)
```

#### 2. **Services & Dependency Injection**
- `TrackService`, `UserService` für API-Kommunikation
- `HttpClient` für typsichere REST-Calls
- Observables für asynchrone Datenströme

#### 3. **Reactive Forms**
- Formular-Validierung mit `Validators`
- Typsichere Form Controls
- Error Handling pro Feld

#### 4. **Routing & Lazy Loading**
- Feature Modules werden on-demand geladen
- Guards für Route Protection
- Nested Routes für Detail-Ansichten

#### 5. **TypeScript Models**
- Interfaces für API-DTOs (`TrackResponse`, `TrackCreate`)
- Typsicherheit zwischen Frontend und Backend

### Key Features

✅ **Reactive Programming** mit RxJS  
✅ **Dependency Injection** out-of-the-box  
✅ **Built-in Form Validation**  
✅ **Angular Material** für UI-Components  
✅ **CLI-Support** für Scaffolding

### Build & Deploy

```bash
# Production Build
ng build --configuration production
# Output: dist/licklib-frontend/
```
 
---

## ⚛️ React/Vite Frontend

### Übersicht

**Port**: `http://localhost:5173`  
**Framework**: React 18+ mit TypeScript  
**Build Tool**: Vite (ultra-schnelles HMR)  
**State Management**: React Query (TanStack Query)  
**Styling**: Tailwind CSS

### Schnellstart

```bash
# Projekt mit Vite erstellen
npm create vite@latest licklib-frontend -- --template react-ts
cd licklib-frontend
 
# Dependencies
npm install
npm install react-router-dom @tanstack/react-query axios
 
# Tailwind CSS (optional)
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
 
# Starten
npm run dev
```

### Architektur-Konzepte

#### 1. **Component-Based Structure**
```
src/
├── api/           # API-Clients (axios-client, track-api, user-api)
├── hooks/         # Custom Hooks (useTracks, useUsers)
├── components/    # UI-Components (TrackList, TrackCard)
├── pages/         # Route-Components
└── types/         # TypeScript Interfaces
```

#### 2. **React Query für State Management**
- Automatisches Caching & Background Refetching
- Loading/Error States out-of-the-box
- Optimistic Updates
- Cache Invalidation nach Mutations

#### 3. **Custom Hooks**
- Wiederverwendbare Logik kapseln
- `useTracks()`, `useCreateTrack()`, `useDeleteTrack()`
- Separation of Concerns

#### 4. **React Router v6**
- Deklaratives Routing
- Nested Routes
- Lazy Loading mit `React.lazy()`

#### 5. **TypeScript Integration**
- Vollständige Typsicherheit
- IntelliSense für API-Responses
- Shared Types mit Backend-DTOs

### Key Features

✅ **Ultra-schnelles HMR** (Vite)  
✅ **React Query** für elegantes Data Fetching  
✅ **Hooks-basierte Architektur**  
✅ **Kleiner Bundle Size** durch Tree Shaking  
✅ **Flexible Library-Auswahl**

### Build & Deploy

```bash
# Production Build
npm run build
# Output: dist/
```
 
---

## 🔄 Frontend-Vergleich: Angular vs. React

| Aspekt | Angular | React/Vite |
|--------|---------|------------|
| **Lernkurve** | Steiler (opinionated) | Flacher (flexibler) |
| **TypeScript** | Default | Optional (hier verwendet) |
| **State Management** | RxJS / Signals | Context / Zustand / React Query |
| **Forms** | Reactive Forms (built-in) | React Hook Form (Library) |
| **Routing** | Angular Router (built-in) | React Router (Library) |
| **HTTP Client** | HttpClient (built-in) | Axios / Fetch (Library) |
| **Build Speed** | Langsamer | Sehr schnell (Vite) |
| **Bundle Size** | Größer | Kleiner (Tree Shaking) |
| **Dependency Injection** | Ja (built-in) | Nein (Context Pattern) |
| **Best For** | Enterprise Apps | Flexible SPAs |
 
---

### CORS-Konfiguration

```java
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins(
                        "http://localhost:4200",  // Angular
                        "http://localhost:5173"   // Vite/React
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

### Frontend starten (Beispiel Angular)
```bash
cd frontend
npm install
ng serve
```

---

## 💡 Entwicklungskonzepte

### Clean Code Prinzipien

#### 1. **Separation of Concerns**
- Controller kümmert sich nur um HTTP
- Service enthält Business Logic
- Repository nur für Datenzugriff

#### 2. **DTO Pattern**
- Keine Entities in API-Responses
- Validierung auf DTO-Ebene
- Mapping in Services

#### 3. **Constructor Injection**
```java
@Service
public class TrackService {
    private final TrackRepository trackRepository;
    
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }
}
```
Vorteile: Testbarkeit, Immutability, Explizite Dependencies

#### 4. **Optionals statt Nulls**
```java
public Optional<TrackResponseDTO> findTrackById(UUID id) {
    return trackRepository.findById(id)
        .map(this::mapToResponseDTO);
}
```

### Spring Boot Features

#### 1. **Spring Data JPA Magic**
```java
// Automatische Query-Generierung aus Methodennamen
List<Track> findByCreatorUsername(String username);
```

#### 2. **Transaktionale Services**
```java
@Transactional
public void updateTrack(UUID id, TrackUpdateDTO trackData) {
    // Änderungen werden automatisch committed
}
```

#### 3. **Exception Handling**
```java
// EntityNotFoundException → HTTP 404
// ResponseStatusException → Custom HTTP Status
```

### Testing Best Practices

#### AAA-Pattern (Arrange-Act-Assert)
```java
@Test
void uploadTrack_validInput_savesAndReturnsDTO() {
    // ARRANGE
    TrackCreateDTO createDTO = new TrackCreateDTO("...");
    when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
    
    // ACT
    TrackResponseDTO result = trackService.uploadTrack(createDTO);
    
    // ASSERT
    assertThat(result.title()).isEqualTo("chronostasis");
}
```

#### Test-Naming-Convention
```
methodName_condition_expectedResult
```
Beispiele:
- `findTrackById_exists_returnsDTO`
- `uploadTrack_userNotFound_throwsException`
- `createUser_emailAlreadyExists_throwsConflict`

---

## ⚠️ Bekannte Einschränkungen

1. **Keine Authentifizierung**: Aktuell keine Spring Security Integration
2. **Fehlende Validierung**: Keine umfassende Input-Sanitization
3. **Keine Paginierung**: `findAll` gibt alle Datensätze zurück
4. **Logging**: Rudimentär, keine strukturierten Logs
5. **Error Handling**: Keine globalen Exception Handler
6. **File Uploads**: Tracks enthalten keine echten Audio-Dateien

---

## 🗺 Roadmap

### Phase 1 (Aktuell)
- ✅ CRUD für Users & Tracks
- ✅ Basic Tests
- ✅ Flyway Migrations

### Phase 2 (Geplant)
- [ ] Spring Security (JWT-basiert)
- [ ] Global Exception Handling
- [ ] Paginierung & Sorting
- [ ] File Upload für Audio

### Phase 3 (Future)
- [ ] WebSocket für Real-time Updates
- [ ] Caching (Redis)
- [ ] Docker Deployment
- [ ] Keycloak User Management
- [ ] ML Tools integrieren

---

## 📚 Lernziele

Dieses Projekt demonstriert:

✅ **Spring Boot Basics**: Application Setup, Auto-Configuration  
✅ **REST API Design**: RESTful Endpoints, HTTP Methods, Status Codes  
✅ **JPA/Hibernate**: Entity Mapping, Relationships, Lazy Loading  
✅ **Testing**: Unit Tests (Mockito), Integration Tests (Testcontainers), Controller Tests (MockMvc)  
✅ **Database Migrations**: Flyway für Schema-Versionierung  
✅ **Clean Architecture**: Layered Design, DTO Pattern, Dependency Injection  
✅ **Build Tools**: Gradle, Dependency Management

---

## 🤝 Mitwirkende

Entwickelt von **alex** zu Lernzwecken.

---

## 📄 Lizenz

Dieses Projekt ist ein Lernprojekt ohne kommerzielle Nutzung.
