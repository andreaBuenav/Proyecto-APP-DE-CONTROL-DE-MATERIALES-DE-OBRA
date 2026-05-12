package com.example.control_material;

public class ReporteModelo {
    private String titulo;
    private String descripcion;
    private String detalle;

    public ReporteModelo(String titulo, String descripcion, String detalle) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.detalle = detalle;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDetalle() {
        return detalle;
    }
}
