package com.example.control_material.catalog;

public class MaterialModelo {

    private int materialId;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String unidadMedida;
    private double precioUnitario;

    public MaterialModelo() {}

    public MaterialModelo(int materialId, String nombre, String descripcion,
                          String categoria, String unidadMedida, double precioUnitario) {
        this.materialId = materialId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.unidadMedida = unidadMedida;
        this.precioUnitario = precioUnitario;
    }

    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}
