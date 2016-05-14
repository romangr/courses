package filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Roman 14.05.2016.
 */
@WebFilter("/*")
public class LocaleFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Optional<String> localeOptional = ofNullable(request.getParameter("locale"));
        Optional<Locale> local = ofNullable((Locale) request.getSession().getAttribute("local"));

        if (!local.isPresent()) {
            if (localeOptional.isPresent()) {
                request.getSession(true).setAttribute("local", Locale.forLanguageTag(localeOptional.get()));
            } else {
                request.getSession().setAttribute("local", Locale.forLanguageTag("ru"));
            }
        }
        System.out.println(request.getSession().getAttribute("local"));
        /*request.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));*/
        chain.doFilter(request, response);
    }
}
