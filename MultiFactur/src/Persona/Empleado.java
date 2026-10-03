package Persona;

import Inventario.Categoria;
import Inventario.Producto;
import java.time.LocalDate;
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
        System.out.println("**** Inventario disponible ****");

        boolean hayProductosActivos = false;

        for (Producto producto : inventario.values()){
            if (producto.isActivo()) {
                hayProductosActivos = true;

                System.out.println(
                        "codigo: " + producto.getCodigo()
                        + "Nombre: " + producto.getNombre()
                        + "Cantidad: " + producto.getCantidadActual()
                        + "Precio de venta: " + producto.getPrecioVenta()
                );
            }
        }
        if (!hayProductosActivos){
            System.out.println("No hay productos activos en el inventario.");
        }
    }

    public void definirCantidadMinima(){

    }

    public void verAlertas(){

    }

    public void verHistorialVentas(){

    }
}
