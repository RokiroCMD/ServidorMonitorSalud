package com.alejandro.servidormonitoreosalud.view;

import com.alejandro.servidormonitoreosalud.controller.DBController;
import com.alejandro.servidormonitoreosalud.model.entities.SensorInfo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.math3.stat.regression.SimpleRegression;

@WebServlet(name = "InferenciaServlet", urlPatterns = {"/InferenciaServlet"})
public class InferenciaServlet extends HttpServlet {

    public static Map<String, Entry<SimpleRegression, Integer>> temperaturaRegresiones = new HashMap<>();
    public static Map<String, Entry<SimpleRegression, Integer>> ritmoRegresiones = new HashMap<>();
    public static Map<String, Entry<SimpleRegression, Integer>> presionRegresiones = new HashMap<>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        
        HttpSession httpSession = request.getSession(true);
        String id = (String) httpSession.getAttribute("username");
        String tipo = request.getParameter("tipo");
        String sensor = request.getParameter("sensor");
        double prediction = 0;
        if ("recargar".equals(tipo)) {
            if (null != sensor) // Realizar acción para recargar los datos
            switch (sensor) {
                case "temp":
                    // Acción relacionada con temperatura
                    System.out.println("Recargando temperatura...");
                    prediction = RefreshTemperaturePrediction(id);
                    break;
                case "ritmo":
                    // Acción relacionada con ritmo cardíaco
                    System.out.println("Recargando ritmo cardíaco...");
                    prediction = RefreshRythmPrediction(id);
                    break;
                case "presion":
                    // Acción relacionada con presión arterial
                    System.out.println("Recargando presión arterial...");
                    prediction = RefreshPressurePrediction(id);
                    break;
                default:
                    break;
            } 
        }

        // Si deseas enviar una respuesta de vuelta al cliente (JavaScript)
        response.setContentType("application/json");
        response.getWriter().write("{\"prediction\": \""+prediction+"\",\"sensor\": \""+sensor+"\"}");
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public double RefreshTemperaturePrediction(String id){
        Entry<SimpleRegression, Integer> regresion =  GetTemperatureRegressionById(id);
        if (regresion == null) {
            regresion = CreateRegressionTemperature(id);
        }
        
        AssignTemperatureRegressionBaseValues(id, regresion.getKey());
        
        return PredictData(regresion, 10);
    }
   
    public double RefreshRythmPrediction(String id){
        Entry<SimpleRegression, Integer> regresion =  GetRythmRegressionById(id);
        if (regresion == null) {
            regresion = CreateRegressionRythm(id);
        }
        
        AssignRythmRegressionBaseValues(id, regresion.getKey());
        
        return PredictData(regresion, 10);
    }
   
    public double RefreshPressurePrediction(String id){
        Entry<SimpleRegression, Integer> regresion =  GetPressureRegressionById(id);
        if (regresion == null) {
            regresion = CreateRegressionPressure(id);
        }
        
        AssignPressureRegressionBaseValues(id, regresion.getKey());
        
        return PredictData(regresion, 10);
    }
    
    public Entry<SimpleRegression, Integer> CreateRegressionTemperature(String id) {

        SimpleRegression regression = new SimpleRegression();
        Entry<SimpleRegression, Integer> response = Map.entry(regression, 0);
        temperaturaRegresiones.put(id, response);

        return response;
    }
    public Entry<SimpleRegression, Integer> CreateRegressionRythm(String id) {

        SimpleRegression regression = new SimpleRegression();
        Entry<SimpleRegression, Integer> response = Map.entry(regression, 0);
        ritmoRegresiones.put(id, response);

        return response;
    }
    public Entry<SimpleRegression, Integer> CreateRegressionPressure(String id) {

        SimpleRegression regression = new SimpleRegression();
        Entry<SimpleRegression, Integer> response = Map.entry(regression, 0);
        presionRegresiones.put(id, response);

        return response;
    }

    public void SetRegresionData(Entry< SimpleRegression, Integer> regression, double[][] newData) {
        regression.getKey().clear();
        for (double data[] : newData) {
            regression.getKey().addData(data[0], data[1]);
        }
        
        regression = Map.entry( regression.getKey(), newData.length);
    }
    public void AddRegresionData(Entry<SimpleRegression, Integer> regression, double[][] newData) {
        for (double data[] : newData) {
            regression.getKey().addData(data[0], data[1]);
        }
        regression.setValue(regression.getValue() + 1);
    }
    public double PredictData(Entry<SimpleRegression, Integer> regression, int value) {
        return regression.getKey().predict(regression.getValue() + value);
    }

    public Entry<SimpleRegression, Integer> GetTemperatureRegressionById(String id) {
        for (Map.Entry<String, Map.Entry<SimpleRegression, Integer>> entry : temperaturaRegresiones.entrySet()) {
            String key = entry.getKey();
            if (key.equals(id)) {
                return entry.getValue();
            }
        }
        return null;
    }
    public Entry<SimpleRegression, Integer> GetRythmRegressionById(String id) {
        for (Map.Entry<String, Map.Entry<SimpleRegression, Integer>> entry : ritmoRegresiones.entrySet()) {
            String key = entry.getKey();
            if (key.equals(id)) {
                return entry.getValue();
            }
        }
        return null;
    }
    public Entry<SimpleRegression, Integer> GetPressureRegressionById(String id) {
        for (Map.Entry<String, Map.Entry<SimpleRegression, Integer>> entry : presionRegresiones.entrySet()) {
            String key = entry.getKey();
            if (key.equals(id)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void AssignTemperatureRegressionBaseValues(String id, SimpleRegression regression) {
        List<SensorInfo> lastHourTemp = DBController.GetsAllHourlyTempreatureData(id);
        double[][] data = new double[lastHourTemp.size()][2];

        for (int i = 0; i < lastHourTemp.size(); i++) {
            SensorInfo info = lastHourTemp.get(i);
            System.out.println("i: "+i);
            data[i][0] = (i + 1);
            data[i][1] = info.getTemperatura();
        }

        SetRegresionData(Map.entry(regression, data.length), data);
    }
    public void AssignRythmRegressionBaseValues(String id, SimpleRegression regression) {
        List<SensorInfo> lastHourRythm = DBController.GetsAllHourlyRithmData(id);
        double[][] data = new double[lastHourRythm.size()][2];

        for (int i = 0; i < lastHourRythm.size(); i++) {
            SensorInfo info = lastHourRythm.get(i);
            System.out.println("i: "+i);
            data[i][0] = (i + 1);
            data[i][1] = info.getRitmoCardiaco();
        }

        SetRegresionData(Map.entry(regression, data.length), data);
    }
    public void AssignPressureRegressionBaseValues(String id, SimpleRegression regression) {
        List<SensorInfo> lastHourPressure = DBController.GetsAllHourlyPreassureData(id);
        double[][] data = new double[lastHourPressure.size()][2];

        for (int i = 0; i < lastHourPressure.size(); i++) {
            SensorInfo info = lastHourPressure.get(i);
            System.out.println("i: "+i);
            data[i][0] = (i + 1);
            data[i][1] = info.getPresionArterial();
        }

        SetRegresionData(Map.entry(regression, data.length), data);
    }
}
