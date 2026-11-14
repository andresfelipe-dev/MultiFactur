package Factura;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Locale;

public class Combrobante {
    private final String numero;
    private final LocalDate fecha;

    public Combrobante(String numero, LocalDate fecha) {
        this.numero = numero;
        this.fecha = fecha;
    }

    public String generarTexto(Venta venta) {
        StringBuilder texto = new StringBuilder();

        texto.append("===== MULTIFACTUR - RECIBO ")
                .append(numero)
                .append(" =====\n");

        texto.append("Fecha: ").append(fecha).append("\n");

        for (DetalleVenta detalle : venta.getDetalles()) {
            texto.append(String.format(
                    Locale.ROOT,
                    "%s | %s | Cantidad: %d | Precio unitario: $%.2f"
                            + " | Subtotal: $%.2f%n",
                    detalle.getCodigoProducto(),
                    detalle.getNombreProducto(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario(),
                    detalle.calcularSubtotal()
            ));
        }

        texto.append(String.format(
                Locale.ROOT,
                "TOTAL: $%.2f%n",
                venta.getTotal()
        ));

        texto.append("================================================\n");

        return texto.toString();
    }

    public void imprimir(Venta venta) {
        System.out.print(generarTexto(venta));
    }

    public Path exportar(Venta venta, Path carpeta) throws IOException {
        Files.createDirectories(carpeta);

        Path archivo = carpeta.resolve(numero + ".txt");

        Files.write(
                archivo,
                generarTexto(venta).getBytes(StandardCharsets.UTF_8)
        );

        return archivo.toAbsolutePath();
    }

    public String getNumero() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}