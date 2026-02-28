---
apply: always
---

# Agent Development Guidelines

## General Principles

- Do not include comments in code.
- Apply Clean Architecture design principles:
  - SRP (Single Responsibility Principle)
  - OCP (Open/Closed Principle)
  - LSP (Liskov Substitution Principle)
  - ISP (Interface Segregation Principle)
  - DIP (Dependency Inversion Principle)
- Organize classes by common domain to ensure high cohesion.
- Follow a simple Onion Architecture combined with Domain-Driven Design (DDD).
- Tests must follow the **Given–When–Then** pattern and the name should follow the same standard (given_when_then).
- For tests, include a `TestContext` class in each test file responsible for setting up the test context to avoid repetition across tests.
- For tests, the tested class variable should be called underTest

---

## Java Guidelines

- Use Java 25 standard.
- Use **jMolecules annotations** to express DDD and Onion Architecture concepts.
- Always verify the project by compiling with:
  - `mvn clean install`
- Always format code at the end with:
  - `mvn spring-javaformat:apply`
- Use `Optional` as a return type when a value may be null.
- Throw exceptions for system errors.
- For unchecked exceptions:
  - Ensure they are handled in the global exception handler.
- If an error status is recoverable:
  - Use checked exceptions.
- Use **Mockito** to mock services and external dependencies in tests.

---

## Angular Guidelines

- Use Angular 21 latest standards.
- Use signals only for UI interaction.
- Do not use signals for backend service logic.
- Never use `any`.
- Always specify types explicitly.
- `undefined` may be used where appropriate.
