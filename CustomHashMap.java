/**
 * HashMap personalizado sin usar java.util.HashMap.
 *
 * Estrategia de colisiones : Chaining (lista enlazada por slot)
 * Función hash             : Método de la Potencia con primo 31
 *                            hash = sum( char[i] * 31^i )  % capacidad
 *
 * @param <K> tipo de la llave  — debe ser String para que generarHash funcione
 * @param <V> tipo del valor
 */

public class CustomHashMap<K, V> {
    private static final int CAPACIDAD_DEFAULT = 16;
    private static final int PRIMO = 31;

    private final Nodo<K, V>[] tabla;   // arreglo de listas enlazadas
    private final int capacidad;
    private int tamanio;  // cantidad de pares almacenados

    public CustomHashMap() {
        this(CAPACIDAD_DEFAULT);
    }

    public CustomHashMap(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new Nodo[capacidad];   // cada slot inicia en null
        this.tamanio = 0;
    }


    /**
     * Convierte cualquier texto (la llave K convertida a String) en un
     * índice válido dentro del arreglo.
     *
     * Técnica: Método de la Potencia con primo 31
     *   hash = char[0]*31^0 + char[1]*31^1 + char[2]*31^2 + ...
     *
     * Luego:  índice = |hash| % capacidad
     *
     * Normalización: se convierte a mayúsculas para que "carlos" y "Carlos"
     * den el mismo índice.
     */
    public int generarHash(K llave) {
        String texto = llave.toString().toUpperCase().trim();
        long hash = 0;
        long potencia = 1;

        for (int i = 0; i < texto.length(); i++) {
            hash += texto.charAt(i) * potencia;
            potencia *= PRIMO;
        }

        // Math.abs para evitar índices negativos por overflow
        return (int)(Math.abs(hash) % capacidad);
    }

    /**
     * Inserta el par (llave, valor) en la tabla.
     * Si la llave ya existe, actualiza su valor (comportamiento estándar de map).
     */
    public void put(K llave, V valor) {
        int indice = generarHash(llave);
        Nodo<K, V> actual = tabla[indice];

        // Recorrer la lista enlazada en ese slot
        while (actual != null) {
            if (actual.llave.equals(llave)) {
                // Llave duplicada -> actualizar valor
                actual.valor = valor;
                return;
            }
            actual = actual.siguiente;
        }

        // No existía -> agregar al frente de la lista (O(1))
        Nodo<K, V> nuevoNodo = new Nodo<>(llave, valor);
        nuevoNodo.siguiente = tabla[indice];   // encadenar con lo que había
        tabla[indice] = nuevoNodo;
        tamanio++;
    }

    /**
     * Devuelve el valor asociado a la llave, o null si no existe.
     */
    public V get(K llave) {
        int indice = generarHash(llave);
        Nodo<K, V> actual = tabla[indice];

        while (actual != null) {
            if (actual.llave.equals(llave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Elimina el nodo con la llave dada de la lista enlazada en su slot.
     * Devuelve true si se eliminó, false si no existía.
     */
    public boolean delete(K llave) {
        int indice = generarHash(llave);
        Nodo<K, V> actual = tabla[indice];
        Nodo<K, V> anterior = null;

        while (actual != null) {
            if (actual.llave.equals(llave)) {
                if (anterior == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    // Saltar el nodo eliminado
                    anterior.siguiente = actual.siguiente;
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Imprime el estado completo de la tabla, slot por slot.
     * Muestra visualmente las colisiones (listas con más de un nodo).
     */
    public void display() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          DIRECTORIO DE EXTENSIONES               ║");
        System.out.printf ("║  Capacidad: %-5d  |  Registros: %-5d           ║%n", capacidad, tamanio);
        System.out.println("╠══════════════════════════════════════════════════╣");

        for (int i = 0; i < capacidad; i++) {
            if (tabla[i] == null) {
                System.out.printf("║  [%02d] -> vacío%n", i);
            } else {
                System.out.printf("║  [%02d] -> ", i);
                Nodo<K, V> actual = tabla[i];
                while (actual != null) {
                    System.out.printf("(%s : %s)", actual.llave, actual.valor);
                    if (actual.siguiente != null) System.out.print(" -> ");
                    actual = actual.siguiente;
                }
                System.out.println();
            }
        }
        System.out.println("╚══════════════════════════════════════════════════╝\n");
    }

    public int getTamanio() {
        return tamanio;
    }

    public boolean isEmpty() {
        return tamanio == 0;
    }
    
    public boolean contiene(K llave) {
        return get(llave) != null;
    }
}
