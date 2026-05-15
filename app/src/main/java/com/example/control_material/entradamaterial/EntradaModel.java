package com.example.control_material;

public class EntradaModel {

    String fecha;

    String material;

    double cantidad;

    double precio;

    public EntradaModel(
            String fecha,
            String material,
            double cantidad,
            double precio
    ) {

        this.fecha = fecha;

        this.material = material;

        this.cantidad = cantidad;

        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public String getMaterial() {
        return material;
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }
}