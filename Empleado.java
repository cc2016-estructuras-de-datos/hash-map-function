/**
 * Objeto de valor que representa un empleado en el directorio telefónico.
 */
public class Empleado {

    private String nombre;
    private String extension;

    public Empleado(String nombre, String extension) {
        this.nombre = nombre;
        this.extension = extension;
    }

    public String getNombre() {
        return nombre;
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public String toString() {
        return String.format("Empleado{ nombre='%s', extension='%s' }", nombre, extension);
    }
}
