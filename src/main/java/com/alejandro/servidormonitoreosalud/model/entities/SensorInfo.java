
package com.alejandro.servidormonitoreosalud.model.entities;

import java.time.Instant;


public class SensorInfo {
    
    private String id;
    private double temperatura;
    private double ritmoCardiaco;
    private double presionArterial;

    public SensorInfo(String id, double temperatura, double ritmoCardiaco, double presionArterial) {
        this.id = id;
        this.temperatura = temperatura;
        this.ritmoCardiaco = ritmoCardiaco;
        this.presionArterial = presionArterial;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    
    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getRitmoCardiaco() {
        return ritmoCardiaco;
    }

    public void setRitmoCardiaco(double ritmoCardiaco) {
        this.ritmoCardiaco = ritmoCardiaco;
    }

    public double getPresionArteril() {
        return presionArterial;
    }

    public void setPresionArteril(double presionArterial) {
        this.presionArterial = presionArterial;
    }
    
    
    
    
    
   
}
