package com.alejandro.servidormonitoreosalud.model;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.*;

public class WSMonitorConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        try {
            if (request != null) {
                if (request.getHttpSession() != null) {
                    if ((HttpSession) request.getHttpSession() != null) {
                        if (((HttpSession) request.getHttpSession()).getAttribute("username") != null) {
                            System.out.println("No es nulo");
                            sec.getUserProperties().put("username", (String) ((HttpSession) request.getHttpSession()).getAttribute("username"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Tronó tú");
        }
    }
}
