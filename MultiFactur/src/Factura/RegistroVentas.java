package Factura;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class RegistroVentas {
    private final Path archivo;
    private final List<Venta> ventas = new ArrayList<>();

    public RegistroVentas(Path archivo) throws IOException {
        this.archivo = archivo.toAbsolutePath();
        cargar();
    }

    private void cargar() throws IOException {
        if (!Files.exists(archivo)) {
            return;
        }

        try (DataInputStream entrada =
                     new DataInputStream(Files.newInputStream(archivo))) {

            if (!"MULTIFACTUR-1".equals(entrada.readUTF())) {
                throw new IOException("Formato de ventas no reconocido.");
            }

            int numeroVentas = entrada.readInt();

            if (numeroVentas < 0) {
                throw new IOException("Archivo de ventas inválido.");
            }

            Set<Integer> identificadores = new HashSet<>();

            for (int i = 0; i < numeroVentas; i++) {
                int id = entrada.readInt();

                if (id <= 0 || !identificadores.add(id)) {
                    throw new IOException(
                            "Número de venta inválido o repetido."
                    );
                }

                LocalDate fecha = LocalDate.parse(entrada.readUTF());
                Venta venta = new Venta(id, fecha);

                int numeroDetalles = entrada.readInt();

                if (numeroDetalles <= 0) {
                    throw new IOException("Venta sin detalles.");
                }

                for (int j = 0; j < numeroDetalles; j++) {
                    String codigo = entrada.readUTF();
                    String nombre = entrada.readUTF();
                    int cantidad = entrada.readInt();
                    double precio = entrada.readDouble();

                    venta.agregarDetalleGuardado(
                            new DetalleVenta(codigo, nombre, cantidad, precio)
                    );
                }

                ventas.add(venta);
            }

            if (entrada.read() != -1) {
                throw new IOException(
                        "Contenido inesperado en el archivo de ventas."
                );
            }

        } catch (RuntimeException e) {
            throw new IOException(
                    "No se pudieron leer las ventas guardadas.",
                    e
            );
        }
    }

    public int siguienteId() {
        int maximo = 0;

        for (Venta venta : ventas) {
            maximo = Math.max(maximo, venta.getIdVenta());
        }

        return Math.addExact(maximo, 1);
    }

    public void guardar(Venta venta) throws IOException {
        if (venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede guardar una venta vacía."
            );
        }

        if (venta.getIdVenta() <= 0
                || buscarPorId(venta.getIdVenta()) != null) {
            throw new IllegalArgumentException(
                    "Número de venta inválido o repetido."
            );
        }

        List<Venta> nuevasVentas = new ArrayList<>(ventas);
        nuevasVentas.add(venta);

        Files.createDirectories(archivo.getParent());

        Path temporal = Files.createTempFile(
                archivo.getParent(),
                "ventas-",
                ".tmp"
        );

        try {
            try (DataOutputStream salida =
                         new DataOutputStream(Files.newOutputStream(temporal))) {

                salida.writeUTF("MULTIFACTUR-1");
                salida.writeInt(nuevasVentas.size());

                for (Venta actual : nuevasVentas) {
                    salida.writeInt(actual.getIdVenta());
                    salida.writeUTF(actual.getFecha().toString());
                    salida.writeInt(actual.getDetalles().size());

                    for (DetalleVenta detalle : actual.getDetalles()) {
                        salida.writeUTF(detalle.getCodigoProducto());
                        salida.writeUTF(detalle.getNombreProducto());
                        salida.writeInt(detalle.getCantidad());
                        salida.writeDouble(detalle.getPrecioUnitario());
                    }
                }
            }

            try {
                Files.move(
                        temporal,
                        archivo,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(
                        temporal,
                        archivo,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

            Venta copia = new Venta(
                    venta.getIdVenta(),
                    venta.getFecha()
            );

            for (DetalleVenta detalle : venta.getDetalles()) {
                copia.agregarDetalleGuardado(detalle);
            }

            ventas.add(copia);

        } finally {
            Files.deleteIfExists(temporal);
        }
    }

    public List<Venta> listar() {
        return Collections.unmodifiableList(ventas);
    }

    public List<Venta> buscarPorFecha(LocalDate fecha) {
        List<Venta> resultado = new ArrayList<>();

        for (Venta venta : ventas) {
            if (venta.getFecha().equals(fecha)) {
                resultado.add(venta);
            }
        }

        return resultado;
    }

    public Venta buscarPorId(int id) {
        for (Venta venta : ventas) {
            if (venta.getIdVenta() == id) {
                return venta;
            }
        }

        return null;
    }

    public List<String> historialProducto(String codigo) {
        List<String> resultado = new ArrayList<>();

        for (Venta venta : ventas) {
            long cantidad = 0;

            for (DetalleVenta detalle : venta.getDetalles()) {
                if (detalle.getCodigoProducto().equalsIgnoreCase(codigo)) {
                    cantidad += detalle.getCantidad();
                }
            }

            if (cantidad > 0) {
                resultado.add(
                        venta.getFecha()
                                + " | Venta " + venta.getIdVenta()
                                + " | Cantidad: " + cantidad
                );
            }
        }

        return resultado;
    }

    public Map<String, Long> productosMasVendidos() {
        Map<String, Long> cantidades = new TreeMap<>();

        for (Venta venta : ventas) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                cantidades.merge(
                        detalle.getCodigoProducto(),
                        (long) detalle.getCantidad(),
                        Long::sum
                );
            }
        }

        List<Map.Entry<String, Long>> orden =
                new ArrayList<>(cantidades.entrySet());

        orden.sort(
                Map.Entry.<String, Long>comparingByValue()
                        .reversed()
                        .thenComparing(Map.Entry.comparingByKey())
        );

        Map<String, Long> resultado = new LinkedHashMap<>();

        for (Map.Entry<String, Long> elemento : orden) {
            resultado.put(elemento.getKey(), elemento.getValue());
        }

        return resultado;
    }

    public String nombreProducto(String codigo) {
        for (int i = ventas.size() - 1; i >= 0; i--) {
            for (DetalleVenta detalle : ventas.get(i).getDetalles()) {
                if (detalle.getCodigoProducto().equals(codigo)) {
                    return detalle.getNombreProducto();
                }
            }
        }

        return codigo;
    }
}