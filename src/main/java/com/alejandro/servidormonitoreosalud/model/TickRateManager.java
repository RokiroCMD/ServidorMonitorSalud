package com.alejandro.servidormonitoreosalud.model;

import com.alejandro.servidormonitoreosalud.controller.DBController;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

public class TickRateManager extends Thread {

    private int TICK_RATE = 10;
    boolean canTick = false;

    public void iniciarTickRate() {
        if (!this.isAlive()) {
            canTick = true;
            this.start();
        }
    }

    public void detenerTickRate() {
        if (this.isAlive()) {
            canTick = false;
            this.interrupt();
        }
    }

    @Override
    public void run() {
        while (canTick) {
            try {
                onTick();
                Thread.sleep(1000 * TICK_RATE);
            } catch (InterruptedException ex) {
                System.out.println("Se intentó parar el hilo del TickRateManager");
            }
        }

    }
    
    
    
    // Método para enviar mensajes a todas las sesiones en un tiempo establecido
    public void onTick() {
        for (Session sesion : WSHealthMonitor.connections) {
            if (sesion.isOpen()) {
                SensorInfo info = DBController.GetSensorInfoData(sesion.getUserProperties().get("username").toString());
                SendGaugesMessage(sesion, info);
            }
        }
    }

    private String FormatTime(Instant time) {
        LocalTime localTime = time.atZone(ZoneId.systemDefault()).toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return localTime.format(formatter);
    }

    public void SendGaugesMessage(Session sesion, SensorInfo info) {
        try {
            JsonObject jsonObject = new JsonObject();
            
            jsonObject.addProperty("tipoMensaje", "gauges");
            jsonObject.addProperty("temperatura", info.getTemperatura());
            jsonObject.addProperty("ritmo", info.getRitmoCardiaco());
            jsonObject.addProperty("presion", info.getPresionArterial());
            jsonObject.addProperty("tiempo", FormatTime(Instant.now()));
            
            sesion.getBasicRemote().sendText(jsonObject.toString());
        } catch (IOException ex) {
            Logger.getLogger(TickRateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void SendAlertMessage(Session sesion, String alertMessage, String alertColor){
        try {
            JsonObject jsonObject = new JsonObject();
            
            jsonObject.addProperty("tipoMensaje", "alert");
            jsonObject.addProperty("message", alertMessage);
            jsonObject.addProperty("color", alertColor);
            
            sesion.getBasicRemote().sendText(jsonObject.toString());
            System.out.println("Mandando alerta a JScript");
        } catch (IOException ex) {
            Logger.getLogger(TickRateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public enum AlertColors{
        RED("toast-red"),
        GREEN("toast-green"),
        YELLOW("toast-yellow"),
        ORANGE("toast-orange"),
        BLUE("toast-blue"),
        CYAN("toast-cyan");
        
        String color;

        private AlertColors(String colorHex) {
            this.color = colorHex;
        }

        public String getColor() {
            return color;
        }

        @Override
        public String toString() {
            return getColor();
        }
        
        
        
        
        
    }
    
}
