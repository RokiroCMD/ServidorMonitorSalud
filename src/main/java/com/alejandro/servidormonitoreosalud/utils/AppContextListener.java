
package com.alejandro.servidormonitoreosalud.utils;

import com.alejandro.servidormonitoreosalud.model.ConnectionMQTT;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class AppContextListener  implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionMQTT.InnitConnection();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionMQTT.instance.disconnet();
    }
    
}
