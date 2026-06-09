# Sistema Eléctrico Nacional — SubÁrea Santander

> Aplicación de escritorio en Java para gestionar las líneas de transmisión eléctrica de la SubÁrea Santander del Sistema Interconectado Nacional de Colombia.  
> Desarrollada con **Java Swing**, arquitectura **MVC**, mapa interactivo con **JXMapViewer** y datos reales de **PARATEC - XM**.

**Autores:** Andres Ramos · Manuel Amell

---

## Tabla de Contenidos

1. [Resumen del Proyecto](#resumen-del-proyecto)
2. [Cumplimiento según la rúbrica](#cumplimiento-según-la-rúbrica)
3. [Entregables del PDF](#entregables-del-pdf)
4. [Tecnologías Utilizadas](#tecnologías-utilizadas)
5. [Arquitectura MVC](#arquitectura-mvc)
6. [Estructura de Carpetas](#estructura-de-carpetas)
7. [Capa Modelo](#capa-modelo)
8. [Capa Persistencia](#capa-persistencia)
9. [Capa Controlador](#capa-controlador)
10. [Capa Vista](#capa-vista)
11. [Datos del Proyecto](#datos-del-proyecto)
12. [Errores Comunes](#errores-comunes-y-soluciones)

---

## Resumen del Proyecto

Esta aplicación permite gestionar las **52 líneas de transmisión reales** de la SubÁrea Santander registradas en PARATEC. Implementa un CRUD-S completo con interfaz gráfica Swing y visualización geográfica sobre un mapa real de OpenStreetMap.

### ¿Qué hace la aplicación?

| Funcionalidad | Descripción |
|---|---|
| **Crear** | Formulario para registrar nuevas líneas con validación automática |
| **Leer** | Tabla con todas las líneas y sus atributos técnicos |
| **Editar** | Modificar cualquier línea existente (doble clic o botón) |
| **Eliminar** | Borrar líneas con confirmación |
| **Buscar** | Búsqueda en tiempo real por nombre, municipio o subestación |
| **Filtrar** | Por nivel de voltaje (115 / 230 / 500 kV) y por operador *(el PDF sugiere filtro por departamento; ver tabla de pendientes)* |
| **Guardar CSV** | Exportar el estado actual a un archivo `.csv` |
| **Abrir CSV** | Importar líneas desde un archivo `.csv` |
| **Mapa** | Visualizar subestaciones y líneas sobre mapa real de Santander |
| **Estadísticas** | Capacidad total en MW, longitud total en km, operadores activos |

---

## Cumplimiento según la rúbrica

Los requisitos de este proyecto provienen del documento [`ProyectoFinal_LineasTransmision.pdf`](ProyectoFinal_LineasTransmision.pdf) (*Caso de Estudio: Gestión de Líneas de Transmisión del Sistema Eléctrico Nacional — POO*). El PDF permite elegir entre varias regiones; **esta implementación cubre la SubÁrea Santander**.

### Criterios de evaluación (referencia del PDF)

| Criterio | Ponderación |
|---|---|
| Diseño orientado a objetos (encapsulamiento, relaciones, UML) | 25% |
| Implementación en Java (programación y estructura) | 25% |
| Interfaz gráfica (CRUD-S funcional) | 20% |
| Persistencia (abrir y guardar archivos) | 20% |
| Extensión (mapa o visualización adicional) | 10% |

### Requisitos cumplidos

| Criterio / Requisito (PDF) | Estado | Evidencia en el repo |
|---|---|---|
| **Diseño POO (25%)** — Encapsulamiento con atributos privados, getters y setters | Cumplido | [`LineaTransmision.java`](SistemaElectricoSantander/src/modelo/LineaTransmision.java), [`Subestacion.java`](SistemaElectricoSantander/src/modelo/Subestacion.java) |
| Clases de dominio: línea, subestación, sistema | Cumplido | [`SistemaElectrico.java`](SistemaElectricoSantander/src/modelo/SistemaElectrico.java) |
| Método de negocio `calcularCapacidadMW` con pf = 0.95 | Cumplido | `LineaTransmision.calcularCapacidadMW` |
| Arquitectura MVC (modelo, vista, controlador, persistencia) | Cumplido | Paquetes `modelo/`, `vista/`, `controlador/`, `persistencia/` |
| **Implementación Java (25%)** — Proyecto NetBeans/Ant | Cumplido | [`build.xml`](SistemaElectricoSantander/build.xml), [`nbproject/`](SistemaElectricoSantander/nbproject/) |
| Estructura de paquetes según Fase 2 del PDF | Cumplido | 9 clases Java en `src/` |
| Datos reales de PARATEC | Cumplido | [`data/lineas.csv`](SistemaElectricoSantander/data/lineas.csv), [`data/subestaciones.csv`](SistemaElectricoSantander/data/subestaciones.csv) |
| **Interfaz gráfica (20%)** — CRUD completo de líneas | Cumplido | [`VentanaPrincipal.java`](SistemaElectricoSantander/src/vista/VentanaPrincipal.java), [`DialogoLinea.java`](SistemaElectricoSantander/src/vista/DialogoLinea.java) |
| Buscar (CRUD-S) | Cumplido | Campo de búsqueda + `SistemaElectrico.buscarLineas` |
| Validación de campos vacíos o negativos | Parcialmente cumplido | [`Controlador.java`](SistemaElectricoSantander/src/controlador/Controlador.java) valida nombre, operador (solo al crear), voltaje, corriente y longitud |
| **Persistencia (20%)** — Abrir y guardar CSV | Cumplido | [`GestorCSV.java`](SistemaElectricoSantander/src/persistencia/GestorCSV.java) + botones en la barra de herramientas |
| Parser CSV con campos entre comillas | Cumplido | `GestorCSV.parsearCSV` |
| **Extensión mapa (10%)** — Visualización geográfica | Cumplido | [`PanelMapa.java`](SistemaElectricoSantander/src/vista/PanelMapa.java) con JXMapViewer + OpenStreetMap |
| Filtro por voltaje (Fase 4) | Cumplido | Combo en `VentanaPrincipal` |
| Capacidad total del sistema (Fase 4) | Parcialmente cumplido | `calcularCapacidadTotal()` + barra de estado y diálogo de estadísticas (resumen, no simulación) |

### Requisitos pendientes, incorrectos o incompletos

| Criterio / Requisito | Problema | Impacto | Acción sugerida |
|---|---|---|---|
| **Entregable 1** — Diagrama UML (`.png` / `.uml`) | No existe en el repositorio | Afecta criterio de diseño POO (25%) | Crear diagrama de clases y versionarlo en el repo |
| **Entregable 2** — Proyecto NetBeans (`.zip`) | El código fuente existe, pero no hay `.zip` de entrega ni JAR en `dist/` | Entregable formal incompleto | Empaquetar el proyecto y generar el JAR ejecutable |
| **Entregable 3** — Informe breve (`.pdf`, opcional) | No incluido | Recomendado para documentar el diseño | Redactar informe con capturas de pantalla |
| **Entregable 4** — Datos fuente (`.xlsx` de PARATEC) | Solo hay CSV derivado | Formato de entrega distinto al solicitado | Adjuntar el `.xlsx` original exportado de PARATEC |
| Clase `Conexion` en modelo UML (objetivo específico 1) | No existe; la conexión es implícita vía strings origen/destino | Relaciones UML incompletas | Documentar en el diagrama UML o agregar enlace explícito |
| Persistencia JSON (Fase 3) | Solo CSV implementado | Requisito alternativo no cubierto | Implementar JSON o documentar CSV como elección justificada |
| Filtro por **departamento** (Fase 4.2) | No existe `filtrarPorDepartamento`; hay filtro por **operador** | Extensión parcial respecto al PDF | Añadir filtro por departamento |
| Simulación de carga (Fase 4.3) | Solo estadísticas agregadas (MW/km totales), sin simulación | Extensión parcial | Implementar simulador de carga o renombrar como resumen de capacidad |
| Validación completa (Fase 3) | `actualizarLinea` no valida operador vacío; no valida subestaciones existentes; posible NPE si campos CSV son `null` en búsqueda | Riesgo de datos inconsistentes | Endurecer validaciones en el controlador |
| CRUD de subestaciones | Solo lectura desde CSV; `guardarSubestaciones` sin exponer en UI/controlador | Fuera del mínimo, pero incompleto | Opcional: CRUD o guardado de subestaciones |
| Dependencias JAR (`lib/`) | La carpeta `lib/` **no existe** en el repo; [`project.properties`](SistemaElectricoSantander/nbproject/project.properties) apunta a rutas Windows absolutas | Proyecto no portable (falla en Linux) | Crear `lib/`, versionar JARs y actualizar el classpath |
| Versión Java documentada | El README anterior decía "17+"; el proyecto compila con **Java 26** | Documentación desalineada con el código | Usar JDK 26 según `project.properties` |
| Mapa vs filtros | El mapa muestra **todas** las líneas aunque la tabla esté filtrada | UX inconsistente | Sincronizar el mapa con los filtros activos |
| Estadísticas vs filtros | La barra inferior muestra totales globales, no del subconjunto filtrado | Estadísticas engañosas al filtrar | Calcular sobre la lista visible |
| Combo de operadores | No se refresca tras "Abrir CSV" | Filtro desactualizado | Repoblar el combo al cargar un archivo |

---

## Entregables del PDF

| # | Entregable | Formato requerido | Estado en el repo |
|---|---|---|---|
| 1 | Modelo UML (clases, relaciones, atributos, métodos) | `.png` o `.uml` | **Pendiente** — no incluido |
| 2 | Proyecto NetBeans funcional con GUI y CRUD-S | `.zip` | **Parcial** — código en [`SistemaElectricoSantander/`](SistemaElectricoSantander/), sin zip de entrega |
| 3 | Informe breve con diseño y capturas | `.pdf` (opcional) | **Pendiente** — no incluido |
| 4 | Datos técnicos de líneas de transmisión (PARATEC) | `.xlsx` | **Parcial** — datos en CSV en [`data/`](SistemaElectricoSantander/data/), sin `.xlsx` original |

---

## Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 26 | Lenguaje principal (según `nbproject/project.properties`) |
| Java Swing | JDK built-in | Interfaz gráfica (GUI) |
| JXMapViewer2 | 2.6 | Mapa geográfico con OpenStreetMap |
| commons-logging | 1.2 | Dependencia requerida por JXMapViewer |
| NetBeans IDE | 17+ | Entorno de desarrollo |
| CSV | — | Formato de persistencia de datos |
| PARATEC - XM | — | Fuente de datos oficiales |

> **Dependencias externas:** Los JAR de JXMapViewer y commons-logging deben estar en el classpath. Actualmente [`project.properties`](SistemaElectricoSantander/nbproject/project.properties) referencia rutas absolutas de Windows (`C:\Users\RYZEN\Downloads\...`). Para portabilidad, crear la carpeta `lib/`, copiar los JAR allí y actualizar el classpath en NetBeans.

---

## Arquitectura MVC

El proyecto sigue el patrón **Modelo - Vista - Controlador**, que separa claramente cada responsabilidad:

```
┌─────────────────────────────────────────────────────────┐
│                        VISTA                            │
│  VentanaPrincipal  ←→  DialogoLinea  ←→  PanelMapa     │
│         │                                               │
│         │ llama métodos                                 │
│         ▼                                               │
│                     CONTROLADOR                         │
│                   Controlador.java                      │
│         │                                               │
│         │ accede y modifica                             │
│         ▼                                               │
│                       MODELO                            │
│  LineaTransmision  Subestacion  SistemaElectrico        │
│                       │                                 │
│                       │ lee y escribe                   │
│                       ▼                                 │
│                   PERSISTENCIA                          │
│                   GestorCSV.java                        │
│                       │                                 │
│                       ▼                                 │
│              lineas.csv / subestaciones.csv             │
└─────────────────────────────────────────────────────────┘
```

> **Regla clave:** La Vista **nunca** accede directamente al Modelo. Siempre pasa por el Controlador.

---

## Estructura de Carpetas

```
SistemaElectricoSantander/
├── src/
│   ├── Main.java                        ← Punto de entrada de la aplicación
│   ├── modelo/
│   │   ├── LineaTransmision.java        ← Entidad principal del dominio
│   │   ├── Subestacion.java             ← Nodo geográfico del sistema
│   │   └── SistemaElectrico.java        ← Gestor de colecciones y lógica
│   ├── persistencia/
│   │   └── GestorCSV.java               ← Lectura y escritura de archivos CSV
│   ├── vista/
│   │   ├── VentanaPrincipal.java        ← Ventana principal con tabla y mapa
│   │   ├── DialogoLinea.java            ← Formulario modal crear / editar
│   │   └── PanelMapa.java               ← Panel de mapa con JXMapViewer
│   └── controlador/
│       └── Controlador.java             ← CRUD-S, validaciones, filtros
├── data/
│   ├── lineas.csv                       ← 52 líneas reales de Santander
│   └── subestaciones.csv                ← 34 subestaciones con coordenadas
├── lib/                                 ← Pendiente: no existe en el repo actual
│   ├── jxmapviewer2-2.6.jar             ← Crear carpeta y agregar al Classpath
│   └── commons-logging-1.2.jar          ← Dependencia requerida por JXMapViewer
├── nbproject/                           ← Configuración del proyecto NetBeans
├── build.xml                            ← Script de compilación Ant
└── dist/                                ← Salida del JAR (generar con Clean and Build)
```

> La carpeta `lib/` está documentada como ubicación recomendada para los JAR, pero **aún no está versionada** en el repositorio. Ver [Errores Comunes](#errores-comunes-y-soluciones) para configurar las dependencias.

---

## Capa Modelo

### `LineaTransmision.java`

Es la **entidad principal** del proyecto. Representa una línea de transmisión eléctrica con todos sus atributos técnicos.

**Atributos:**

| Atributo | Tipo | Descripción |
|---|---|---|
| `id` | `int` | Identificador único autoasignado |
| `nombre` | `String` | Nombre oficial (ej: `BARRANCA - BUENAVISTA 1 115 kV`) |
| `operador` | `String` | Empresa responsable (ESSA, ISA, CELSIA, etc.) |
| `estado` | `String` | `Operación`, `Fuera de servicio`, `Construcción` |
| `subestacionOrigen` | `String` | Subestación donde inicia la línea |
| `subestacionDestino` | `String` | Subestación donde termina la línea |
| `voltajeNominalKV` | `double` | Voltaje en kV (115, 230 ó 500) |
| `corrienteNominalA` | `double` | Corriente nominal en amperios |
| `capacidadMW` | `double` | Calculado automáticamente en MW |
| `longitudKm` | `double` | Longitud total de la línea en km |
| `municipio` | `String` | Municipio de ubicación |
| `departamento` | `String` | Departamento (Santander / Norte de Santander) |

**Método clave — Cálculo de capacidad MW:**

```java
public double calcularCapacidadMW(double voltajeKV, double corrienteA, double pf) {
    return Math.sqrt(3) * voltajeKV * corrienteA / 1000.0 * pf;
}
```

La fórmula trifásica es:

```
MW = √3 × V_kV × I_A / 1000 × pf
```

Donde `pf = 0.95` (factor de potencia por defecto).

**Ejemplo:**
```
Línea 115 kV, 530 A:
MW = 1.732 × 115 × 530 / 1000 × 0.95 = 100.29 MW
```

> **Importante:** Los setters de `voltajeNominalKV` y `corrienteNominalA` recalculan `capacidadMW` automáticamente al ser llamados, manteniendo siempre la consistencia del dato.

---

### `Subestacion.java`

Representa un **nodo geográfico** del sistema eléctrico. Es la entidad que aparece como círculo en el mapa.

| Atributo | Tipo | Descripción |
|---|---|---|
| `id` | `int` | Identificador único |
| `nombre` | `String` | Nombre de la subestación (ej: `BUCARAMANGA`, `PALENQUE`) |
| `municipio` | `String` | Municipio donde está ubicada |
| `departamento` | `String` | Departamento correspondiente |
| `latitud` | `double` | Coordenada geográfica latitud (grados decimales) |
| `longitud` | `double` | Coordenada geográfica longitud (negativa en Colombia) |

---

### `SistemaElectrico.java`

Es el **gestor central del dominio**. Contiene las listas de líneas y subestaciones y expone toda la lógica de negocio. No tiene ninguna dependencia de Swing ni de archivos.

**Métodos principales:**

```java
// CRUD
void agregarLinea(LineaTransmision linea)       // Agrega con id autoincremental
boolean eliminarLinea(int id)                   // true si eliminó, false si no existe
boolean actualizarLinea(LineaTransmision l)     // Reemplaza por id

// Búsqueda y filtros
List<LineaTransmision> buscarLineas(String texto)         // Busca en nombre, municipio, subestaciones
List<LineaTransmision> filtrarPorVoltaje(double kv)       // Solo líneas del voltaje dado
List<LineaTransmision> filtrarPorOperador(String op)      // Solo líneas del operador dado

// Estadísticas
double calcularCapacidadTotal()    // Suma de MW de todas las líneas
double calcularLongitudTotal()     // Suma de km de todas las líneas
List<String> getOperadoresUnicos() // Lista sin repetición de operadores
List<Double> getVoltajesUnicos()   // Lista sin repetición de voltajes
```

---

## Capa Persistencia

### `GestorCSV.java`

Maneja la **lectura y escritura de archivos CSV** con un parser propio que soporta campos con comas internas (entre comillas).

**Formato del archivo `lineas.csv`:**

```csv
nombre,operador,estado,fechaPuestaOperacion,tipoUso,subestacionOrigen,
subestacionDestino,voltajeNominalKV,corrienteNominalA,capacidadMW,
longitudKm,municipio,departamento,subarea

BARRANCA - BUENAVISTA 1 115 kV,ELECTRIFICADORA DE SANTANDER S.A. E.S.P.,
Operación,16/04/2018,Uso STR,BUENAVISTA,BARRANCA,115.0,530.0,100.29,
12.12,BARRANCABERMEJA,SANTANDER,SubArea Santander
```

**Formato del archivo `subestaciones.csv`:**

```csv
id,nombre,municipio,departamento,latitud,longitud
1,BARRANCA,BARRANCABERMEJA,SANTANDER,7.0650,-73.8547
9,BUCARAMANGA,BUCARAMANGA,SANTANDER,7.1193,-73.1227
```

**Características del parser:**

```java
private String[] parsearCSV(String linea) {
    // Recorre caracter por caracter
    // Maneja campos entre comillas dobles (pueden contener comas internas)
    // Soporta comillas escapadas ("")
    // Elimina espacios al inicio/final de cada campo
}
```

**Detección automática de columna `id`:**

```java
// Detecta si el CSV tiene o no columna id en la primera columna
boolean tieneId = header[0].equalsIgnoreCase("id");
int offset = tieneId ? 1 : 0;
```

Esto permite cargar tanto el CSV original de PARATEC (sin `id`) como los CSVs guardados por la app (con `id`).

---

## Capa Controlador

### `Controlador.java`

Es el **puente entre la Vista y el Modelo**. Centraliza todas las validaciones y operaciones. La Vista nunca llama métodos del Modelo directamente.

**Validaciones implementadas:**

```java
if (nombre.trim().isEmpty())  return "ERROR: El nombre no puede estar vacío.";
if (voltaje <= 0)             return "ERROR: El voltaje debe ser mayor a 0.";
if (corriente <= 0)           return "ERROR: La corriente debe ser mayor a 0.";
if (longitud <= 0)            return "ERROR: La longitud debe ser mayor a 0.";
// Si todo OK:
return "OK";
```

**Métodos principales:**

```java
// CRUD-S
String agregarLinea(String nombre, String operador, ...)  // Valida y agrega
String actualizarLinea(int id, String nombre, ...)        // Valida y actualiza
String eliminarLinea(int id)                              // Elimina por id
List<LineaTransmision> obtenerTodasLineas()               // Sin filtros
List<LineaTransmision> buscarLineas(String texto)         // Búsqueda
List<LineaTransmision> filtrarPorVoltaje(double kv)       // Filtro voltaje
List<LineaTransmision> filtrarPorOperador(String op)      // Filtro operador

// Persistencia
String guardarLineas(String ruta)    // Guarda CSV en la ruta dada
String cargarLineas(String ruta)     // Carga CSV desde la ruta dada
void cargarDatosIniciales(String rutaLineas, String rutaSubs) // Al arrancar

// Estadísticas
double getCapacidadTotal()
double getLongitudTotal()
int getTotalLineas()
List<String> getOperadores()
List<Double> getVoltajes()
```

---

## Capa Vista

### `VentanaPrincipal.java`

Es la **ventana principal** de la aplicación. Organiza todos los componentes en un `JSplitPane` horizontal (tabla a la izquierda, mapa a la derecha).

**Componentes:**

| Componente | Tipo Swing | Función |
|---|---|---|
| Barra superior | `JToolBar` | Botones Nueva, Editar, Eliminar, Guardar CSV, Abrir CSV |
| Menú | `JMenuBar` | Archivo, Líneas, Herramientas |
| Campo de búsqueda | `JTextField` | Filtra en tiempo real al escribir |
| Filtro voltaje | `JComboBox` | Filtra por nivel de kV |
| Filtro operador | `JComboBox` | Filtra por empresa operadora |
| Tabla | `JTable` + `DefaultTableModel` | Muestra las líneas con 11 columnas |
| Ordenamiento | `TableRowSorter` | Ordena al hacer clic en encabezados |
| Panel mapa | `PanelMapa` | Visualización geográfica interactiva |
| Barra de estado | `JPanel` | Muestra líneas totales, MW y km |

**Flujo de actualización de tabla:**

```
Usuario escribe / cambia filtro / hace CRUD
         ↓
    actualizarTabla()
         ↓
    Obtiene texto de búsqueda y filtros
         ↓
    Llama al Controlador
         ↓
    Limpia tabla (setRowCount(0))
         ↓
    Repuebla con resultados
         ↓
    Actualiza barra de estado
         ↓
    Llama a actualizarMapa()
```

---

### `DialogoLinea.java`

Es el **formulario modal** para crear o editar una línea. Bloquea la ventana principal hasta que el usuario confirme o cancele.

**Características:**

- Al **crear**: todos los campos vacíos
- Al **editar**: campos pre-cargados con `cargarDatos(LineaTransmision)`
- El campo **Capacidad (MW)** es de solo lectura y se recalcula en tiempo real al cambiar voltaje o corriente:

```java
// Listener que actualiza MW en tiempo real
txtCorriente.getDocument().addDocumentListener(new DocumentListener() {
    public void insertUpdate(DocumentEvent e) { actualizarCapacidad(); }
    public void removeUpdate(DocumentEvent e) { actualizarCapacidad(); }
    public void changedUpdate(DocumentEvent e) { actualizarCapacidad(); }
});
```

- `isConfirmado()` retorna `true` si el usuario hizo clic en Guardar, `false` si canceló

---

### `PanelMapa.java`

Contiene el **mapa geográfico interactivo** con JXMapViewer. Dibuja dos capas (painters) sobre el mapa base de OpenStreetMap.

**Cómo funciona JXMapViewer:**

```java
// 1. Configurar la fuente de tiles (imágenes del mapa)
TileFactoryInfo info = new TileFactoryInfo(...) {
    public String getTileUrl(int x, int y, int zoom) {
        return "https://tile.openstreetmap.org/" + z + "/" + x + "/" + y + ".png";
    }
};
DefaultTileFactory tileFactory = new DefaultTileFactory(info);
tileFactory.setUserAgent("Mozilla/5.0 SistemaElectricoSantander/1.0");
mapViewer.setTileFactory(tileFactory);

// 2. Centrar en Santander, Colombia
mapViewer.setAddressLocation(new GeoPosition(7.0, -73.2));
mapViewer.setZoom(7);
```

**Painters (capas de dibujo):**

```java
// Painter de líneas: conecta subestaciones con líneas coloreadas
painters.add((g, map, w, h) -> {
    for (LineaTransmision l : lineas) {
        Point2D p1 = map.convertGeoPositionToPoint(new GeoPosition(orig.getLatitud(), ...));
        Point2D p2 = map.convertGeoPositionToPoint(new GeoPosition(dest.getLatitud(), ...));
        g.setColor(COLORES_VOLTAJE.get(l.getVoltajeNominalKV()));
        g.draw(new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
    }
});

// Painter de subestaciones: círculo morado en cada nodo
painters.add((g, map, w, h) -> {
    for (Subestacion s : subestaciones) {
        Point2D p = map.convertGeoPositionToPoint(new GeoPosition(s.getLatitud(), ...));
        g.fill(new Ellipse2D.Double(px - r, py - r, r * 2, r * 2));
    }
});

mapViewer.setOverlayPainter(new CompoundPainter<>(painters));
```

**Colores por nivel de voltaje:**

| Color | Voltaje | Tipo de red |
|---|---|---|
| 🟢 Verde | 115 kV | STR — distribución regional |
| 🔵 Azul | 230 kV | STN — transporte nacional |
| 🔴 Rojo | 500 kV | STN — troncales de alta tensión |
| 🟣 Morado | — | Subestaciones (nodos) |

> **Nota:** `convertGeoPositionToPoint()` retorna `Point2D` (no `Point`) en JXMapViewer 2.6. Por eso se usa `.getX()` y `.getY()` en lugar de `.x` y `.y`.

---

## 📊 Datos del Proyecto

### Origen de los datos

Los datos técnicos provienen de **[PARATEC - XM](https://paractec.xm.com.co)**, la plataforma oficial del operador del mercado eléctrico colombiano. Para obtenerlos:

1. Ir a la sección **Transmisión → Detalle de líneas**
2. Filtrar por **Subárea operativa: Santander**
3. Filtrar por **Estado: Operación**
4. Exportar el resultado

### Resumen de datos incluidos

| Dato | Cantidad |
|---|---|
| Líneas de transmisión | 52 |
| Subestaciones | 34 |
| Voltajes presentes | 115 kV, 230 kV, 500 kV |
| Operadores | 5 (ESSA, ISA INTERCOLOMBIA, CELSIA, PCH SAN BARTOLOME, AMB) |

### Operadores del sistema

| Operador | Descripción |
|---|---|
| ESSA | Electrificadora de Santander S.A. E.S.P. — operador regional principal |
| ISA INTERCOLOMBIA | ISA Intercolombia S.A. E.S.P. — operador nacional STN |
| CELSIA | Celsia Colombia S.A. E.S.P. — operador de generación |
| PCH SAN BARTOLOME | PCH San Bartolomé S.A.S. E.S.P. — pequeña central hidráulica |
| AMB | Acueducto Metropolitano de Bucaramanga — carga especial |

---

### Errores Comunes y Soluciones

| Error | Causa | Solución |
|---|---|---|
| `NoClassDefFoundError: org/apache/commons/logging` | Falta `commons-logging-1.2.jar` | Agregar el JAR al Classpath |
| `package org.jxmapviewer does not exist` | Falta `jxmapviewer2-2.6.jar` | Agregar el JAR al Classpath |
| Tabla vacía al abrir | La carpeta `data/` no está en la raíz del proyecto | Verificar y corregir la ruta |
| `0 líneas cargadas` al abrir CSV | Formato del CSV no compatible | Usar el CSV original descargado con el proyecto |
| Mapa gris sin tiles | Sin conexión a internet | Normal — las subestaciones igual se muestran |
| Pantalla en blanco (componentes invisibles) | Incompatibilidad JDK 26 con DirectDraw | Agregar `System.setProperty("sun.java2d.noddraw", "true")` en `Main.java` |
| `BUILD FAILED` al compilar | Versión de Java incompatible o JARs no encontrados | Usar JDK 26 y verificar classpath en `project.properties` |
| Caracteres extraños en tildes | Codificación del CSV incorrecta | Guardar el CSV en **UTF-8** (verificar con Notepad++) |
| `incompatible types: Point2D cannot be converted to Point` | Versión de JXMapViewer incompatible | Usar el `PanelMapa.java` corregido con `Point2D` |

---

*Datos técnicos obtenidos de [PARATEC - XM S.A. E.S.P.](https://paractec.xm.com.co) — SubÁrea Santander, junio 2026.*
