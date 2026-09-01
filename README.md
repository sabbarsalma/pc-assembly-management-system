# PC Assembly Management System

A role-based Android application for managing custom PC orders end to end, from request to assembly, built for SEG2505 (Introduction to Software Engineering, University of Ottawa) as a team project.

**Skills demonstrated:** Java, Android development, SQLite, UML/OOP design, concurrency debugging, unit testing (JUnit)

## Context

The app models a small internal PC-building service: employees submit requests for custom-built computers, a stock manager keeps the parts inventory, and an assembler builds and fulfills the orders. Rather than a generic CRUD app, the project's core challenge was designing a coherent multi-role workflow and data model that could evolve cleanly across five incremental deliverables, from an in-memory prototype to a fully persistent, tested application.

## Roles

The system supports four user roles, each with a distinct set of permissions and screens:

- **Administrator**: manages user accounts and can reset the database from a configuration file.
- **StoreKeeper**: manages the parts inventory (hardware and software components).
- **Assembler**: reviews incoming orders, checks component availability, and assembles or holds/rejects orders when stock is insufficient.
- **Requester**: creates orders by selecting compatible components from the available catalog.

## Architecture

The app is a standalone Android application (Java, Android Studio) with no client-server component, deliberately chosen to keep the solution simple, robust, and easy to deploy without network dependencies. Data is persisted locally with SQLite. The system evolved iteratively: an in-memory data model for the first deliverable was replaced with full SQLite persistence starting from the second, without changing the application's role-based structure.

The design was driven by UML modeling throughout: class diagrams for the role hierarchy and data structure, sequence diagrams for key flows like order creation and stock management, state diagrams for the order lifecycle (created, held, rejected, assembled), and activity diagrams for the assembly workflow.

## Key features

Administrators can reinitialize the entire dataset (users and stock) from an external file through a dedicated import screen, which made demos and testing far easier than manually recreating data each time. Order creation constrains component selection to pre-validated, compatible options, cutting down on invalid configurations by design rather than through validation after the fact. When stock runs short, the assembler can hold or reject an order instead of the system just failing, which reflects how this would actually need to work in practice. All input fields are validated with explicit error messages, and default role accounts are created with hashed (bcrypt) passwords rather than storing anything in plaintext in the database.

## Engineering decisions and lessons learned

Concurrent writes to the SQLite database initially caused data integrity issues when multiple actions touched stock and orders close together; this was resolved by introducing synchronized methods and ensuring writes were atomic. Unit tests were only introduced starting from the third deliverable, which meant several bugs surfaced late in development; in hindsight, testing from the first deliverable would have caught issues far earlier, and that's one of the clearest lessons from the project. The final deliverable had to withstand a "monkey test" (unpredictable, rapid user input), which pushed the team to add stricter input validation across every screen rather than only the common paths.

## Stack

Java, Android Studio, SQLite, bcrypt (`at.favre.lib:bcrypt`) for password hashing, OpenCSV for data import, JUnit for unit testing.

## Team

Built by a 3-person team as part of SEG2505. All three of us worked across the same parts of the codebase throughout the project rather than splitting strictly by role or layer.

## Running it

Open the project in Android Studio, let Gradle sync, and run on an emulator or device (minimum SDK per `app/build.gradle.kts`). Default role accounts (Administrator, StoreKeeper, Assembler) are created on first launch with placeholder demo credentials defined in `MemoryDataBaseHelper.java` / `SqliteDatabaseHelper.java` — change these before any real deployment.

## Repository structure

```
app/src/main/java/com/example/commande_pc/
├── LaunchActivity.java, LoginActivity.java, MainActivity.java, Utils.java
├── adapters/       # RecyclerView adapters
├── database/       # SQLite and in-memory data access
├── entity/         # Administrator, StoreKeeper, Assembler, Requester, Order, Item, ...
└── ui/             # Screens grouped by role (administrator/, storekeeper/, requester/, orders/, Assembler/)
```
