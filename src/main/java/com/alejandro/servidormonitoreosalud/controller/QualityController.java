package com.alejandro.servidormonitoreosalud.controller;

import com.alejandro.servidormonitoreosalud.model.TickRateManager;
import com.alejandro.servidormonitoreosalud.model.WSHealthMonitor;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.websocket.Session;
        
public class QualityController {

    public static Map<String, SensorInfo> sensor_caches = new HashMap<>();
    
    private static final float validatePorcentage = 30f;

    public static void checkValues(SensorInfo info) {

        // CHEQUEO DE INFORMACION SENSIBLE
        // CHECAR INDICE DE FALLA DEL SENSOR
        // PORCENTAJE DE DIFERENCIA ENTRE EL PREVIO VALOR DEL SENSOR
        // Y EL ACTUAL
        Entry<SensorInfo, Boolean[]> cacheResponse = ValidateSafeValues(info);

        info = cacheResponse.getKey();

        Session sesion = WSHealthMonitor.GetSesionById(info.getId());
        if (sesion != null) {
            HandleAlerts(cacheResponse, info, sesion);
        }
        
        if (!(!cacheResponse.getValue()[0] && !cacheResponse.getValue()[1] && !cacheResponse.getValue()[2] )) {
            System.out.println("guardando informacion");
            StoreValues(info);
        }
    }
    
    private static void HandleAlerts(Entry<SensorInfo, Boolean[]> cacheResponse, SensorInfo info, Session sesion){
        // If temperature values are safe send notification of health warning
        if (cacheResponse.getValue()[0]) {
            if (info.getTemperatura() >= 38.0) {
                // DO SOMETHING
                // CALENTURA
                WSHealthMonitor.tickRateManager.SendAlertMessage(
                        sesion, "Propenso a CALENTURA, favor de consultar un especialista de la salud", 
                        TickRateManager.AlertColors.ORANGE.toString());

            } else if (info.getTemperatura() <= 32) {
                // HIPOTERMIA
                WSHealthMonitor.tickRateManager.SendAlertMessage(sesion, "Propenso a HIPOTERMIA, favor de consultar un especialista de la salud", TickRateManager.AlertColors.BLUE.toString());
            }
        }

        // If heath rythm values are safe send notification of health warning
        if (cacheResponse.getValue()[1]) {
            if (info.getRitmoCardiaco() >= 120) {
                // TAQUICARDIA
                WSHealthMonitor.tickRateManager.SendAlertMessage(sesion, "Propenso a TAQUICARDIA, favor de consultar un especialista de la salud", TickRateManager.AlertColors.ORANGE.toString());

            } else if (info.getRitmoCardiaco() <= 40) {
                // BRADICARDIA
                WSHealthMonitor.tickRateManager.SendAlertMessage(sesion, "Propenso a BRADICARDIA, favor de consultar un especialista de la salud", TickRateManager.AlertColors.CYAN.toString());

            }
        }

        // If blood preassure values are safe send notification of health warning
        if (cacheResponse.getValue()[2]) {
            if (info.getPresionArterial() >= 120) {
                // DO SOMETHING
                // presion elevada, prehipertension
                WSHealthMonitor.tickRateManager.SendAlertMessage(sesion, "Propenso a PREHIPERTENSION, favor de consultar un especialista de la salud", TickRateManager.AlertColors.YELLOW.toString());

                if (info.getPresionArterial() >= 180) {
                    // crisis hipertensiva
                    WSHealthMonitor.tickRateManager.SendAlertMessage(sesion, "Propenso a CRISIS HIPERTENSIVA, favor de consultar un especialista de la salud", TickRateManager.AlertColors.ORANGE.toString());

                }
            } else if (info.getPresionArterial() <= 90) {
                //HIPOTENSION
                WSHealthMonitor.tickRateManager.SendAlertMessage(sesion, "Propenso a HIPOTENSION, favor de consultar un especialista de la salud", TickRateManager.AlertColors.BLUE.toString());
            }
        }
    }

    private static Entry<SensorInfo, Boolean[]> ValidateSafeValues(SensorInfo info) {
        SensorInfo correctedInfo = info;
        Boolean safeValues[] = new Boolean[]{true, true, true};
        SensorInfo cache = getCacheInfo(correctedInfo.getId());
        
        if (!(info.getTemperatura() >= 0 && info.getTemperatura() < 100))   safeValues[0] = false;
        if (!(info.getRitmoCardiaco() >= 0 && info.getRitmoCardiaco() < 230)) safeValues[1] = false;
        if (!(info.getPresionArterial() >= 0 && info.getRitmoCardiaco() < 170)) safeValues[2] = false;

        if (cache == null) {
            if (!safeValues[0] && !safeValues[1] && !safeValues[2]) {
                WSHealthMonitor.tickRateManager.SendAlertMessage(WSHealthMonitor.GetSesionById(correctedInfo.getId()),
                    "Error en los valores del sensor de vida, esperando nuevo envio de datos, se omitira el registro de este dato.",
                    TickRateManager.AlertColors.RED.toString());
            return Map.entry(correctedInfo, safeValues);
            }
        } else{
            if (!safeValues[0]) {
                correctedInfo.setTemperatura(cache.getTemperatura());
                WSHealthMonitor.tickRateManager.SendAlertMessage(WSHealthMonitor.GetSesionById(correctedInfo.getId()),
                    "Error en los valores del sensor de Temperatura, esperando nuevo envio de datos, se omitira el registro de este dato.",
                    TickRateManager.AlertColors.RED.toString());
            }
            if (!safeValues[1]) {
                correctedInfo.setRitmoCardiaco(cache.getRitmoCardiaco());
                WSHealthMonitor.tickRateManager.SendAlertMessage(WSHealthMonitor.GetSesionById(correctedInfo.getId()),
                    "Error en los valores del sensor de Ritmo Cardiaco, esperando nuevo envio de datos, se omitira el registro de este dato.",
                    TickRateManager.AlertColors.RED.toString());
            }
            if (!safeValues[2]) {
                correctedInfo.setPresionArteril(cache.getPresionArterial());
                WSHealthMonitor.tickRateManager.SendAlertMessage(WSHealthMonitor.GetSesionById(correctedInfo.getId()),
                    "Error en los valores del sensor de Presion Arterial, esperando nuevo envio de datos, se omitira el registro de este dato.",
                    TickRateManager.AlertColors.RED.toString());
            }
        }
        return validatePorcentageThresshold(correctedInfo, safeValues, cache);

    }

    private static Entry<SensorInfo, Boolean[]> validatePorcentageThresshold(SensorInfo correctedInfo, Boolean[] safeValues, SensorInfo cache) {
        
        boolean hasError = false;
        String errorsFound = "";
        if (cache == null) { return Map.entry(correctedInfo, safeValues); }
        float tempTresshold = (float) ((float) (cache.getTemperatura() / (1 - validatePorcentage / 100)) - cache.getTemperatura());
        float ritmoTresshold = (float) ((float) (cache.getRitmoCardiaco()/ (1 - validatePorcentage / 100)) - cache.getRitmoCardiaco());
        float presionTresshold = (float) ((float) (cache.getPresionArterial() / (1 - validatePorcentage / 100)) - cache.getPresionArterial());
        
        if (!(correctedInfo.getTemperatura() >= cache.getTemperatura() - tempTresshold 
                && correctedInfo.getTemperatura() <= cache.getTemperatura() + tempTresshold)) {
            safeValues[0] = false;
            errorsFound += "  TEMPERATURA";
            correctedInfo.setTemperatura(cache.getTemperatura());
            hasError = true;
        }

        if (!(correctedInfo.getRitmoCardiaco() >= cache.getRitmoCardiaco() - ritmoTresshold 
                && correctedInfo.getRitmoCardiaco() <= cache.getRitmoCardiaco() + ritmoTresshold)) {
            safeValues[1] = false;
            errorsFound += "  RITMO CARDIACO";
            correctedInfo.setRitmoCardiaco(cache.getRitmoCardiaco());
            hasError = true;
        }

        if (!(correctedInfo.getPresionArterial() >= cache.getPresionArterial() - presionTresshold 
                && correctedInfo.getPresionArterial() <= cache.getPresionArterial() + presionTresshold)) {
            safeValues[2] = false;
            errorsFound += "  PRESION ARTERIAL";
            correctedInfo.setPresionArteril(cache.getPresionArterial());
            hasError = true;
        }

        if (hasError) {
            WSHealthMonitor.tickRateManager.SendAlertMessage(WSHealthMonitor.GetSesionById(correctedInfo.getId()),
                    "Desvio de valores del sensor de vida en " + errorsFound + ", esperando nuevo envio de datos, se omitira el registro de este dato.",
                    TickRateManager.AlertColors.RED.toString());
        }

        return Map.entry(correctedInfo, safeValues);

    }

    private static SensorInfo getCacheInfo(String id) {

        for (Object cache : sensor_caches.entrySet()) {

            Entry<String, SensorInfo> cacheValue = (Entry<String, SensorInfo>) cache;

            if (cacheValue.getValue().getId().equals(id)) {
                return cacheValue.getValue();
            }
        }
        return null;
    }

    public static void StoreValues(SensorInfo info) {
        sensor_caches.put(info.getId(), info);
        DBController.SendSensorInfo(info);
    }

}
