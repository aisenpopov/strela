package ru.strela.startup;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import java.io.File;

public class Application {

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerConnector scc = new ServerConnector(server);
        scc.setPort(Integer.parseInt(System.getProperty("jetty.port", "9090")));
        server.setConnectors(new Connector[]{scc});

        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/");
        context.setWar("src/main/webapp");
        context.setResourceBase("src/main/webapp");
        context.getMetaData().addContainerResource(new FileResource(new File("./target/classes").toURI()));
        context.setConfigurations(new Configuration[]{
                new WebXmlConfiguration(),
                new AnnotationConfiguration()
        });
//        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
//	    errorHandler.addErrorPage(404, "/error404");
//        errorHandler.addErrorPage(403, "/error403");
//	    context.setErrorHandler(errorHandler);

        server.setHandler(context);
        
        try {
            System.out.println(">>> STARTING EMBEDDED JETTY SERVER");
            System.out.println(String.format(">>> open http://localhost:%s/", scc.getPort()));
            server.start();
            while (System.in.available() == 0) {
                Thread.sleep(5000);
            }
            server.stop();
            server.join();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(100);
        }

    }
}
