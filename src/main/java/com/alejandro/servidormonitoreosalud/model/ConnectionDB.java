
package com.alejandro.servidormonitoreosalud.model;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;


public class ConnectionDB {
    
    // PROPIEDAD CLIENTE LIBRE DE UTILIZAR Y MODIFICAR
    // A TRVES DEL SINGLETON
    public InfluxDBClient CLIENT;
    
    
    // VALORES BASICOS PARA ACCESO A CONSULTA Y ESCRITURA
    private static final String INF_URL = "http://localhost:8086"; // URL SERVICIO
    private static final String INF_TOKEN = "XryqkTQehXGXDQXs6BNcNC08q2L_7boGlVXTojrpubjBDPHEkla9cOF1iXKgi6VbeaJOH5NjzBggOJ2z7bDglg=="; // TOCKEN ACCESO
    private static final String INF_ORG = "Equipo9IoT"; // NOMBRE ORGANIZACION
    private static final String INF_BUCKET = "MonitorSalud"; // BUCKET

    // SINGLETON DE LA CONEXION CON BASE DE DATOS
    public static ConnectionDB instance = new ConnectionDB();
    
    
    public ConnectionDB() {
        CLIENT = InfluxDBClientFactory.create(INF_URL, INF_TOKEN.toCharArray(),INF_ORG, INF_BUCKET);
    }

    public static String getINF_URL() {
        return INF_URL;
    }
    public static String getINF_TOKEN() {
        return INF_TOKEN;
    }
    public static String getINF_ORG() {
        return INF_ORG;
    }
    public static String getINF_BUCKET() {
        return INF_BUCKET;
    }

    
    
    
}
