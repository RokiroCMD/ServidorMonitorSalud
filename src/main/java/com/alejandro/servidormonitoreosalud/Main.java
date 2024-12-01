
package com.alejandro.servidormonitoreosalud;

import com.alejandro.servidormonitoreosalud.controller.DBController;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import com.influxdb.query.FluxRecord;


public class Main {
    
    public static void main(String[] args) {
        SensorInfo info = new SensorInfo("Jocelyn", 30.8, 80.6, 112.0);
        IngresarDatos(info);
        MostrarDatos(info.getId());
    }
    
    private static void MostrarDatos(String id){
        
        FluxRecord[] records =  DBController.GetAllData(id);
        
        System.out.println("Datos encontrados en Base de datos");
        
        for (int i = 0; i < records.length; i++) {
            System.out.print(
                    "Measurement: " + records[i].getMeasurement() +
                    " Campo(" + records[i].getField() + 
                    ") Valor(" + records[i].getValue() +
                    ") tiempo(" + records[i].getTime().toString()+")\n");
        }
    }
    
    private static void IngresarDatos(SensorInfo info){
        DBController.SendSensorInfo(info);
    }
}
