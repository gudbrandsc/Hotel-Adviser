package server;


import database.HotelReviewBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class HotelAppServer {
    protected static Logger log = LogManager.getLogger();
    private static int PORT = 8080;

    public static void main(String[] args) {

        Server server = new Server(PORT);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(LoginUserServlet.class,     "/login");
        handler.addServletWithMapping(LoginRegisterServlet.class, "/register");
        handler.addServletWithMapping(HotelsDisplayServlet.class,  "/viewhotels");
        handler.addServletWithMapping(HotelreviewsServlet.class,  "/reviews");

        handler.addServletWithMapping(LoginRedirectServlet.class, "/*");


        log.info("Starting server on port " + PORT + "...");

        try {
            server.start();
            server.join();

            log.info("Exiting...");
        }
        catch (Exception ex) {
            log.fatal("Interrupted while running server.", ex);
            System.exit(-1);
        }
    }
}