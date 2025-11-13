# Tricol-v2-SB: Supply Management API

This is the Spring Boot REST API for the Tricol company's supply chain management system. It provides a robust, layered backend for all procurement and inventory processes.

---

## 🚀 Core Functionalities

* **Supplier Management:** Full CRUD for all supplier information.
* **Product Management:** Full CRUD for products, including stock levels and reorder alerts.
* **Purchase Order Management:** Create, track, and receive supplier purchase orders.
* **Stock Management (FIFO):** Manages all stock entries and ensures all consumption is strictly **First-In, First-Out (FIFO)**.
* **Goods Issue Notes:** Handles internal requests for materials, triggering FIFO stock consumption upon validation.

---

## 🛠️ Tech Stack

* **Spring Boot:** Core framework
* **Spring Data JPA:** Database interaction
* **MySQL:** Relational database
* **Liquibase:** Database schema migration
* **MapStruct:** Entity/DTO mapping
* **Lombok:** Boilerplate code reduction
* **Maven:** Build management

---

## 🏛️ API Architecture

This application follows a strict layered architecture:

* **Controller**: Exposes API endpoints and handles HTTP requests/validation.
* **Service**: Contains all business logic (e.g., FIFO algorithm, calculations).
* **Repository**: Data access layer that communicates with the database via Spring Data JPA.
* **Entity**: Contains the JPA `@Entity` objects that map to database tables.

### DTOs and Mapping

To ensure security and a clear API contract, this project **never** exposes database entities.

* **DTOs (`dto`)**: We use **Data Transfer Objects** (e.g., `CreateSupplierDTO`) for all API communication.
* **Mappers (`mapper`)**: We use **MapStruct** to generate the high-performance mapping code that converts between DTOs and Entities.

---

### Prerequisites

* JDK 17 or higher
* Maven
* A running instance of MySQL
