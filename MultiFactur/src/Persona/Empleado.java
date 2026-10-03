package Persona;

import Inventario.Categoria;
import Inventario.Producto;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Empleado extends Usuario {

    // Constructor
    public Empleado(int id, String nombre, String usuario, String contrasena) {
        super(id, nombre, usuario, contrasena);
    }

    //Metodos
    public void registrarVenta(){

    }

    public void registrarEntrada(){

    }

    public void editarProducto(){

    }

    public void registrarProducto(Map<String, Producto> inventario, Map<Integer, Categoria> categorias){
        Scanner read = new Scanner(System.in);

        System.out.println("REGISTRAR PRODUCTO");

        int desicion=0;
        while (desicion != 2){
            System.out.print("Ingrese el código del producto: ");
            String codigo = read.nextLine();

            if (inventario.containsKey(codigo)){
                System.out.println("ERROR: Ya existe un producto registrado con este código." + codigo);
                return;
            }

            System.out.print("Ingrese el nombre del producto: ");
            String nombre = read.nextLine();

            System.out.print("Ingrese el precio de venta: ");
            double precioVenta = read.nextDouble();

            if (nombre.isEmpty() || precioVenta <= 0){
                System.out.println("ERROR: El producto de no se puede registrar sin un nombre y un precio de venta mayor a 0.");
                return;
            }

            System.out.print("Ingrese el precio de costo: ");
            double precioCosto = read.nextDouble();

            System.out.print("Ingrese la cantidad inicial (stock): ");
            int cantidadActual = read.nextInt();

            System.out.print("Ingrese la cantidad mínima que debería tener el producto: ");
            int cantidadMinima = read.nextInt();

            //Asignacion de categoría
            System.out.print("Ingrese el ID de la categoría del producto:");
            int idCategoria = read.nextInt();

            //Creo una variable "catObjetnida" de tipo de dato "Categoria" y con el "=" extraigo un valor
            //dentro del HashMap, si no existe ese Id de categoria, le va a asignar  por defecto la categoria "General"
            //que fue creada dentro de Main
            Categoria catObtenida = categorias.get(idCategoria);
            if (catObtenida == null){
                System.out.println("ID de categoría no encontrado. Se añadirá a la categoría General.");
                catObtenida = categorias.get(0);
            }

            LocalDate fechaVencimiento = LocalDate.now().plusMonths(6);

            boolean activo = true;
            //Asigno todos los valores ingresados a el nuevo objeto de tipo Producto
            Producto nuevoProducto = new Producto(
                    codigo, nombre, precioVenta, precioCosto, cantidadActual, cantidadMinima, fechaVencimiento,
                    activo, catObtenida
            );
            //Asigno el codigo del producto como la clave del HashMap y todo el objeto Producto como el valor del HashMap
            inventario.put(codigo, nuevoProducto);

            System.out.println("Producto: " + nombre +
                    "\nCódigo: "+ codigo+
                    "\nCategoria: " + catObtenida.getNombre() +
                    "\nAgregado Corectamente");

            System.out.println();
            System.out.println("Quieres Ingresar un nuevo producto?");
            System.out.println("Ingresa 1 = Si. 2 = NO");
            desicion = read.nextInt();
            read.nextLine();
            if (desicion !=1 && desicion !=2){
                desicion = 2;
                System.out.println("Salió Correctamente del Menú de Registro");
            }
        }
    }

    public void consultarInventario(Map<String, Producto> inventario, Map<Integer, Categoria> categorias){

        Scanner sc= new Scanner(System.in);
        int option=0;
        do {
            verAlertas(inventario,categorias);
            System.out.println("***********");
            System.out.println("INVENTARIO");
            System.out.println("***********");
            System.out.println("1. Nombre.");
            System.out.println("2. Categoria.");
            System.out.println("3. Codigo:");
            System.out.println("4. Listar todo el inventario");
            System.out.println("5. Salir");
            System.out.println("***********");
            System.out.println("Ingrese la opcion por la cual desea buscar en el inventario");
            option=sc.nextInt();
            sc.nextLine();
            switch (option){
                case 1:
                    System.out.println("Ingrese el nombre:");
                    String nombre=sc.nextLine();
                    for (Producto producto : inventario.values()){
                        if (producto.getNombre().toLowerCase().contains(nombre.toLowerCase())){
                            System.out.println("Codigo: " + producto.getCodigo());
                            System.out.println("Nombre: " + producto.getNombre());
                            System.out.println("Cantidad disponible: " + producto.getCantidadActual());
                        }
                    }
                    break;
                case 2:
                    System.out.println("Ingrese la categoria:");
                    String categoria=sc.nextLine();
                    for (Producto producto: inventario.values()){
                        if (producto.getCategoria().getNombre().toLowerCase().contains(categoria.toLowerCase())){
                            System.out.println("");
                            System.out.println("Codigo: " + producto.getCodigo());
                            System.out.println("Nombre: " + producto.getNombre());
                            System.out.println("Cantidad disponible: " + producto.getCantidadActual());
                            System.out.println("");
                        }
                    }
                    break;
                case 3:
                    System.out.println("Ingrese el codigo:");
                    String codigo=sc.nextLine();
                    Producto producto=inventario.get(codigo);
                    System.out.println("");
                    System.out.println("Nombre: " + producto.getNombre());
                    System.out.println("Cantidad disponible:" + producto.getCantidadActual());
                    System.out.println("");
                    break;
                case 4:
                    for (Producto product: inventario.values()){
                        System.out.println("");
                        System.out.println("Codigo: " +product.getCodigo());
                        System.out.println("Nombre: " +product.getNombre());
                        System.out.println("Cantidad disponible: " + product.getCantidadActual());
                        System.out.println("");
                    }
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Saliendo...");
                    break;
            }
        }while (option!=5);

    }

    public void definirCantidadMinima(){

    }

    public void verAlertas(Map<String, Producto> inventario, Map<Integer, Categoria> categorias){
        for (Producto producto: inventario.values()){
            if (producto.getCantidadActual()<= producto.getCantidadMinima()){
                System.out.println("");
                System.out.println("ALERTA DE STOCK BAJO");
                System.out.println("Quedan " + producto.getCantidadActual() + " unidades de " + producto.getNombre());
                System.out.println("");
            }
        }
    }

    public void verHistorialVentas(){

    }
}
