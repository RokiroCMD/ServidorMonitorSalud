
package com.alejandro.servidormonitoreosalud.model;

import com.alejandro.servidormonitoreosalud.controller.QualityController;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionMQTT {
    
    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String TOPIC = "test/sensor";
    private static MqttClient client;
    
    public static ConnectionMQTT instance;
    
    public static void InnitConnection(){
        if (instance == null) {
            try {
                instance = new ConnectionMQTT();
                instance.connect();
                System.out.println("Servidor MQTT iniciado");
            } catch (MqttException ex) {
                System.out.println("Error al iniciar servidor MQTT");
            }
        }
    }
    
    public void disconnet(){
        if (client.isConnected()) {
            try {
                client.disconnect();
            } catch (MqttException ex) {
                Logger.getLogger(ConnectionMQTT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void connect() throws MqttException {
        client = new MqttClient(BROKER_URL, MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false); // Cambiado a true para pruebas
        options.setAutomaticReconnect(true);
        
        client.connect(options);
        
        if (client.isConnected()) {
            System.out.println("Conectado al broker MQTT.");
            client.subscribe(TOPIC);
            System.out.println("Suscrito al topic: " + TOPIC);
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Conexión perdida, causa: " + cause);
                try {
                    reconnect();
                } catch (MqttException e) {
                    System.out.println("-");
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                try {
                    String mensaje = new String(message.getPayload(), "UTF-8");
                    //System.out.println("Mensaje recibido: " + mensaje);
                    JsonObject json = JsonParser.parseString(mensaje).getAsJsonObject();
                    String id =json.get("id").getAsString();
                    double temperatura = json.get("temperatura").getAsDouble();
                    double presion = json.get("presion").getAsDouble();
                    double ritmo = json.get("ritmo").getAsDouble();
                    SensorInfo info = new SensorInfo(id,temperatura,ritmo,presion);
                    QualityController.checkValues(info);
                } catch (Exception e) {
                    System.err.println("Error al decodificar el mensaje: " + e.getMessage());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Entrega completa.");
            }
        });
    }

    private void reconnect() throws MqttException {
        while (!client.isConnected()) {
            try {
                System.out.println("Intentando reconectar...");
                client.connect();
                client.subscribe(TOPIC);
                System.out.println("Reconectado al broker y suscrito de nuevo.");
            } catch (MqttException e) {
                System.out.println("Error al reconectar, intentando nuevamente...");
                try {
                    Thread.currentThread().sleep(4000); 
                } catch (InterruptedException ie) {
                    
                }
            }
        }
    }
}
