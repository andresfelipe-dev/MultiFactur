package Factura;

import Inventario.Producto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Venta {
    private final int idVenta;
    private final LocalDate fecha;
    private final List<DetalleVenta> detalles = new ArrayList<>();

    public Venta(int idVenta) {
        this(idVenta, LocalDate.now());
    }

    public Venta(int idVenta, LocalDate fecha) {
        this.idVenta = idVenta;
        this.fecha = fecha;
    }

    public void agregarItem(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a cero."
            );
        }

        long cantidadAgregada = 0;

        for (DetalleVenta detalle : detalles) {
            if (detalle.getCodigoProducto().equals(producto.getCodigo())) {
                cantidadAgregada += detalle.getCantidad();
            }
        }

        if (cantidadAgregada + cantidad > producto.getCantidadActual()) {
            throw new IllegalArgumentException(
                    "Stock insuficiente de " + producto.getNombre()
            );
        }

        detalles.add(new DetalleVenta(producto, cantidad));
    }

    // Recupera detalles de ventas guardadas sin descontar inventario.
    void agregarDetalleGuardado(DetalleVenta detalle) {
        detalles.add(detalle);
    }

    public double calcularTotal() {
        double total = 0;

        for (DetalleVenta detalle : detalles) {
            total += detalle.calcularSubtotal();
        }

        return total;
    }

    public Combrobante generarComprobante() {
        return new Combrobante(
                String.format("C-%04d", idVenta),
                fecha
        );
    }

    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
    }

    public int getIdVenta() {
        return idVenta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getTotal() {
        return calcularTotal();
    }

    public List<DetalleVenta> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }
}