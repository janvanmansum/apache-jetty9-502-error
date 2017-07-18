package nl.knaw.dans.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class BackendServer {
  public static void main(String[] args) throws Exception {
    Server server = new Server(20000);
    ServletContextHandler handler = new ServletContextHandler();
    handler.addServlet(AcceptAllServlet.class, "/acceptAll");
    handler.addServlet(ReadAllServlet.class, "/readAll");
    server.setHandler(handler);
    server.start();
    server.join();
  }
}
