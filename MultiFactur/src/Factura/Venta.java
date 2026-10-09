package Factura;

import Inventario.Producto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Venta {
    private int idVenta;
    private LocalDate fecha;
    private double total;
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta(int idVenta) {
        this.idVenta = idVenta;
        this.fecha = LocalDate.now();
        this.total = 0;
    }

    //esto limita que el valor sea menor a 0
    public void agregarItem(Producto prod, int cant) {
        if (cant <= 0) {
            System.out.println("La cantidad debe ser mayor a 0.");
        }

        // Se resta lo que ya se agrego de este mismo producto en esta venta
        int disponible = prod.getCantidadActual() - cantidadAgregada(prod);
        if (cant > disponible) {
            System.out.println("Solo quedan " + disponible + " en stock.");
        }

        //agrega a la lista de DetalleVenta
        detalles.add(new DetalleVenta(prod, cant));
    }

    // Revision final antes de descontar: ningun producto puede superar su stock
    public void validarStock() {
        for (DetalleVenta d : detalles) {
            Producto p = d.getProducto();
            if (cantidadAgregada(p) > p.getCantidadActual()) {
                System.out.println("Stock insuficiente de " + p.getNombre() + ".");
            }
        }
    }

    // Antes: total = total + subtotal;
    public double calcularTotal() {
        total = 0;
        for (DetalleVenta d : detalles) {
            total += d.calcularSubtotal();
        }
        return total;
    }

    // Antes: el bloque de println del recibo (ahora lo imprime Comprobante)
    public Combrobante generarComprobante() {
        return new Combrobante("C-" + String.format("%04d", idVenta), fecha);
    }

    // Suma lo ya agregado de un mismo producto (se puede escanear el mismo codigo dos veces)
    private int cantidadAgregada(Producto prod) {
        int suma = 0;
        for (DetalleVenta d : detalles) {
            if (d.getProducto() == prod) {
                suma += d.getCantidad();
            }
        }
        return suma;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }
}


