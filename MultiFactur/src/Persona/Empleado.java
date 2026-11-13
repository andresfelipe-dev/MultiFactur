package Persona;

import Inventario.Categoria;
import Inventario.Producto;
import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

public class Empleado extends Usuario {

    // Constructor
    public Empleado(String nombre, String usuario, String contrasena) {
        super(nombre, usuario, contrasena);
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
                    System.out.println();
                    System.out.println("Producto agregado. " + "\nNombre: " + producto.getNombre());
                    System.out.println();
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
    public void registrarEntrada(Map<String, Producto> inventario, Scanner read) {

        System.out.println("***** REGISTRAR ENTRADA DE MERCANCÍA *****");

        if (inventario.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        System.out.print("Ingrese el código del producto: ");
        String codigo = read.nextLine().trim();

        // Buscar el producto existente por su código.
        Producto producto = inventario.get(codigo);

        if (producto == null) {
            System.out.println("No existe un producto con ese código.");
            return;
        }

        System.out.println("Producto: " + producto.getNombre());
        System.out.println("Cantidad actual: " + producto.getCantidadActual());
        System.out.print("Ingrese la cantidad de unidades que llegan: ");

        // Comprobar que se escribió un número entero.
        if (!read.hasNextInt()) {
            read.nextLine();
            System.out.println("Debe ingresar una cantidad válida.");
            return;
        }

        int cantidad = read.nextInt();
        read.nextLine();

        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor que cero.");
            return;
        }

        // Evitar que la suma supere el límite del tipo int.
        if (producto.getCantidadActual() > Integer.MAX_VALUE - cantidad) {
            System.out.println("La cantidad ingresada supera el límite permitido.");
            return;
        }

        // Sumar las unidades recibidas al stock que ya tenía el producto.
        int nuevaCantidad = producto.getCantidadActual() + cantidad;
        producto.setCantidadActual(nuevaCantidad);

        System.out.println("Entrada registrada correctamente.");
        System.out.println("Producto: " + producto.getNombre());
        System.out.println("Unidades recibidas: " + cantidad);
        System.out.println("Cantidad actualizada: " + nuevaCantidad);
    }

    public void editarProducto(Map<String, Producto> inventario,
                               Map<Integer, Categoria> categorias,
                               Scanner read) {

        System.out.println("\n***** EDITAR PRODUCTO *****");

        if (inventario.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        System.out.print("Ingrese el código del producto: ");
        String codigo = read.nextLine().trim();

        Producto producto = inventario.get(codigo);

        if (producto == null) {
            System.out.println("No existe un producto con ese código.");
            return;
        }

        System.out.println("Producto seleccionado: " + producto.getNombre());
        System.out.println("Ingrese todos los datos que tendrá el producto.");

        // Primero recogemos los datos sin modificar todavía el producto.
        System.out.print("Nuevo nombre: ");
        String nombre = read.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede quedar vacío.");
            return;
        }

        try {
            System.out.print("Nuevo precio de venta (use punto para decimales): ");
            double precioVenta = Double.parseDouble(read.nextLine().trim());

            if (!Double.isFinite(precioVenta) || precioVenta <= 0) {
                System.out.println("El precio debe ser un número mayor que cero.");
                return;
            }

            System.out.println("Categorías disponibles:");

            for (Categoria categoria : categorias.values()) {
                System.out.println(
                        categoria.getIdCategoria() + ". " + categoria.getNombre()
                );
            }

            System.out.print("Ingrese el ID de la nueva categoría: ");
            int idCategoria = Integer.parseInt(read.nextLine().trim());

            Categoria categoria = categorias.get(idCategoria);

            if (categoria == null) {
                System.out.println("La categoría seleccionada no existe.");
                return;
            }

            System.out.print("Nueva fecha de vencimiento (AAAA-MM-DD): ");
            LocalDate fechaVencimiento =
                    LocalDate.parse(read.nextLine().trim());

            System.out.print("Nueva cantidad actual: ");
            int cantidadActual = Integer.parseInt(read.nextLine().trim());

            System.out.print("Nueva cantidad mínima: ");
            int cantidadMinima = Integer.parseInt(read.nextLine().trim());

            if (cantidadActual < 0 || cantidadMinima < 0) {
                System.out.println("Las cantidades no pueden ser negativas.");
                return;
            }

            // Guardamos únicamente después de validar todos los datos.
            producto.setNombre(nombre);
            producto.setPrecioVenta(precioVenta);
            producto.setCategoria(categoria);
            producto.setFechaVencimiento(fechaVencimiento);
            producto.setCantidadActual(cantidadActual);
            producto.setCantidadMinima(cantidadMinima);

            System.out.println("Producto actualizado correctamente.");
            System.out.println("Código: " + producto.getCodigo());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Precio de venta: " + producto.getPrecioVenta());
            System.out.println("Categoría: " + producto.getCategoria().getNombre());
            System.out.println("Vencimiento: " + producto.getFechaVencimiento());
            System.out.println("Cantidad actual: " + producto.getCantidadActual());
            System.out.println("Cantidad mínima: " + producto.getCantidadMinima());

        } catch (NumberFormatException e) {
            System.out.println(
                    "Número inválido o vacío. No se guardaron los cambios."
            );
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println(
                    "Fecha inválida. Use AAAA-MM-DD. No se guardaron los cambios."
            );
        }
    }

    public void registrarProducto(Map<String, Producto> inventario, Map<Integer, Categoria> categorias){
        Scanner read = new Scanner(System.in);

        System.out.println("REGISTRAR PRODUCTO");

        String desicion = "";
        while (!desicion.equals("2")){
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
            for (Categoria categoria : categorias.values()) {
                System.out.println(
                        categoria.getIdCategoria() + ". " + categoria.getNombre()
                );
            }
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
            read.nextLine();
            desicion = read.nextLine();
            if (!desicion.equals("1") && !desicion.equals("2")){
                desicion = "2";
                System.out.println("Salió Correctamente del Menú de Registro");
            }
        }
    }

    public void consultarInventario(Map<String, Producto> inventario, Map<Integer, Categoria> categorias){

        Scanner sc= new Scanner(System.in);
        int option=0;
        verAlertas(inventario, categorias);
        do {
            System.out.println("***********");
            System.out.println("INVENTARIO");
            System.out.println("***********");
            System.out.println("1. Nombre.");
            System.out.println("2. Categoria.");
            System.out.println("3. Codigo.");
            System.out.println("4. Listar todo el inventario.");
            System.out.println("5. Salir.");
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
