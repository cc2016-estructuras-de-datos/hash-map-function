# CustomHashMap — Directorio de Extensiones Telefónicas

Implementación de un HashMap personalizado en Java **sin usar** `java.util.HashMap`.  
Almacena empleados con nombre y extensión telefónica usando una función hash propia y manejo de colisiones por encadenamiento (chaining).

---

## Cómo compilar y correr

### Requisitos
- Java JDK 11 o superior

### Compilar
Desde la raíz del proyecto:
```bash
javac -d bin Empleado.java Nodo.java CustomHashMap.java Main.java
```

### Correr
```bash
java -cp bin Main
```

### Compilar y correr en un solo comando
```bash
javac -d bin Empleado.java Nodo.java CustomHashMap.java Main.java && java -cp bin Main
```

---

## Uso del programa

Al iniciar, el programa carga 5 empleados de ejemplo y muestra un menú interactivo:

```
╔══════════════════════════════╗
║   DIRECTORIO TELEFÓNICO      ║
╠══════════════════════════════╣
║  1. Agregar empleado         ║
║  2. Buscar empleado          ║
║  3. Eliminar empleado        ║
║  4. Mostrar directorio       ║
║  5. Ver índice hash de nombre║
║  0. Salir                    ║
╚══════════════════════════════╝
```

| Opción | Acción | Ejemplo de entrada |
|--------|--------|--------------------|
| `1` | Agregar un empleado nuevo | Nombre: `Juan Perez` / Extensión: `2201` |
| `2` | Buscar por nombre | `Juan Perez` |
| `3` | Eliminar por nombre | `Juan Perez` |
| `4` | Ver toda la tabla con sus slots | — |
| `5` | Ver qué índice genera un nombre | `Juan Perez` -> `[7]` |
| `0` | Salir | — |

> **Nota:** La búsqueda no es sensible a mayúsculas/minúsculas.  
> `"carlos lopez"` y `"Carlos Lopez"` apuntan al mismo slot.

---

## Estructura del proyecto

```
.
├── Empleado.java        # Objeto de valor: nombre + extensión
├── Nodo.java            # Nodo de la lista enlazada (llave, valor, siguiente)
├── CustomHashMap.java   # HashMap personalizado con generarHash(), put(), get(), delete(), display()
├── Main.java            # Menú interactivo y datos de ejemplo
└── README.md
```

---

## Arquitectura

### Modelo de datos

Cada celda del arreglo interno es la cabeza de una lista enlazada. Esto permite que múltiples empleados que produzcan el mismo índice convivan en el mismo slot.

```
tabla[0]  ->  null
tabla[1]  ->  null
tabla[3]  ->  ("Carlos Lopez", ext:1042)  ->  ("Ana Ruiz", ext:2018)  ->  null
tabla[4]  ->  null
...
tabla[15] ->  null
```

### Clases

| Clase | Responsabilidad |
|-------|----------------|
| `Nodo<K,V>` | Unidad mínima de la lista enlazada. Guarda `llave`, `valor` y referencia al `siguiente` nodo. |
| `Empleado` | Objeto de negocio. Contiene `nombre` y `extension`. |
| `CustomHashMap<K,V>` | Núcleo del programa. Maneja el arreglo, la función hash y todas las operaciones. |
| `Main` | Punto de entrada. Menú interactivo y datos precargados. |

---

## Función Hash

Se usa el **Método de la Potencia con primo 31**, que es robusto contra anagramas y ampliamente validado:

```
hash = char[0] * 31⁰  +  char[1] * 31¹  +  char[2] * 31²  + ...
índice = |hash| % capacidad
```

**Ejemplo con `"CARLOS"`:**

| i | char | ASCII | × 31^i | parcial |
|---|------|-------|--------|---------|
| 0 | C | 67 | × 1 | 67 |
| 1 | A | 65 | × 31 | 2015 |
| 2 | R | 82 | × 961 | 78802 |
| 3 | L | 76 | × 29791 | 2264116 |
| ... | | | | |

`índice = |suma total| % 16`

La llave se normaliza a mayúsculas antes de calcular, garantizando que `"carlos"` y `"Carlos"` siempre caigan en el mismo slot.

---

## Manejo de Colisiones — Chaining

Cuando dos llaves distintas producen el mismo índice, ambos nodos conviven en ese slot dentro de una lista enlazada. Las operaciones `get()` y `delete()` recorren la lista comparando llaves con `.equals()` hasta encontrar la correcta.

```
put("Carlos Lopez")  ->  índice 3
put("Ana Ruiz")      ->  índice 3  ← colisión

tabla[3] -> [Carlos Lopez | 1042] -> [Ana Ruiz | 2018] -> null
```

---

## Operaciones disponibles

| Método | Firma | Descripción |
|--------|-------|-------------|
| `generarHash` | `int generarHash(K llave)` | Convierte la llave en un índice del arreglo |
| `put` | `void put(K llave, V valor)` | Inserta o actualiza un par llave-valor |
| `get` | `V get(K llave)` | Devuelve el valor o `null` si no existe |
| `delete` | `boolean delete(K llave)` | Elimina y devuelve `true` si existía |
| `display` | `void display()` | Imprime el estado completo de la tabla |
| `contiene` | `boolean contiene(K llave)` | Verifica si una llave existe |
| `getTamanio` | `int getTamanio()` | Devuelve el número de registros almacenados |
