import java.util.Scanner;

/**
 * Programa principal — Directorio de Extensiones Telefónicas.
 * Demuestra todas las operaciones del CustomHashMap.
 */
public class Main {

    private static final CustomHashMap<String, Empleado> directorio =
            new CustomHashMap<>(16);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        cargarDatosEjemplo();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ");
            System.out.println();

            switch (opcion) {
                case 1 -> agregarEmpleado();
                case 2 -> buscarEmpleado();
                case 3 -> eliminarEmpleado();
                case 4 -> directorio.display();
                case 5 -> mostrarHash();
                case 0 -> System.out.println("Fin del programa.");
                default -> System.out.println("Opción inválida.\n");
            }

        } while (opcion != 0);

        scanner.close();
    }

    // ------------------------------------------------------------------ //

    private static void mostrarMenu() {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║   DIRECTORIO TELEFÓNICO      ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Agregar empleado         ║");
        System.out.println("║  2. Buscar empleado          ║");
        System.out.println("║  3. Eliminar empleado        ║");
        System.out.println("║  4. Mostrar directorio       ║");
        System.out.println("║  5. Ver índice hash de nombre║");
        System.out.println("║  0. Salir                    ║");
        System.out.println("╚══════════════════════════════╝");
    }

    private static void agregarEmpleado() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Extensión: ");
        String extension = scanner.nextLine().trim();

        if (nombre.isEmpty() || extension.isEmpty()) {
            System.out.println("Nombre y extensión son obligatorios.\n");
            return;
        }

        directorio.put(nombre, new Empleado(nombre, extension));
        System.out.printf("Empleado '%s' guardado en índice [%d].%n%n",
                nombre, directorio.generarHash(nombre));
    }

    private static void buscarEmpleado() {
        System.out.print("Nombre a buscar: ");
        String nombre = scanner.nextLine().trim();
        Empleado result = directorio.get(nombre);

        if (result != null) {
            System.out.println("Encontrado -> " + result);
        } else {
            System.out.printf("'%s' no existe en el directorio.%n", nombre);
        }
        System.out.println();
    }

    private static void eliminarEmpleado() {
        System.out.print("Nombre a eliminar: ");
        String nombre = scanner.nextLine().trim();
        boolean ok = directorio.delete(nombre);

        if (ok) {
            System.out.printf("'%s' eliminado correctamente.%n%n", nombre);
        } else {
            System.out.printf("'%s' no existe en el directorio.%n%n", nombre);
        }
    }

    private static void mostrarHash() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.printf("generarHash(\"%s\") -> índice [%d]%n%n", nombre, directorio.generarHash(nombre));
    }

    private static void cargarDatosEjemplo() {
        directorio.put("Carlos Lopez", new Empleado("Carlos Lopez", "1042"));
        directorio.put("Ana Ruiz", new Empleado("Ana Ruiz", "2018"));
        directorio.put("Pedro Gonzalez", new Empleado("Pedro Gonzalez", "3305"));
        directorio.put("Maria Torres", new Empleado("Maria Torres", "1150"));
        directorio.put("Luis Herrera", new Empleado("Luis Herrera", "4421"));
        System.out.println("5 empleados de ejemplo cargados.\n");
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
