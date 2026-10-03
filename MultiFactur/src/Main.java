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
        inventario.put("P002", new Producto("P002", "Limpido 1.8L", 12500.0, 9000.0, 15, 5, LocalDate.now().plusMonths(12), true, categorias.get(2)));

        //Usuario temporal admin para realizar todos los procedimientos
        Propietario admin = new Propietario(1, "Gabriel", "admin", "1234");
        do {
            System.out.println("***** MULTIFACTUR *****");
            System.out.println("Bienvenido al sistema de gestión de inventario y ventas.");
            System.out.println("¿Que quieres hacer? Ingresa el número de la opción elegida.");
            System.out.println();
            System.out.println("1. Registrar un Producto Nuevo");
            System.out.println("2. Consultar Inventario Disponible.");
            System.out.println("3. Registrar Ventas");
            System.out.println("4. Eliminar Productos");
            System.out.println("5. Cerrar sesión");
            desicionMenu = read.nextInt();
            switch (desicionMenu){
                case 1:
                    admin.registrarProducto(inventario, categorias);
                    break;
                case 2:
                    admin.consultarInventario(inventario, categorias);
                    break;
                case 3:
                    //admin.registrarVenta();
                    empleado.registrarVenta(inventario);
                    break;
                case 4:
                    admin.eliminarProducto();
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
