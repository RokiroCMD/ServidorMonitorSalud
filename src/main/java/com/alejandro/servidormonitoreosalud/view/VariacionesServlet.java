/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.alejandro.servidormonitoreosalud.view;

import com.alejandro.servidormonitoreosalud.controller.DBController;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author alexc
 */
@WebServlet(name = "VariacionesServlet", urlPatterns = {"/VariacionesServlet"})
public class VariacionesServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession httpSession = request.getSession(true);
        String id = (String) httpSession.getAttribute("username");
        String jsonResponse = "";
        Gson gson = new Gson();
        String type = request.getParameter("type");
        switch (type) {
            case "temperatura":
                List<Map<String, Object>> datosPruebaTemperatura = new ArrayList<>();
                for (SensorInfo info : DBController.GetsAllDailyTempreatureData(id)) {
                    Map<String, Object> registro = new LinkedHashMap<>();
                    registro.put("tiempo", FormatTime(info.getTime()));
                    registro.put("valor", info.getTemperatura());
                    datosPruebaTemperatura.add(registro);
                }
                jsonResponse = gson.toJson(datosPruebaTemperatura);
                break;
            case "ritmo":
                List<Map<String, Object>> datosPruebaRitmo = new ArrayList<>();
                for (SensorInfo info : DBController.GetsAllDailyRithmData(id)) {
                    Map<String, Object> registro = new LinkedHashMap<>();
                    registro.put("tiempo", FormatTime(info.getTime()));
                    registro.put("valor", info.getRitmoCardiaco());
                    datosPruebaRitmo.add(registro);
                }
                jsonResponse = gson.toJson(datosPruebaRitmo);
                break;
            case "presion":
                List<Map<String, Object>> datosPruebaPresion = new ArrayList<>();
                for (SensorInfo info : DBController.GetsAllDailyPreassureData(id)) {
                    Map<String, Object> registro = new LinkedHashMap<>();
                    registro.put("tiempo", FormatTime(info.getTime()));
                    registro.put("valor", info.getPresionArterial());
                    datosPruebaPresion.add(registro);
                }
                jsonResponse = gson.toJson(datosPruebaPresion);
                break;
        }
        try ( PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

    private String FormatTime(Instant time) {
        LocalTime localTime = time.atZone(ZoneId.systemDefault()).toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return localTime.format(formatter);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
