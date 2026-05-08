# Issue: Singleton Pattern — Configuration & Logging System

## Description
Implement the Singleton pattern to guarantee a single global `Configuration` instance and a single `Logger` instance throughout the application. This is the first design pattern in the project.

## Objectives
- Create a thread-safe `Configuration` singleton that loads settings from a properties file
- Create a `Logger` singleton for centralized application logging
- Ensure both singletons are lazily initialized and thread-safe

## Tasks
- [ ] Create `util/Configuration.java` with:
  - Private static volatile instance field
  - Private constructor (loads `config.properties` from classpath)
  - Public static `getInstance()` method using double-checked locking
  - Methods: `get(String key)`, `set(String key, String value)`, `save()`
- [ ] Create `util/Logger.java` with:
  - Private static final instance (eager initialization acceptable)
  - Methods: `info(String)`, `warn(String)`, `error(String)`
  - Output to both console and a `logs/app.log` file
- [ ] Create `config.properties` with default values (e.g., `app.name=DocumentBuilder`, `export.default.format=pdf`)
- [ ] Write unit tests verifying:
  - `Configuration.getInstance()` returns the same instance across calls
  - `Logger.getInstance()` returns the same instance across calls
  - Thread-safety under concurrent access

## Acceptance Criteria
- Only one `Configuration` instance exists in any JVM runtime
- Only one `Logger` instance exists in any JVM runtime
- Configuration changes are reflected immediately across all consumers
- Logger output is consistently formatted and time-stamped

## Pattern Reference
- **Singleton** (Creational): Ensures a class has only one instance and provides a global point of access to it.

## Technical Notes
- Use `synchronized` block inside `getInstance()` (not on the method signature) for performance
- Consider `Enum` singleton approach for `Logger` as a simpler alternative (Joshua Bloch style)
- Configuration file location: `src/main/resources/config.properties`
