package Persona;

import Inventario.Categoria;
import Inventario.Producto;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import Factura.Combrobante;
import Factura.DetalleVenta;
import Factura.RegistroVentas;
import Factura.Venta;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Empleado extends Usuario {

    // Constructor
    public Empleado(int id, String nombre, String usuario, String contrasena) {
        super(id, nombre, usuario, contrasena);
    }

    // Métodos
    // Se agregan registro y read para conectar las ventas con HU-10 y HU-11.
    public void registrarVenta(
            Map<String, Producto> inventario,
            RegistroVentas registro,
            Scanner read) {

        Venta venta = new Venta(registro.siguienteId());

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

                    // guarda el detalle para HU-10 y HU-11.
                    venta.agregarDetalle(new DetalleVenta(producto, cantidad));

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

            // guarda la venta antes de confirmar el recibo.
            try {
                registro.guardar(venta);

            } catch (IOException e) {
                // Si falla el guardado, devuelve el stock descontado.
                for (DetalleVenta detalle : venta.getDetalles()) {
                    Producto producto = inventario.get(
                            detalle.getCodigoProducto()
                    );

                    producto.setCantidadActual(
                            producto.getCantidadActual() + detalle.getCantidad()
                    );
                }

                System.out.println(
                        "No se pudo guardar la venta. Se devolvió el stock: "
                                + e.getMessage()
                );
                return;
            }

            System.out.println("\n===== RECIBO =====");
            System.out.println(recibo);
            System.out.println("TOTAL A COBRAR: $" + total);
            System.out.println("==================");

            // HU-10: exporta el comprobante completo como TXT.
            try {
                System.out.println(
                        "Comprobante exportado: "
                                + venta.generarComprobante().exportar(
                                venta,
                                Paths.get("datos", "comprobantes")
                        )
                );

            } catch (IOException e) {
                System.out.println(
                        "La venta está guardada, pero no se pudo exportar el TXT. "
                                + "Reintente desde Consultar facturas."
                );
            }
        }
    }

    // HU-10: listar facturas, buscar por fecha y exportar comprobantes.
    public void consultarFacturas(RegistroVentas registro, Scanner read) {
        System.out.println("1. Listar todas las facturas");
        System.out.println("2. Buscar por fecha");
        System.out.println("3. Ver y exportar una factura por número de venta");
        System.out.print("Seleccione una opción: ");

        String opcion = read.nextLine().trim();

        if (opcion.equals("1")) {
            mostrarFacturas(registro.listar());

        } else if (opcion.equals("2")) {
            System.out.print("Fecha (AAAA-MM-DD): ");

            try {
                LocalDate fecha = LocalDate.parse(read.nextLine().trim());
                mostrarFacturas(registro.buscarPorFecha(fecha));

            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Use AAAA-MM-DD.");
            }

        } else if (opcion.equals("3")) {
            System.out.print("Número de venta (ejemplo: 1 para C-0001): ");

            try {
                int numero = Integer.parseInt(read.nextLine().trim());
                Venta venta = registro.buscarPorId(numero);

                if (venta == null) {
                    System.out.println("Factura no encontrada.");
                    return;
                }

                Combrobante comprobante = venta.generarComprobante();
                comprobante.imprimir(venta);

                System.out.println(
                        "Archivo exportado: "
                                + comprobante.exportar(
                                venta,
                                Paths.get("datos", "comprobantes")
                        )
                );

            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero.");

            } catch (IOException e) {
                System.out.println("No se pudo exportar: " + e.getMessage());
            }

        } else {
            System.out.println("Opción inválida.");
        }
    }

    // HU-10: muestra las facturas obtenidas en una consulta.
    private void mostrarFacturas(List<Venta> ventas) {
        if (ventas.isEmpty()) {
            System.out.println("No hay facturas para esta consulta.");
            return;
        }

        for (Venta venta : ventas) {
            System.out.printf(
                    Locale.ROOT,
                    "%s | Fecha: %s | Total: $%.2f%n",
                    venta.generarComprobante().getNumero(),
                    venta.getFecha(),
                    venta.getTotal()
            );
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

        Scanner sc= new Scanner(System.in);
        int option=0;
        verAlertas(inventario, categorias);
        do {
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

    public void consultarInventarioActivos(Map<String, Producto> inventario, Map<Integer, Categoria> categorias){
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

    // HU-11: consultar las ventas históricas de un producto.
    public void verHistorialVentas(RegistroVentas registro, Scanner read) {
        System.out.print("Código del producto a consultar: ");
        String codigo = read.nextLine().trim();

        List<String> historial = registro.historialProducto(codigo);

        if (historial.isEmpty()) {
            System.out.println("No hay ventas registradas de ese producto.");
            return;
        }

        System.out.println("Fecha | Venta | Cantidad vendida");

        for (String fila : historial) {
            System.out.println(fila);
        }
    }

    // HU-11: identificar los productos más vendidos por unidades.
    public void verProductosMasVendidos(RegistroVentas registro) {
        Map<String, Long> ranking = registro.productosMasVendidos();

        if (ranking.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        System.out.println("PRODUCTOS MÁS VENDIDOS");
        int puesto = 1;

        for (Map.Entry<String, Long> producto : ranking.entrySet()) {
            System.out.println(
                    puesto++ + ". "
                            + producto.getKey() + " | "
                            + registro.nombreProducto(producto.getKey())
                            + " | Unidades vendidas: " + producto.getValue()
            );
        }
    }
}