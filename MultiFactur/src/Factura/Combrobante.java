package Factura;

import java.time.LocalDate;

public class Combrobante {
    private String numero;
    private LocalDate fecha;

    public Combrobante(String numero, LocalDate fecha) {
        this.numero = numero;
        this.fecha = fecha;
    }

    // imprime la factura de la venta
    public void imprimir(Venta venta) {
        System.out.println("\n===== RECIBO " + numero + " =====");
        System.out.println("Fecha: " + fecha);
        for (DetalleVenta d : venta.getDetalles()) {
            System.out.println(d.getProducto().getNombre() + " x" + d.getCantidad()
                    + " = $" + d.calcularSubtotal());
        }
        System.out.println("TOTAL A COBRAR: $" + venta.getTotal());
        System.out.println("==================");
    }

    public String getNumero() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
