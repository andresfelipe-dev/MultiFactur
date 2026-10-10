package Inventario;

import java.time.LocalDate;

public class EntradaMercancia {
    private int idEntrada;
    private LocalDate fecha;
    private int cantidad;

    //Constructor
    public EntradaMercancia(int idEntrada, LocalDate fecha, int cantidad) {
        this.idEntrada = idEntrada;
        this.fecha = fecha;
        this.cantidad = cantidad;
    }

    //Getter y Setter
    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
