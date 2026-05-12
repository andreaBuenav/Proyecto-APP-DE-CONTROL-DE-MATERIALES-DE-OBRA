package com.example.control_material.inventory;

public class InventarioModelo {
    private int materialId;
    private String nombreMaterial;
    private String unidadMedida;
    private double stockActual;
    private double stockMinimo;
    private double precioUnitario;
    private boolean alertaActiva;

    public InventarioModelo() {}

    public InventarioModelo(int materialId, String nombreMaterial, String unidadMedida, double stockActual, double stockMinimo, double precioUnitario) {
        this.materialId = materialId;
        this.nombreMaterial = nombreMaterial;
        this.unidadMedida = unidadMedida;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.precioUnitario = precioUnitario;
        this.alertaActiva = stockActual <= stockMinimo;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getNombreMaterial() {
        return nombreMaterial;
    }

    public void setNombreMaterial(String nombreMaterial) {
        this.nombreMaterial = nombreMaterial;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public double getStockActual() {
        return stockActual;
    }

    public void setStockActual(double stockActual) {
        this.stockActual = stockActual;
        this.alertaActiva = stockActual <= stockMinimo;
    }

    public double getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(double stockMinimo) {
        this.stockMinimo = stockMinimo;
        this.alertaActiva = stockActual <= stockMinimo;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public boolean isAlertaActiva() {
        return alertaActiva;
    }
}
