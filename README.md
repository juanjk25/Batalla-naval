# 📌 Batalla Naval - Figuras 2D

Batalla Naval es un juego de estrategia clásico desarrollado en Java con JavaFX, donde un jugador humano compite contra una inteligencia artificial. 
El objetivo es hundir la flota enemiga antes de que el oponente hunda la tuya. Cada jugador dispone de una flota de 10 barcos de distintos tamaños, posicionados 
estratégicamente en tableros de 10x10. El juego incluye interfaz gráfica interactiva, guardado automático, turnos dinámicos y un sistema de inteligencia artificial básico.

---

## 🚀 Tecnologías utilizadas
- Java 17 (JDK amazon coretto 17.0.17)
- Libreria JavaFX 17 (UI)

---

##  ⚙ Características
- ✔️ Modo de juego: Humano vs Máquina (IA)
- ✔️ Tableros diferenciados:
- ✔️ Tablero de Posición: Muestra la flota del jugador y los disparos recibidos.
- ✔️ Tablero Principal: Donde el jugador realiza sus disparos contra la flota enemiga.
- ✔️ Flota personalizable: 1 portaaviones (4 casillas), 2 submarinos (3 casillas), 3 destructores (2 casillas) y 4 fragatas (1 casilla).
- ✔️ Mecánicas de disparo: Agua (X), Tocado (marca parcial) y Hundido (barco completo).
- ✔️ Guardado automático: Serialización de tableros y archivos planos para nickname y estado.
- ✔️ Visualización del oponente: Opción para ver el tablero de la máquina (modo verificación).
- ✔️ Interfaz gráfica intuitiva: Desarrollada con JavaFX y Scene Builder (FXML).
- ✔️ Arquitectura robusta: MVC, principios SOLID, patrones de diseño y manejo de excepciones.

---

## 📦 Instalación

1. Requisitos:
   - Java SE 17 o superior
   - JavaFX SDK
   - IntelliJ IDEA (recomendado) o cualquier IDE compatible con Java
   - Scene Builder (para edición de interfaces FXML)
2. Pasos:
   - Clonar el repositorio del proyecto.
   - Importar el proyecto en IntelliJ IDEA como proyecto Maven o Gradle (según configuración).
   - Configurar el SDK de JavaFX en el IDE.
   - Ejecutar la clase principal App ubicada en src/main/java/org/example/batalia_naval_re/view/App.java.


## 🛠 Estructura del programa (Arquitectura MVC)
```bash
src/main/java/org/example/batalla_naval_re/
├── ai/                    # Lógica de IA
│   └── SimpleAI.java
├── controller/            # Controladores
│   ├── GameController.java
│   ├── IGameController.java
|   ├── IMainController.java
│   └── MainController.java
├── exception/             # Excepciones personalizadas
│   └── PlacementException.java
├── model/                 # Modelos del dominio
│   ├── Board.java
│   ├── Cell.java
│   ├── GameState.java
│   ├── Player.java
│   ├── Ship.java
│   ├── ShipType.java
│   └── interfaces/       (iBoard, iCell, iGameState, iShip, iShipType)
├── persistence/           # Persistencia de datos
│   └── url/
├──  view/
|   └── renderer/
|   |   ├── BoardRenderer.java
|   └── shapes/
|       ├── CarrierShape.java
|       ├── DestroyerShape.java
|       ├── FrigateShape.java
|       ├── IShipShape.java
|       ├── ShipShape.java
|       ├── ShipShapeFactory.java
|       └── SubmarineShape.java
└── resources (imágenes, estilos, archivos FXML)

```

## 👀 Vista del juego 
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/7c692f98-d3e6-4e27-b61a-fc9d155ba29c" />


## 👤 Autores

1. Nombre: Oscar Andrés Rengifo Bustos   
   GitHub: TheRoscar   
   Correo: oscar.andres.rengifo@correounivalle.edu.co

2. Nombre: Juan David López Jiménez   
   Github: juanjk25   
   Correo: juan.lopez.jimenez@correounivalle.edu.co   

3. Nombre: Jarrison Daniel Caicedo Pascuaza   
   GitHub: Jarrison001   
   Correo: jarrison.caicedo@correounivalle.edu.co

4. Nombre: Hugo Alexander Eraso   
   GitHub: ningagamer1
   Correo: hugo.eraso@correounivalle.edu.co


 
