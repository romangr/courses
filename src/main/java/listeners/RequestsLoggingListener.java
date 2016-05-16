package listeners;

import org.apache.log4j.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Logs all the requests which have been initialized
 * Roman 15.05.2016.
 */

@WebListener
public class RequestsLoggingListener implements ServletRequestListener {

    private static final Logger LOGGER = Logger.getLogger(RequestsLoggingListener.class.getName());

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        LOGGER.debug(request.getMethod() + " " + request.getRequestURI() +
                (request.getQueryString() != null
                        ? "?" + request.getQueryString()
                        : "")
        );
    }
}
