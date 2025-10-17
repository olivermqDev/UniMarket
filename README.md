<h1 align="center">📱 UniMarket</h1>
<p align="center">
  <b>Aplicación móvil Marketplace para universitarios con integración de Firebase</b>  
  <br>Desarrollado en Kotlin + Jetpack Compose · Firebase Auth · Firestore · Storage
</p>

---

<p align="center">
  <img src="https://miro.medium.com/v2/1*r0hDPKC9AEEyU4bTxZY5MQ.png"/>
</p>

---

### 🚀 Descripción del proyecto
**UniMarket** es una aplicación Android tipo **Marketplace universitario**, que permite a los estudiantes:
- Registrarse e iniciar sesión (Firebase Authentication)
- Publicar, editar y eliminar productos (CRUD con Firestore + Storage)
- Buscar por categoría, precio o nombre
- Administrar su perfil con foto y datos personales
- Gestionar sus publicaciones y ventas en tiempo real

Desarrollada con un enfoque **modular y escalable** bajo arquitectura **MVVM + Jetpack Compose**.

---

### 🧠 Tecnologías principales
| Componente | Tecnología |
|-------------|-------------|
| Lenguaje | Kotlin |
| UI Toolkit | Jetpack Compose + Material 3 |
| Backend | Firebase (Auth, Firestore, Storage, Realtime Database) |
| Inyección de dependencias | Koin |
| Imágenes | Coil Compose |
| Control de versiones | Git + GitHub |

---

### 🏗️ Estructura del Proyecto

- app/
- ├── data/
- │ ├── model/ # Modelos de datos (User, Producto)
- │ ├── repository/ # Repositorios Firebase
- │
- ├── viewmodel/ # Lógica de negocio (MVVM)
- ├── iu/screens/ # Pantallas Compose
- ├── navigation/ # Sistema de rutas
- ├── ui/theme/ # Colores y tipografía
- └── MainActivity.kt # Punto de entrada



---

### 🧩 Funcionalidades Sprint 1 (implementadas ✅)
- 🔐 Login y registro de usuario con Firebase Authentication  
- 🧍 Gestión de perfil con foto de usuario (Firestore + Storage)  
- 🛒 CRUD completo de productos (crear, editar, listar, eliminar)  
- 🗂️ Catálogo con filtros por categoría y precio  

---

### 📅 Roadmap
| Sprint | Objetivos | Estado |
|--------|------------|--------|
| 1️⃣ | Autenticación y CRUD básico | ✅ Completado |
| 2️⃣ | Chat en tiempo real + chatbot IA | 🟡 En desarrollo |
| 3️⃣ | Valoraciones + geolocalización | 🔜 Pendiente |

---

### 👨‍💻 Autores
| Nombre | Rol |
|--------|-----|
| **Romero Velásquez Diego Aldair** | Android Developer · Firebase Integration |
| **Mitma Quino Oliver Alexander** | Backend & UX/UI Designer . Módulo de Productos|
| **Mario Sánchez Mirones** | Frontend Developer · Módulo de Usuarios (Auth & Profile) |

---
