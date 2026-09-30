package Inventario;

import java.time.LocalDate;

public class Producto {
    private String codigo;
    private String name;
    private double precioVenta;
    private double precioCosto;
    private int cantidadActual;
    private int cantidadMinima;
    private LocalDate fechaVencimiento;
    private boolean activo;
    private Categoria categoria;
    //Constructor
    public Producto(String codigo, String name, double precioVenta, double precioCosto, int cantidadActual,
                    int cantidadMinima, LocalDate fechaVencimiento, boolean activo, Categoria categoria) {
        this.codigo = codigo;
        this.name = name;
        this.precioVenta = precioVenta;
        this.precioCosto = precioCosto;
        this.cantidadActual = cantidadActual;
        this.cantidadMinima = cantidadMinima;
        this.fechaVencimiento = fechaVencimiento;
        this.activo = activo;
        this.categoria = categoria;
    }

    //Getter y Setter

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(double precioCosto) {
        this.precioCosto = precioCosto;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(int cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    //Metodos

}
