package Persona;
import Inventario.Producto;
import java.util.Map;
import java.util.Scanner;

public class Propietario extends Empleado {

    // Constructor
    public Propietario(int id, String nombre, String usuario, String contrasena) {
        super(id, nombre, usuario, contrasena);
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
            System.out.println("codigo: " + producto.getCodigo() + "Nombre: " + producto.getNombre());
        }
        System.out.println("Ingresar el codigo del producto: ");

        String codigo = read.nextLine().trim();

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

        String confirmacion = read.nextLine().trim();
        if (!confirmacion.equalsIgnoreCase("SI")
        && !confirmacion.equalsIgnoreCase("SÍ")){
            System.out.println("Operación cancelada. El producto no fue eliminado");
            return;
        }

        //Eliminar la entrada correspondiente del inventario
        inventario.remove(codigo);
        System.out.println("Producto - " + producto.getNombre() + " - eliminado correctamente");
    }
}
