/**
 * Nodo que vive dentro de cada slot del arreglo.
 * Forma la lista enlazada para manejar colisiones (chaining).
 *
 * @param <K> tipo de la llave   (ej: String)
 * @param <V> tipo del valor     (ej: Empleado)
 */
public class Nodo<K, V> {

    K llave;
    V valor;
    Nodo<K, V> siguiente;   // enlace al próximo nodo en caso de colisión

    public Nodo(K llave, V valor) {
        this.llave = llave;
        this.valor = valor;
        this.siguiente = null;
    }
}
