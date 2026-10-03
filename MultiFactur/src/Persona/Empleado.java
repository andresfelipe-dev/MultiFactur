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
    public void registrarVenta(Map<String, Producto> inventario) {
        Scanner read = new Scanner(System.in);
        double total = 0;
        String recibo = "";

        System.out.println("REGISTRAR VENTA");

        boolean seguir = true;
        while (seguir) {
            System.out.print("Codigo del producto (escribe fin para terminar): ");
            String codigo = read.nextLine();

            //esto termina el ciclo si el usuario coloca fin
            if (codigo.equals("fin")) {
                seguir = false;

            } else if (!inventario.containsKey(codigo)) {
                System.out.println("ERROR: Ese producto no existe.");

            } else {
                //esto obtiene el codigo del producto si existe
                Producto producto = inventario.get(codigo);

                System.out.print("Cantidad a vender: ");
                int cantidad = read.nextInt();
                read.nextLine();

                if (cantidad <= 0) {
                    System.out.println("ERROR: La cantidad debe ser mayor a 0.");

                } else if (cantidad > producto.getCantidadActual()) {
                    System.out.println("ERROR: Solo quedan " + producto.getCantidadActual() + " en stock.");

                } else {
                    //esto calcula el subtotal segun la cantidad de productos
                    double subtotal = producto.getPrecioVenta() * cantidad;
                    total = total + subtotal;

                    // esto le resta la cantidad en el stock al producto
                    producto.setCantidadActual(producto.getCantidadActual() - cantidad);

                    //esto da todos los valores del producto seleccionado
                    recibo = recibo + producto.getNombre() + " x" + cantidad + " = $" + subtotal + "\n";
                    System.out.println("Producto agregado.");
                }
            }
        }

        //esto imprime el recibo con todos los datos
        if (total == 0) {
            System.out.println("No se vendio nada.");
        } else {
            System.out.println("\n===== RECIBO =====");
            System.out.println(recibo);
            System.out.println("TOTAL A COBRAR: $" + total);
            System.out.println("==================");
        }
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

    }

    public void definirCantidadMinima(){

    }

    public void verAlertas(){

    }

    public void verHistorialVentas(){

    }
}
