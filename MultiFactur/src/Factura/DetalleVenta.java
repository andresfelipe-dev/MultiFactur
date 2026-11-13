package Factura;

import Inventario.Producto;

// Guarda los datos originales, aunque después cambie el producto.
public class DetalleVenta {
    private final String codigoProducto;
    private final String nombreProducto;
    private final int cantidad;
    private final double precioUnitario;

    public DetalleVenta(Producto producto, int cantidad) {
        this(
                producto.getCodigo(),
                producto.getNombre(),
                cantidad,
                producto.getPrecioVenta()
        );
    }

    public DetalleVenta(
            String codigo,
            String nombre,
            int cantidad,
            double precio) {

        if (cantidad <= 0 || !Double.isFinite(precio) || precio <= 0) {
            throw new IllegalArgumentException(
                    "Cantidad y precio deben ser positivos."
            );
        }

        this.codigoProducto = codigo;
        this.nombreProducto = nombre;
        this.cantidad = cantidad;
        this.precioUnitario = precio;
    }

    public double calcularSubtotal() {
        return precioUnitario * cantidad;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }
}