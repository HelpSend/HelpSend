package com.heu.cs.jettyserver;


import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


/**
 * Created by memgq on 2017/5/14.
 */
public class JettyServerStart {
    public static void main(String[] args) throws Exception {

//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//
//        Server jettyServer;
//        jettyServer = new Server(8080);
//        jettyServer.setHandler(context);
//        ServletHolder jerseyServlet = context.addServlet(
//                org.glassfish.jersey.servlet.ServletContainer.class, "/webapi/*");
//        jerseyServlet.setInitOrder(1);
//
//        // Tells the Jersey Servlet which REST api/class to load.
//        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.heu.cs.api");
//        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
//                "UploadFileService;org.glassfish.jersey.media.multipart.MultiPartFeature");
//
//
//        ServletHolder staticServlet = context.addServlet(DefaultServlet.class, "/*");
//
//        staticServlet.setInitParameter("resourceBase", "src/main/resources");
//
//        staticServlet.setInitParameter("pathInfoOnly", "true");
//
//
//        try {
//            jettyServer.start();
//            jettyServer.join();
//        } finally {
//            jettyServer.destroy();
//        }


        BasicConfigurator.configure();
        Server jettyServer = new Server();
        HttpConfiguration http_config = new HttpConfiguration();
//        http_config.setSecureScheme("https");
//        http_config.setSecurePort(8443);
//        http_config.setOutputBufferSize(32768);
//        http_config.setRequestHeaderSize(8192);
//        http_config.setResponseHeaderSize(8192);
        http_config.setSendServerVersion(true);
        http_config.setSendDateHeader(false);
        ServerConnector http = new ServerConnector(jettyServer,
                new HttpConnectionFactory(http_config));
        http.setPort(7012);
        http.setIdleTimeout(120000);
        jettyServer.addConnector(http);



        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/webapi/*");
        jerseyServlet.setInitOrder(1);
        // Tells the Jersey Servlet which REST api/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.heu.cs.api");
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
                "UploadFileService;org.glassfish.jersey.media.multipart.MultiPartFeature");

        ServletHolder staticServlet = context.addServlet(DefaultServlet.class, "/*");
        staticServlet.setInitParameter("resourceBase", "src/main/resources");
        staticServlet.setInitParameter("pathInfoOnly", "true");

 //       WebAppContext appContext = new WebAppContext();
//        // 设置描述符位置
//        appContext.setDescriptor("./web/WEB-INF/web.xml");
//        // 设置Web内容上下文路径
//        appContext.setResourceBase("./web");
////        // 设置上下文路径
////        appContext.setContextPath("/");
//        appContext.setParentLoaderPriority(true);


        HandlerCollection handlers = new HandlerCollection();
       // handlers.setHandlers(new Handler[]{context,appContext});
        handlers.setHandlers(new Handler[]{context});
        jettyServer.setHandler(handlers);
        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }



    }

}