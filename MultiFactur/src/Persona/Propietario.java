package Persona;
import Inventario.Producto;
import java.util.Map;
import java.util.Scanner;

public class Propietario extends Empleado {

    // Constructor
    public Propietario(String nombre, String usuario, String contrasena) {
        super(nombre, usuario, contrasena);
    }

    public void eliminarProducto(
            Map<String,Producto> inventario,Scanner read) {

        System.out.println("**** ELIMINAR PRODUCTO ****");

        if (inventario.isEmpty()){
            System.out.println("No hay productos en el inventario");
            return;
        }

        //Mostrar los productos disponibles para eliminar
        for (Producto producto : inventario.values()){
            System.out.println("Código: " + producto.getCodigo() + " Nombre: " + producto.getNombre());
        }
        System.out.println("Ingresar el codigo del producto: ");

        String codigo = read.nextLine();

        if (codigo.isEmpty()){
            System.out.println("Operacion invalida.");
            return;
        }

        //Mostrar el producto del codigo ingresado
        Producto producto = inventario.get(codigo);

        if (producto == null){
            System.out.println("No existe un producto con este codigo");
            return;
        }

        System.out.println("Producto seleccionado: " + producto.getNombre());
        System.out.println("Esta seguro que desea eliminar el producto? Escriba SI para confirmar: ");

        String confirmacion = read.nextLine();

        while (!confirmacion.equalsIgnoreCase("SI")
                && !confirmacion.equalsIgnoreCase("SÍ")
                && !confirmacion.equalsIgnoreCase("NO")) {

            System.out.print("Escriba SI para eliminar o NO para cancelar: ");
            confirmacion = read.nextLine();
        }

        if (!confirmacion.equalsIgnoreCase("SI")
        && !confirmacion.equalsIgnoreCase("SÍ")){
            System.out.println("Operación cancelada. El producto no fue eliminado");
            return;
        }

        //Eliminar la entrada correspondiente del inventario
        inventario.remove(codigo);
        System.out.println("Producto - " + producto.getNombre() + " - eliminado correctamente");
        return;
    }

    public void administrarEmpleados(Map<String,Usuario> usuarios, Scanner read) {
        String opcion;
        do {
            System.out.println();
            System.out.println("**** ADMINISTRAR EMPLEADOS ****");
            System.out.println("1. Crear empleado.");
            System.out.println("2. Eliminar empleado.");
            System.out.println("0. Volver al menú principal.");
            opcion = read.nextLine().trim();
            switch (opcion) {
                case "1":
                    crearEmpleado(usuarios, read);
                    break;
                case "2":
                    eliminarEmpleado(usuarios, read);
                case "0":
                    break;
            }
        } while (!opcion.equals("0"));
    }

    private void crearEmpleado(Map<String, Usuario> usuarios, Scanner read){
        System.out.println("**** CREAR EMPLEADO ****");
        System.out.println("Nombre completo: ");
        String nombre = read.nextLine().trim();
        System.out.println("Usuario: ");
        String nombreUsuario = read.nextLine().trim();
        System.out.println("Contraseña: ");
        String contrasena = read.nextLine();

        if (nombre.isBlank() || nombreUsuario.isBlank() || contrasena.isBlank()) {
            System.out.println("Todos los campos son obligatorios.");
            System.out.println("El empleado no fue creado.");
            return;
        }

        if (nombreUsuario.equals("salir")) {
            System.out.println("Ese nombre de usuario no está permitido.");
            System.out.println("El empleado no fue creado.");
            return;
        }

        if (usuarios.containsKey(nombreUsuario)) {
            System.out.println("Ya existe un usuario con ese nombre.");
            System.out.println("El empleado no fue creado.");
            return;
        }

        usuarios.put(nombreUsuario, new Empleado(nombre, nombreUsuario, contrasena));
        System.out.println("Empleado: " + nombre +
                "\nUsuario: " + nombreUsuario +
                "\nCreado correctamente.");
    }

    private void eliminarEmpleado(Map<String, Usuario> usuarios, Scanner read) {
        System.out.println("**** ELIMINAR EMPLEADO ****");
        System.out.print("Usuario del empleado: ");
        String nombreUsuario = read.nextLine().trim().toLowerCase();

        Usuario encontrado = usuarios.get(nombreUsuario);
        if (encontrado == null) {
            System.out.println("No existe ningun usuario con ese nombre.");
            return;
        }

        if (encontrado instanceof Propietario) {
            System.out.println("No se puede eliminar un usuario administrador.");
            return;
        }

        System.out.println("Empleado encontrado: " + encontrado.getNombre() +
                "\nNombre de usuario: " + encontrado.getUsuario());
        String respuesta;
        do {
            System.out.println("Escribe SI para eliminar o NO para cancelar.");
            respuesta = read.nextLine().trim().toUpperCase();
        } while (!respuesta.equals("SI") && !respuesta.equals("NO"));

        if (respuesta.equals("SI")) {
            usuarios.remove(nombreUsuario);
            System.out.println("Empleado eliminado correctamente.");
        } else {
            System.out.println("Eliminación cancelada.");
        }
    }
}