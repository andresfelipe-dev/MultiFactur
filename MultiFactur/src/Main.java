import Inventario.Categoria;
import Inventario.Producto;
import Persona.Empleado;
import Persona.Propietario;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        int desicionMenu = 0;
        Map<String, Producto> inventario = new HashMap<>();
        Map<Integer, Categoria> categorias = new HashMap<>();

        //Lo que hago aquí es crear el objeto y añadirlo en el HashMap en una sola linea.
        //Añado un elemento en el HashMap y dentro de él creo el objeto.
        categorias.put(0, new Categoria(0, "General"));
        categorias.put(1, new Categoria(1, "Granos"));
        categorias.put(2, new Categoria(2, "Aseo"));
        categorias.put(3, new Categoria(3, "Gaseosas"));
        categorias.put(4, new Categoria(4, "Mecato"));
        categorias.put(5, new Categoria(5, "Cuidos"));
        categorias.put(6, new Categoria(6, "Parba"));

        //Ejemplos de Productos
        inventario.put("P001", new Producto("P001", "Leche Colanta 1L", 3800.0, 2900.0, 40, 10, LocalDate.now().plusMonths(2), true, categorias.get(1)));
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

        //Usuario temporal admin para realizar todos los procedimientos
        Propietario admin = new Propietario(1, "Gabriel", "admin", "1234");
        do {
            System.out.println();
            System.out.println("***** MULTIFACTUR *****");
            System.out.println();
            System.out.println("--- Bienvenido al sistema de gestión de inventario y ventas ---");
            System.out.println("¿Que quieres hacer? Ingresa el número de la opción elegida.");
            System.out.println();
            System.out.println("-------------- MENU ----------------");
            System.out.println("1. Registrar un Producto Nuevo");
            System.out.println("2. Consultar Inventario Disponible.");
            System.out.println("3. Registrar Ventas");
            System.out.println("4. Eliminar Productos");
            System.out.println("5. Cerrar sesión");
            System.out.println("-------------------------------------");
            desicionMenu = read.nextInt();
            read.nextLine();
            switch (desicionMenu){
                case 1:
                    admin.registrarProducto(inventario, categorias);
                    break;
                case 2:
                    admin.consultarInventario(inventario, categorias);
                    break;
                case 3:
                    admin.registrarVenta(inventario);
                    break;
                case 4:
                    admin.eliminarProducto(inventario, read);
                    break;
                case 5:
                    System.out.println("Sesión Cerrada Correctamente");
                    break;
                default:
                    System.out.println("Opción Ingresada NO Valida." +
                            "\nIntenta Nuevamente.");
            }
        } while (desicionMenu != 5);
    }
}
