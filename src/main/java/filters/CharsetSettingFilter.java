package filters;

import listeners.DaoProvider;
import org.apache.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Roman 05.05.2016.
 */
@WebFilter("/*")
public class CharsetSettingFilter extends HttpFilter {

    private static final Logger LOGGER = Logger.getLogger(CharsetSettingFilter.class.getName());

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.trace("Charset is set");
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
}
