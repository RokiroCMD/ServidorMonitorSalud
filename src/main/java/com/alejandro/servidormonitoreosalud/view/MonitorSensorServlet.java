package com.alejandro.servidormonitoreosalud.view;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "MonitorSensorServlet", urlPatterns = {"/MonitorSensorServlet"})
public class MonitorSensorServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MonitorSensorServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MonitorSensorServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }*/
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
        //processRequest(request, response);
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        HttpSession httpSession = request.getSession(true);
        String username = request.getParameter("nombre");
        httpSession.setAttribute("username", username);

        if (username != null) {
            /*String document[] = new String[]{
                "<html>",
                "<head>",
                "<title>Monitor sensor</title>",
                "</head>",
                "<body>",
                "<h1>Usuario: " + username + "</h1>",
                "<Script>",
                "var websocket = new WebSocket(\"ws://localhost:8080/ServidorMonitoreoSalud/wsmonitor\");",
                "websocket.onmessage = function processMessage (message) {",
                "var h1 = document.createElement(\"h1\");",
                "h1.textContent = message.data;",
                "document.body.appendChild(h1);",
                "};",
                "</Script>",
                "</body>",
                "</html>"
            };*/

            String document[] = new String[]{
                "<html>\n"
                + "    <head>\n"
                + "        <title>Monitor WebSocket</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "            <h1 id=\"temperatura\">Temperatura: </h1>\n"
                + "            <h1 id=\"ritmo\">Ritmo Cardiaco: </h1>\n"
                + "            <h1 id=\"presion\">Presion arterial: </h1>\n"
                + "            <script> \n"
                + "                var websocket = new WebSocket(\"ws://localhost:8080/ServidorMonitoreoSalud/wsmonitor\");\n"
                + "                websocket.onmessage = function processMessage (message) {\n"
                + "                    var data = JSON.parse(message.data);\n"
                + "                    document.getElementById(\"temperatura\").textContent = \"Temperatura: \" + data.temperatura;\n"
                + "                    document.getElementById(\"ritmo\").textContent = \"Ritmo cardiaco: \" + data.ritmo;\n"
                + "                    document.getElementById(\"presion\").textContent = \"Presion arterial: \" + data.presion;\n"
                + "                };\n"
                + "            </script>\n"
                + "    </body>\n"
                + "</html>"
            };

            for (int i = 0; i < document.length; i++) {
                printWriter.println(document[i]);
            }

        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
