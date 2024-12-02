package com.alejandro.servidormonitoreosalud.view;

import com.alejandro.servidormonitoreosalud.controller.DBController;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smile.clustering.KMeans;

@WebServlet(name = "ClusteringServlet", urlPatterns = {"/ClusteringServlet"})
public class ClusteringServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession httpSession = request.getSession(true);
        String id = (String) httpSession.getAttribute("username");

        double[][] datos = GetClusterData(id);

        if (datos== null) {
            return;
        }
        
        int k = 3;
        KMeans kmeans = KMeans.fit(datos, k);

        // Preparar datos para enviar al cliente
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("centroids", kmeans.centroids);
        resultado.put("clusters", kmeans.y);
        resultado.put("data", datos);

        // Enviar JSON al cliente
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(resultado);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public double[][] GetClusterData(String id) {
        try {
            List<SensorInfo> temp = DBController.GetsAllDailyTempreatureData(id);
            List<SensorInfo> ritmo = DBController.GetsAllDailyRithmData(id);
            List<SensorInfo> presion = DBController.GetsAllDailyPreassureData(id);
            double[][] result = new double[temp.size()][3];
            for (int i = 0; i < temp.size(); i++) {
                result[i][0] = temp.get(i).getTemperatura();
                result[i][1] = ritmo.get(i).getRitmoCardiaco();
                result[i][2] = presion.get(i).getPresionArterial();
            }
            return  result;
        } catch (Exception e) {

        }

        return null;
    }

}
