package com.heu.cs.jettyserver;


import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by memgq on 2017/5/14.
 */
public class JettyServerStart {
    public static void main(String[] args)throws Exception{

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer;
        jettyServer = new Server(8080);
        jettyServer.setHandler(context);


        ServletHolder jerseyServlet = context.addServlet(
        org.glassfish.jersey.servlet.ServletContainer.class, "/webapi/*");
        jerseyServlet.setInitOrder(1);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages","com.heu.cs.service");
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
                "UploadFileService;org.glassfish.jersey.media.multipart.MultiPartFeature");


        ServletHolder staticServlet = context.addServlet(DefaultServlet.class,"/*");

        staticServlet.setInitParameter("resourceBase","src/main/resources");

        staticServlet.setInitParameter("pathInfoOnly","true");
        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
            }
        }
}