
package com.alejandro.servidormonitoreosalud.controller;

import com.alejandro.servidormonitoreosalud.model.TickRateManager;
import com.alejandro.servidormonitoreosalud.model.WSHealthMonitor;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.Session;


public class QualityController {
        
    public static Set<SensorInfo> sensor_caches = new HashSet<>();
    
    public static void checkValues(SensorInfo info){
        
        // CHEQUEO DE INFORMACION SENSIBLE
        // CHECAR INDICE DE FALLA DEL SENSOR
        // PORCENTAJE DE DIFERENCIA ENTRE EL PREVIO VALOR DEL SENSOR
        // Y EL ACTUAL
        
        if (!validatePorcentageThresshold()) {
            WSHealthMonitor.tickRateManager.SendAlertMessage(WSHealthMonitor.GetSesionById(info.getId()), 
                    "Desvio de valores del sensor de vida, esperando nuevo envio de datos",
                    TickRateManager.AlertColors.RED.toString());
            return;
        }
        System.out.println("Consultando");
        Session sesion = WSHealthMonitor.GetSesionById(info.getId());
        if (info.getTemperatura() >= 38.0) {
            // DO SOMETHING
            // CALENTURA
            WSHealthMonitor.tickRateManager.SendAlertMessage( sesion , "Propenso a CALENTURA, favor de consultar un especialista de la salud",TickRateManager.AlertColors.ORANGE.toString());
            
        } else if (info.getTemperatura() <= 32){
            // HIPOTERMIA
            WSHealthMonitor.tickRateManager.SendAlertMessage( sesion , "Propenso a HIPOTERMIA, favor de consultar un especialista de la salud",TickRateManager.AlertColors.BLUE.toString());
        }
       
        if (info.getRitmoCardiaco() >= 120) {
            // DO SOMETHING
            // TAQUICARDIA
            WSHealthMonitor.tickRateManager.SendAlertMessage( sesion , "Propenso a TAQUICARDIA, favor de consultar un especialista de la salud",TickRateManager.AlertColors.ORANGE.toString());

        } else if(info.getRitmoCardiaco() <= 40){
            // BRADICARDIA
            WSHealthMonitor.tickRateManager.SendAlertMessage( sesion , "Propenso a BRADICARDIA, favor de consultar un especialista de la salud",TickRateManager.AlertColors.CYAN.toString());

        }
        
        if (info.getPresionArterial() >= 120) {
            // DO SOMETHING
            // presion elevada, prehipertension
            WSHealthMonitor.tickRateManager.SendAlertMessage( sesion , "Propenso a PREHIPERTENSION, favor de consultar un especialista de la salud",TickRateManager.AlertColors.YELLOW.toString());

            
            if (info.getPresionArterial() >= 180) {
                // crisis hipertensiva
                WSHealthMonitor.tickRateManager.SendAlertMessage( sesion , "Propenso a CRISIS HIPERTENSIVA, favor de consultar un especialista de la salud",TickRateManager.AlertColors.ORANGE.toString());

            }
        } else if(info.getPresionArterial() <= 90){
            //HIPOTENSION
            WSHealthMonitor.tickRateManager.SendAlertMessage( sesion , "Propenso a HIPOTENSION, favor de consultar un especialista de la salud",TickRateManager.AlertColors.BLUE.toString());

        }
        
        StoreValues(info);
        
        
    }
    
    private static boolean validatePorcentageThresshold(){
        return true;
    }
    
    public static void StoreValues(SensorInfo info){
        sensor_caches.add(info);
        DBController.SendSensorInfo(info);
    }
    
}
