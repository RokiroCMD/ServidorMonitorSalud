
package com.alejandro.servidormonitoreosalud.model;

import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import com.alejandro.servidormonitoreosalud.controller.DBController;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/wsmonitor", configurator = WSMonitorConfigurator.class)
public class WSHealthMonitor {

    public static TickRateManager tickRateManager = new TickRateManager();
    
    public static Set<Session> connections = Collections.synchronizedSet(new HashSet<Session>());
    
    @OnOpen
    public void onOpen(EndpointConfig endpointConfig, Session userSession){
        try {
            connections.add(userSession);
            userSession.getUserProperties().put("username", endpointConfig.getUserProperties().get("username") );
            
            SensorInfo info = DBController.GetSensorInfoData(userSession.getUserProperties().get("username").toString());     
            
            JsonObject jsonObject = new JsonObject();
            
            jsonObject.addProperty("temperatura", info.getTemperatura());
            jsonObject.addProperty("ritmo", info.getRitmoCardiaco());
            jsonObject.addProperty("presion", info.getPresionArteril());
            
            userSession.getBasicRemote().sendText(jsonObject.toString());
            
            if (tickRateManager != null) {
                tickRateManager.iniciarTickRate();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(WSHealthMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @OnClose
    public void onClose(Session userSession){
            System.out.println("Cerrando");
            System.out.println("" + connections.size());
        if (connections.size()-1 <= 0) {
            System.out.println("Cerrando");
            tickRateManager.detenerTickRate();
        }
        connections.remove(userSession);
    }
    
    @OnMessage
    public String onMessage(String message) {
        return null;
    }
    
    
    public static void BroadcastMessage(String data){
        
        Iterator iterator = connections.iterator();
       
        while(iterator.hasNext()){
            try {
                ((Session)iterator.next()).getBasicRemote().sendText(data);
            } catch (IOException ex) {
                Logger.getLogger(WSHealthMonitor.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
}
