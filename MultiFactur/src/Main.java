import Factura.RegistroVentas;
import Inventario.Categoria;
import Inventario.Producto;
import Persona.Propietario;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        int desicionMenu = 0;

        // HU-10 y HU-11: cargar las ventas guardadas.
        RegistroVentas registro;

        try {
            registro = new RegistroVentas(
                    Paths.get("datos", "ventas.dat")
            );
        } catch (IOException e) {
            System.out.println(
                    "No se pudo cargar el historial de ventas: "
                            + e.getMessage()
            );
            System.out.println(
                    "Se detiene el programa para proteger las ventas guardadas."
            );
            return;
        }

        Map<String, Producto> inventario = new HashMap<>();
        Map<Integer, Categoria> categorias = new HashMap<>();

        // Categorías del proyecto original.
        categorias.put(0, new Categoria(0, "General"));
        categorias.put(1, new Categoria(1, "Granos"));
        categorias.put(2, new Categoria(2, "Aseo"));
        categorias.put(3, new Categoria(3, "Gaseosas"));
        categorias.put(4, new Categoria(4, "Mecato"));
        categorias.put(5, new Categoria(5, "Cuidos"));
        categorias.put(6, new Categoria(6, "Parba"));

        inventario.put(
                "P001",
                new Producto(
                        "P001",
                        "Leche Colanta 1L",
                        3800.0,
                        2900.0,
                        40,
                        10,
                        LocalDate.now().plusMonths(2),
                        true,
                        categorias.get(1)
                )
        );

        inventario.put(
                "P002",
                new Producto(
                        "P002",
                        "Limpido 1.8L",
                        12500.0,
                        9000.0,
                        15,
                        5,
                        LocalDate.now().plusMonths(12),
                        true,
                        categorias.get(2)
                )
        );

        inventario.put(
                "P003",
                new Producto(
                        "P003",
                        "Coca cola 400ml",
                        4000.0,
                        3600.0,
                        2,
                        12,
                        LocalDate.now().plusMonths(2),
                        true,
                        categorias.get(3)
                )
        );

        // Usuario temporal del proyecto original.
        Propietario admin = new Propietario(
                1,
                "Gabriel",
                "admin",
                "1234"
        );

        do {
            System.out.println();
            System.out.println("***** MULTIFACTUR *****");
            System.out.println();
            System.out.println(
                    "--- Bienvenido al sistema de gestión de inventario y ventas ---"
            );
            System.out.println(
                    "¿Que quieres hacer? Ingresa el número de la opción elegida."
            );
            System.out.println();
            System.out.println("-------------- MENU ----------------");

            // Opciones originales.
            System.out.println("1. Registrar un Producto Nuevo");
            System.out.println("2. Consultar Inventario Disponible.");
            System.out.println("3. Registrar Ventas");
            System.out.println("4. Eliminar Productos");
            System.out.println("5. Cerrar sesión");

            // Opciones nuevas de tus historias.
            System.out.println(
                    "6. Consultar facturas y exportar comprobantes (HU-10)"
            );
            System.out.println(
                    "7. Consultar historial de ventas por producto (HU-11)"
            );
            System.out.println(
                    "8. Consultar productos más vendidos (HU-11)"
            );

            System.out.println("-------------------------------------");

            desicionMenu = read.nextInt();
            read.nextLine();

            switch (desicionMenu) {
                case 1:
                    admin.registrarProducto(inventario, categorias);
                    break;

                case 2:
                    admin.consultarInventario(inventario, categorias);
                    break;

                case 3:
                    // Se conecta la venta con el almacenamiento
                    // necesario para HU-10 y HU-11.
                    admin.registrarVenta(inventario, registro, read);
                    break;

                case 4:
                    admin.eliminarProducto(inventario, read);
                    break;

                case 5:
                    System.out.println("Sesión Cerrada Correctamente");
                    break;

                case 6:
                    // HU-10.
                    admin.consultarFacturas(registro, read);
                    break;

                case 7:
                    // HU-11.
                    admin.verHistorialVentas(registro, read);
                    break;

                case 8:
                    // HU-11.
                    admin.verProductosMasVendidos(registro);
                    break;

                default:
                    System.out.println(
                            "Opción Ingresada NO Valida."
                                    + "\nIntenta Nuevamente."
                    );
            }

        } while (desicionMenu != 5);
    }
}