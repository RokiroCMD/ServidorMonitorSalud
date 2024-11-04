package com.alejandro.servidormonitoreosalud.model;

import com.alejandro.servidormonitoreosalud.controller.DBController;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import com.google.gson.JsonObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

public class TickRateManager extends Thread {

    private int TICK_RATE = 2;
    boolean canTick = false;

    public void iniciarTickRate() {
        if (!this.isAlive()) {
            canTick = true;
            this.start();
        }
    }

    public void detenerTickRate() {
        if (this.isAlive()) {
            //canTick = false;
        }
    }

    @Override
    public void run() {
        while (canTick) {
            try {
                onTick();
                Thread.sleep(1000 * TICK_RATE);
            } catch (InterruptedException ex) {
                Logger.getLogger(TickRateManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    // Método para enviar mensajes a todas las sesiones en un tiempo establecido
    public void onTick() {
        for (Session sesion : WSHealthMonitor.connections) {
            if (sesion.isOpen()) {
                try {

                    SensorInfo info = DBController.GetSensorInfoData(sesion.getUserProperties().get("username").toString());

                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("temperatura", info.getTemperatura());
                    jsonObject.addProperty("ritmo", info.getRitmoCardiaco());
                    jsonObject.addProperty("presion", info.getPresionArteril());

                    sesion.getBasicRemote().sendText(jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
