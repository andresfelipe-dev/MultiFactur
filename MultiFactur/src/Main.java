import Factura.RegistroVentas;
import Inventario.Categoria;
import Inventario.Producto;
import Persona.Empleado;
import Persona.Propietario;
import Persona.Usuario;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {
        Scanner read = new Scanner(System.in);

        RegistroVentas registro;
        try {
            registro = new RegistroVentas(Paths.get("datos", "ventas.dat"));
        } catch (IOException e) {
            System.out.println(
                    "No se pudo cargar el historial de ventas: " + e.getMessage()
            );
            System.out.println(
                    "Se detiene el programa para proteger las ventas guardadas."
            );
            return;
        }

        Map<String, Producto> inventario = new HashMap<>();
        Map<Integer, Categoria> categorias = new HashMap<>();
        Map<String, Usuario> usuarios = new HashMap<>();

        cargarDatos(categorias, inventario, usuarios);

        while (true) {
            Usuario sesion = iniciarSesion(usuarios, read);

            if (sesion == null) {
                System.out.println("Programa finalizado.");
                return;
            }

            if (!(sesion instanceof Empleado)) {
                System.out.println("El usuario no tiene acceso a este menú.");
                continue;
            }

            Empleado usuarioActivo = (Empleado) sesion;
            String decisionMenu;

            do {
                System.out.println();
                System.out.println("***** MULTIFACTUR *****");
                System.out.println("-------------- MENÚ ----------------");
                System.out.println("1. Registrar un producto nuevo");
                System.out.println("2. Consultar inventario disponible");
                System.out.println("3. Registrar ventas");
                System.out.println("4. Registrar entrada de mercancía");
                System.out.println("5. Editar producto");

                if (usuarioActivo instanceof Propietario) {
                    System.out.println("6. Eliminar productos");
                    System.out.println("7. Administrar empleados");
                }

                System.out.println("8. Consultar facturas y exportar comprobantes");
                System.out.println("9. Consultar historial de ventas por producto");
                System.out.println("10. Consultar productos más vendidos");
                System.out.println("0. Cerrar sesión");
                System.out.println("------------------------------------");
                System.out.print("Seleccione una opción: ");

                decisionMenu = read.nextLine().trim();

                switch (decisionMenu) {
                    case "1":
                        usuarioActivo.registrarProducto(inventario, categorias);
                        break;

                    case "2":
                        usuarioActivo.consultarInventario(inventario, categorias);
                        break;

                    case "3":
                        usuarioActivo.registrarVenta(inventario, registro, read);
                        break;

                    case "4":
                        usuarioActivo.registrarEntrada(inventario, read);
                        break;

                    case "5":
                        usuarioActivo.editarProducto(inventario, categorias, read);
                        break;

                    case "6":
                        if (usuarioActivo instanceof Propietario) {
                            Propietario propietario = (Propietario) usuarioActivo;
                            propietario.eliminarProducto(inventario, read);
                        } else {
                            System.out.println("No tienes permiso para eliminar productos.");
                        }
                        break;

                    case "7":
                        if (usuarioActivo instanceof Propietario) {
                            Propietario propietario = (Propietario) usuarioActivo;
                            propietario.administrarEmpleados(usuarios, read);
                        } else {
                            System.out.println("No tienes permiso para administrar empleados.");
                        }
                        break;

                    case "8":
                        usuarioActivo.consultarFacturas(registro, read);
                        break;

                    case "9":
                        usuarioActivo.verHistorialVentas(registro, read);
                        break;

                    case "10":
                        usuarioActivo.verProductosMasVendidos(registro);
                        break;

                    case "0":
                        System.out.println("Sesión cerrada correctamente.");
                        break;

                    default:
                        System.out.println("Opción inválida. Intenta nuevamente.");
                }
            } while (!decisionMenu.equals("0"));
        }
    }

    static Usuario iniciarSesion(
            Map<String, Usuario> usuarios,
            Scanner read) {

        System.out.println();
        System.out.println("***** MULTIFACTUR *****");
        System.out.println("--------- INICIAR SESIÓN ---------");
        System.out.println("Escribe 'salir' en Usuario para cerrar el programa.");

        while (true) {
            System.out.print("Usuario: ");
            String nombreUsuario =
                    read.nextLine().trim().toLowerCase(java.util.Locale.ROOT);

            if (nombreUsuario.equals("salir")) {
                return null;
            }

            System.out.print("Contraseña: ");
            String contrasena = read.nextLine();

            Usuario encontrado = usuarios.get(nombreUsuario);

            if (encontrado != null
                    && encontrado.getContrasena().equals(contrasena)) {
                System.out.println("Bienvenid@, " + encontrado.getNombre());
                return encontrado;
            }

            System.out.println("Usuario o contraseña incorrectos. Intenta nuevamente.");
        }
    }

    static void cargarDatos(
            Map<Integer, Categoria> categorias,
            Map<String, Producto> inventario,
            Map<String, Usuario> usuarios) {

        categorias.put(0, new Categoria(0, "General"));
        categorias.put(1, new Categoria(1, "Granos"));
        categorias.put(2, new Categoria(2, "Aseo"));
        categorias.put(3, new Categoria(3, "Gaseosas"));
        categorias.put(4, new Categoria(4, "Mecato"));
        categorias.put(5, new Categoria(5, "Cuidos"));
        categorias.put(6, new Categoria(6, "Parba"));
        categorias.put(7, new Categoria(7, "Lacteos"));

        inventario.put("P001", new Producto("P001", "Leche Colanta 1L", 3800.0, 2900.0, 40, 10, LocalDate.now().plusMonths(2), true, categorias.get(7)));
        inventario.put("P002", new Producto("P002", "Limpido Clorox 1.8L", 12500.0, 9000.0, 15, 5, LocalDate.now().plusMonths(12), true, categorias.get(2)));
        inventario.put("P003", new Producto("P003", "Coca Cola No Retornable 400ml", 4000.0, 3000.0, 30, 8, LocalDate.now().plusMonths(6), true, categorias.get(3)));
        inventario.put("P004", new Producto("P004", "Arroz Diana 1kg", 4500.0, 3500.0, 50, 10, LocalDate.now().plusMonths(12), true, categorias.get(1)));
        inventario.put("P005", new Producto("P005", "Frijol Cargamanto 500g", 6200.0, 4800.0, 25, 8, LocalDate.now().plusMonths(10), true, categorias.get(1)));
        inventario.put("P006", new Producto("P006", "Jabón Rey en Barra", 2200.0, 1600.0, 60, 15, LocalDate.now().plusMonths(24), true, categorias.get(2)));
        inventario.put("P007", new Producto("P007", "Detergente en Polvo Deja 1kg", 9800.0, 7500.0, 20, 5, LocalDate.now().plusMonths(18), true, categorias.get(2)));
        inventario.put("P008", new Producto("P008", "Postobón Manzana 1.5L", 6500.0, 5000.0, 24, 6, LocalDate.now().plusMonths(4), true, categorias.get(3)));
        inventario.put("P009", new Producto("P009", "Papas Margarita Pollo 90g", 3500.0, 2500.0, 35, 10, LocalDate.now().plusMonths(3), true, categorias.get(4)));
        inventario.put("P010", new Producto("P010", "Chocoramo Individual", 2500.0, 1800.0, 40, 12, LocalDate.now().plusDays(20), true, categorias.get(4)));
        inventario.put("P011", new Producto("P011", "Galletas Festival Fresa", 1800.0, 1200.0, 50, 10, LocalDate.now().plusMonths(5), true, categorias.get(4)));
        inventario.put("P012", new Producto("P012", "Dog Chow Adulto 1kg", 14500.0, 11000.0, 15, 4, LocalDate.now().plusMonths(12), true, categorias.get(5)));
        inventario.put("P013", new Producto("P013", "Pan Tajado Bimbo Original", 7800.0, 5800.0, 18, 5, LocalDate.now().plusDays(15), true, categorias.get(6)));
        inventario.put("P014", new Producto("P014", "Almojábanas Paquete x 5", 6000.0, 4200.0, 12, 3, LocalDate.now().plusDays(7), true, categorias.get(6)));
        inventario.put("P015", new Producto("P015", "Pilas AA Duracell x2", 8500.0, 6000.0, 25, 5, LocalDate.now().plusYears(3), true, categorias.get(0)));

        usuarios.put("admin", new Propietario("Gabriel", "admin", "1234"));
    }
}