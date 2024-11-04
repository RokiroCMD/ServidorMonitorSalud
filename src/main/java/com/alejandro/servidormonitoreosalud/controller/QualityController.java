
package com.alejandro.servidormonitoreosalud.controller;

import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import java.util.HashSet;
import java.util.Set;


public class QualityController {
        
    public static Set<SensorInfo> sensor_caches = new HashSet<>();
    
    public static void checkValues(SensorInfo info){
        
        // CHEQUEO DE INFORMACION SENSIBLE
        if (info.getTemperatura() >= 38.0) {
            // DO SOMETHING
            // CALENTURA
        } else if (info.getTemperatura() <= 32){
            // HIPOTERMIA
        }
       
        if (info.getRitmoCardiaco() >= 150) {
            // DO SOMETHING
            // TAQUICARDIA
        } else if(info.getRitmoCardiaco() <= 40){
            // BRADICARDIA
        }
        
        if (info.getPresionArteril() >= 120) {
            // DO SOMETHING
            // presion elevada, prehipertension
            
            if (info.getPresionArteril() >= 180) {
                // crisis hipertensiva
            }
        } else if(info.getPresionArteril() <= 90){
            //HIPOTENSION
        }
        
        // CHECAR INDICE DE FALLA DEL SENSOR
        // PORCENTAJE DE DIFERENCIA ENTRE EL PREVIO VALOR DEL SENSOR
        // Y EL ACTUAL
        
        // TO DO 
        if (validatePorcentageThresshold()) {
            StoreValues(info);
        }
    }
    
    private static boolean validatePorcentageThresshold(){
        return true;
    }
    
    public static void StoreValues(SensorInfo info){
        sensor_caches.add(info);
        DBController.SendSensorInfo(info);
    }
    
}
