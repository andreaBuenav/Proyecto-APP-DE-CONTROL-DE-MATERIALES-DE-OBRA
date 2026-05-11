package com.example.control_material;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
