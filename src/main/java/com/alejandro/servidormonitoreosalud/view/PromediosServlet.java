/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.alejandro.servidormonitoreosalud.view;

import com.alejandro.servidormonitoreosalud.controller.DBController;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
@WebServlet(name = "PromediosServlet", urlPatterns = {"/PromediosServlet"})
public class PromediosServlet extends HttpServlet {

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonObject = new JsonObject();
        HttpSession httpSession = request.getSession(true);
        String id = (String) httpSession.getAttribute("username");
        
        
        float promedioTemperatura = 0;
        List<SensorInfo> tempData = DBController.GetsAllDailyTempreatureData(id);
        for (SensorInfo tempInfo : tempData ){
            promedioTemperatura += tempInfo.getTemperatura();
        }
        promedioTemperatura /= tempData.size();
        
        
        float promedioRitmo = 0;
        List<SensorInfo> ritmoData = DBController.GetsAllDailyRithmData(id);
        for (SensorInfo ritmoInfo : ritmoData ){
            promedioRitmo += ritmoInfo.getRitmoCardiaco();
        }
        promedioRitmo /= ritmoData.size();
        
        
        float promedioPresion = 0;
        List<SensorInfo> presionData = DBController.GetsAllDailyPreassureData(id);
        for (SensorInfo presionInfo : presionData ){
            promedioPresion += presionInfo.getPresionArterial();
        }
        promedioPresion /= presionData.size();

        jsonObject.addProperty("temperatura", promedioTemperatura);
        jsonObject.addProperty("ritmo", promedioRitmo);
        jsonObject.addProperty("presion", promedioPresion);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonObject.toString());
            out.flush();
        }

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
