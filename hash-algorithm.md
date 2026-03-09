# Algoritmo Hash — Método de la Potencia con Primo 31

Documentación técnica del algoritmo usado en `generarHash()` dentro de `CustomHashMap.java`.

---

## ¿Qué problema resuelve?

Un HashMap necesita convertir una llave (texto) en un número entero que sirva como índice de un arreglo. Ese proceso es la **función hash**.

El reto es que la función debe:
- Siempre dar el mismo índice para la misma llave.
- Distribuir las llaves lo más uniformemente posible entre los slots.
- Ser rápida de calcular.

---

## La fórmula

```
hash = char[0]×31⁰  +  char[1]×31¹  +  char[2]×31²  +  ...  +  char[n-1]×31^(n-1)

índice = |hash| % capacidad
```

Donde:
- `char[i]` es el valor ASCII del carácter en la posición `i`.
- `31` es el número primo base (explicado abajo).
- `capacidad` es el tamaño del arreglo (16 en este proyecto).
- `| |` es valor absoluto, para evitar índices negativos por overflow.

---

## Implementación en Java

```java
public int generarHash(K llave) {
    String texto    = llave.toString().toUpperCase().trim();
    long   hash     = 0;
    long   potencia = 1;  // 31^0 = 1 en la primera iteración

    for (int i = 0; i < texto.length(); i++) {
        hash     += texto.charAt(i) * potencia;
        potencia *= 31;
    }

    return (int)(Math.abs(hash) % capacidad);
}
```

---

## Paso a paso con un ejemplo real

Llave de entrada: `"LUIS"`  
Capacidad del arreglo: `16`

### 1. Normalización

```
"Luis"  →  toUpperCase()  →  "LUIS"
```

Esto garantiza que `"luis"`, `"Luis"` y `"LUIS"` siempre produzcan el mismo índice.

### 2. Cálculo del hash

| i | char | ASCII | potencia (31^i) | contribución |
|---|------|-------|-----------------|--------------|
| 0 | L    | 76    | 1               | 76           |
| 1 | U    | 85    | 31              | 2635         |
| 2 | I    | 73    | 961             | 70153        |
| 3 | S    | 83    | 29791           | 2472653      |

```
hash = 76 + 2635 + 70153 + 2472653 = 2545517
```

### 3. Reducción al rango del arreglo

```
índice = 2545517 % 16 = 13
```

`"LUIS"` se almacena en `tabla[13]`.

---

## ¿Por qué el número 31?

El 31 no es arbitrario. Tiene tres propiedades que lo hacen ideal:

**Es primo.**  
Los números primos reducen la probabilidad de colisiones porque no comparten factores con la capacidad del arreglo (que suele ser potencia de 2). Esto produce una distribución más uniforme de los índices.

**Es impar.**  
Multiplicar por un número par puede causar pérdida de bits en operaciones binarias. El 31 al ser impar evita ese problema.

**Es eficiente para el compilador.**  
`x * 31` es equivalente a `(x << 5) - x`, una operación que los procesadores modernos ejecutan muy rápido. Es por esto que Java usa exactamente este primo en su propio `String.hashCode()`.

---

## ¿Por qué multiplicar por la potencia y no solo sumar ASCII?

La suma simple de ASCII tiene un problema crítico: es sensible a anagramas.

```
Suma simple de ASCII:
"ROCA" = 82 + 79 + 67 + 65 = 293
"CARO" = 67 + 65 + 82 + 79 = 293  ← misma suma, colisión garantizada
```

Al multiplicar cada carácter por `31^i`, la **posición de cada letra afecta el resultado**:

```
Potencia con primo 31:
"ROCA":  R×1  + O×31  + C×961  + A×29791  =  936,152
"CARO":  C×1  + A×31  + R×961  + O×29791  =  921,692  ← valores distintos
```

Palabras con las mismas letras en distinto orden producen hashes diferentes.

---

## El módulo final — reducir al tamaño del arreglo

El hash puede ser un número enorme (millones). El arreglo solo tiene 16 slots (índices 0–15). El operador `%` (módulo) lo reduce:

```
cualquier número % 16  →  resultado siempre entre 0 y 15
```

| hash      | % 16 | índice |
|-----------|------|--------|
| 2545517   | % 16 | 13     |
| 936152    | % 16 | 8      |
| 198       | % 16 | 6      |
| 1000000   | % 16 | 0      |

---

## ¿Qué pasa con el overflow?

Los números se vuelven muy grandes rápido con potencias. Para una palabra de 10 caracteres, `31^9 = 26,439,622,160,169` — ese valor desborda un `int`.

Por eso se usa `long` en el cálculo:

```java
long hash     = 0;
long potencia = 1;
```

Un `long` aguanta hasta `9,223,372,036,854,775,807`. El `Math.abs()` al final protege contra el caso extremo donde incluso el `long` desborda y produce un número negativo.

---

## Limitaciones del algoritmo

**Colisiones siguen siendo posibles.**  
El módulo `% 16` obliga a que 16 sea el máximo de slots distintos. Si hay más de 16 llaves, al menos dos compartirán índice por el principio del palomar. Eso es normal y para eso existe el chaining.

**La capacidad afecta la distribución.**  
Con 16 slots y una función hash buena, los empleados deberían distribuirse de forma razonablemente uniforme. Si la tabla tuviera solo 2 slots, hasta el mejor algoritmo produciría muchas colisiones.

**No es criptográfico.**  
Este algoritmo está diseñado para velocidad y distribución, no para seguridad. No debe usarse para contraseñas ni datos sensibles.

---

## Comparación con otras técnicas

| Técnica | Fórmula | Problema |
|---------|---------|---------|
| Suma ASCII | `sum(char[i])` | Anagramas colisionan siempre |
| Ponderación por posición | `sum(char[i] * i)` | Mejor, pero primo no garantizado |
| **Potencia con primo 31** | `sum(char[i] * 31^i)` | Distribución óptima, estándar de industria |
