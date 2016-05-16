package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Save in session locale has been choosen by user.
 * Roman 14.05.2016.
 */
@WebServlet("/localize")
public class LocalizeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> lang = ofNullable(req.getParameter("lang"));
        Optional<String> page = ofNullable(req.getParameter("page"));

        if (lang.isPresent()) {
            switch (lang.get()) {
                case "ru":
                    req.getSession().setAttribute("local", Locale.forLanguageTag("ru"));
                    break;
                case "en":
                    req.getSession().setAttribute("local", Locale.forLanguageTag("en"));
                    break;
            }
        }

        if (page.isPresent()) {
            resp.sendRedirect(page.get());
        } else {
            resp.sendRedirect("/");
        }
    }
}
