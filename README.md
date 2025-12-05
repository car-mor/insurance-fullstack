# 🛡️ OpenPolicy - Core Insurance System

> Un sistema de gestión de pólizas de seguros simplificado, inspirado en la lógica de negocio de **Guidewire PolicyCenter**.

![Status](https://img.shields.io/badge/STATUS-EN_DESARROLLO-orange?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

## 📖 Descripción

**OpenPolicy** es una solución Fullstack diseñada para simular el ciclo de vida básico de una póliza de seguros. El proyecto implementa una arquitectura **Headless** moderna, separando la lógica de negocio (Backend) de la capa de presentación (Frontend).

El objetivo es demostrar prácticas de desarrollo "Enterprise" como:
* Patrón **DTO** (Request/Response) para seguridad de datos.
* Validaciones estrictas (Backend & Frontend).
* Manejo transaccional de bases de datos.
* Documentación de API con OpenAPI (Swagger).

## 🏗️ Arquitectura del Proyecto

El repositorio está estructurado como un Monorepo:

```text
open-policy-system/
├── core-system/       # 🧠 Backend (Spring Boot + H2 + JDBC)
└── frontend-ui/       # 💻 Frontend (React + Vite + Bootstrap)
