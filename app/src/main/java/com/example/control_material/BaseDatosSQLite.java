package com.example.control_material;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.control_material.inventory.InventarioModelo;

import java.util.ArrayList;
import java.util.List;

public class BaseDatosSQLite extends SQLiteOpenHelper {

    public static final String dbName="dbControlMaterial.sqlite";
    public static final int dbversion = 3;
    public static final  String tableUsuario="CREATE TABLE usuario ("
            + "usuario_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "nombre TEXT,"
            + "apellido TEXT,"
            + "edad INTEGER,"
            + "cedula INTEGER,"
            + "nacionalidad TEXT,"
            + "genero TEXT,"
            + "fechaNac TEXT,"
            + "estadoCivil TEXT,"
            + "username TEXT,"
            + "password TEXT,"
            + "nivelIngles FLOAT,"
            + "estado INTEGER)";
    public static final  String tableMaterial ="CREATE TABLE material ("
        + "material_id INTEGER PRIMARY KEY AUTOINCREMENT,"
        + "nombre TEXT,"
        + "descripcion TEXT,"
        + "unidad_medida TEXT,"
        + "stock_actual REAL,"
        + "stock_minimo REAL,"
        + "precio_unitario REAL,"
        + "estado INTEGER)";
    public static final  String tableObra="CREATE TABLE obra ("
            + "obra_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "nombre TEXT,"
            + "ubicacion TEXT,"
            + "fecha_inicio TEXT,"
            + "fecha_fin TEXT,"
            + "estado TEXT)";

    public static final  String tableEntradaMaterial="CREATE TABLE entrada_material ("
            + "entrada_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "usuario_id INTEGER,"
            + "fecha_entrada TEXT,"
            + "observacion TEXT,"
            + "FOREIGN KEY(usuario_id) REFERENCES usuario(usuario_id))";


    public static final  String tableDetalleEntrada="CREATE TABLE detalle_entrada ("
            + "detalle_entrada_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "entrada_id INTEGER,"
            + "material_id INTEGER,"
            + "cantidad REAL,"
            + "precio_unitario REAL,"
            + "FOREIGN KEY(entrada_id) REFERENCES entrada_material(entrada_id),"
            + "FOREIGN KEY(material_id) REFERENCES material(material_id))";

    public static final  String tableUsoMaterial="CREATE TABLE uso_material ("
            + "uso_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "obra_id INTEGER,"
            + "usuario_id INTEGER,"
            + "fecha_uso TEXT,"
            + "actividad TEXT,"
            + "observacion TEXT,"
            + "FOREIGN KEY(obra_id) REFERENCES obra(obra_id),"
            + "FOREIGN KEY(usuario_id) REFERENCES usuario(usuario_id))";

    public static final  String tableDetalleUso="CREATE TABLE detalle_uso ("
            + "detalle_uso_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "uso_id INTEGER,"
            + "material_id INTEGER,"
            + "cantidad REAL,"
            + "FOREIGN KEY(uso_id) REFERENCES uso_material(uso_id),"
            + "FOREIGN KEY(material_id) REFERENCES material(material_id))";

    public static final  String tableAlertaStock="CREATE TABLE alerta_stock ("
            + "alerta_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "material_id INTEGER,"
            + "fecha_alerta TEXT,"
            + "mensaje TEXT,"
            + "atendida INTEGER,"
            + "FOREIGN KEY(material_id) REFERENCES material(material_id))";

    public BaseDatosSQLite (Context context){
       super(context, dbName, null, dbversion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tableUsuario);
        db.execSQL(tableMaterial);
        db.execSQL(tableEntradaMaterial);
        db.execSQL(tableDetalleEntrada);
        db.execSQL(tableUsoMaterial);
        db.execSQL(tableDetalleUso);
        db.execSQL(tableObra);
        db.execSQL(tableAlertaStock);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Métodos del Módulo de Inventario

    public List<InventarioModelo> obtenerInventario() {
        List<InventarioModelo> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT material_id, nombre, unidad_medida, stock_actual, stock_minimo, precio_unitario FROM material", null);

        if (cursor.moveToFirst()) {
            do {
                InventarioModelo modelo = new InventarioModelo();
                modelo.setMaterialId(cursor.getInt(0));
                modelo.setNombreMaterial(cursor.getString(1));
                modelo.setUnidadMedida(cursor.getString(2));
                modelo.setStockActual(cursor.getDouble(3));
                modelo.setStockMinimo(cursor.getDouble(4));
                modelo.setPrecioUnitario(cursor.getDouble(5));
                
                lista.add(modelo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public void verificarYRegistrarAlertaStock(int materialId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT stock_actual, stock_minimo FROM material WHERE material_id = ?", new String[]{String.valueOf(materialId)});
        
        if (cursor.moveToFirst()) {
            double stockActual = cursor.getDouble(0);
            double stockMinimo = cursor.getDouble(1);

            if (stockActual <= stockMinimo) {
                // Verificar si ya existe una alerta reciente no atendida para no duplicar innecesariamente
                Cursor cursorAlerta = db.rawQuery("SELECT alerta_id FROM alerta_stock WHERE material_id = ? AND atendida = 0", new String[]{String.valueOf(materialId)});
                if (!cursorAlerta.moveToFirst()) {
                    // No hay alerta activa, la creamos
                    String fechaActual = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());
                    ContentValues values = new android.content.ContentValues();
                    values.put("material_id", materialId);
                    values.put("fecha_alerta", fechaActual);
                    values.put("mensaje", "Nivel de stock crítico");
                    values.put("atendida", 0);
                    db.insert("alerta_stock", null, values);
                }
                cursorAlerta.close();
            }
        }
        cursor.close();
        db.close();
    }
}
