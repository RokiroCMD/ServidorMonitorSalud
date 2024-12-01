
package com.alejandro.servidormonitoreosalud.model.entities;

import java.time.Instant;


public class SensorInfo {
    
    private String id;
    private double temperatura;
    private double ritmoCardiaco;
    private double presionArterial;
    private Instant time;

    public SensorInfo(String id, double temperatura, double ritmoCardiaco, double presionArterial) {
        this.id = id;
        this.temperatura = temperatura;
        this.ritmoCardiaco = ritmoCardiaco;
        this.presionArterial = presionArterial;
    }

    public SensorInfo(String id, double temperatura, double ritmoCardiaco, double presionArterial, Instant time) {
        this.id = id;
        this.temperatura = temperatura;
        this.ritmoCardiaco = ritmoCardiaco;
        this.presionArterial = presionArterial;
        this.time = time;
    }
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Instant getTime() {
        return time;
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

    public double getPresionArterial() {
        return presionArterial;
    }

    public void setPresionArteril(double presionArterial) {
        this.presionArterial = presionArterial;
    }
    
    
    
    
    
   
}
