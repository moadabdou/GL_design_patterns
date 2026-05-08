# Issue: Project Setup & Build Configuration

## Description
Establish the Java project skeleton with build automation, directory structure, and dependency management. This issue lays the groundwork for all subsequent development.

## Objectives
- Initialize a Java project with Maven/Gradle build system
- Define the standard directory layout (src/main/java, src/test/java)
- Configure dependencies (JUnit 5, a mock 3rd-party PDF library for Adapter pattern)
- Set up a basic logging framework (java.util.logging or SLF4J)

## Tasks
- [ ] Choose build system (Gradle recommended for simplicity)
- [ ] Create `build.gradle` with Java plugin and required dependencies
- [ ] Set up directory structure: `domain/`, `creational/`, `structural/`, `behavioral/`, `facade/`, `util/` packages
- [ ] Configure `.gitignore` for IDE files, build outputs
- [ ] Create a `Main.java` placeholder class with a `main()` method
- [ ] Verify build compiles with `./gradlew build`

## Acceptance Criteria
- `./gradlew build` completes without errors
- Project structure follows standard Java conventions
- Dependencies for JUnit 5 and PDF library are declared
- A runnable `Main.main()` exists

## Technical Notes
- Java version: 17 or 21 (LTS)
- Mock PDF library: create a simple `com.thirdparty.pdf.PDFGenerator` class inside the project to simulate a 3rd-party API (to be adapted later)
- Build output directory: `build/`
