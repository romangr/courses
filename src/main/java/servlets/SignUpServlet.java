package servlets;

import DaoAndModel.CourseDao;
import DaoAndModel.UserDao;
import exceptions.SameEmailRegistrationException;
import listeners.DaoProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Roman 08.05.2016.
 */
@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        Optional<String> firstNameOptional = ofNullable(req.getParameter("firstName"));
        Optional<String> lastNameOptional = ofNullable(req.getParameter("lastName"));
        Optional<String> emailOptional = ofNullable(req.getParameter("email"));
        Optional<String> passwordOptional = ofNullable(req.getParameter("password"));
        Optional<String> passwordConfirmOptional = ofNullable(req.getParameter("passwordConfirm"));

        if (firstNameOptional.isPresent() && lastNameOptional.isPresent()
                && emailOptional.isPresent() && passwordOptional.isPresent() && passwordConfirmOptional.isPresent()) {
            System.out.println("All params are got");
            if (firstNameOptional.get().length() > 30 || lastNameOptional.get().length() > 30) {
                System.out.println("name length");
                giveAnotherTryWithMessage(req, resp, "Max names length is 30");
                return;
            }
            if (emailOptional.get().length() < 5) {
                System.out.println("email length");
                giveAnotherTryWithMessage(req, resp, "Email error");
                return;
            }
            if (!passwordOptional.get().equals(passwordConfirmOptional.get())) {
                System.out.println("passwords check");
                giveAnotherTryWithMessage(req, resp, "Passwords are not equal");
                return;
            }
            try {
                System.out.println("Creating user");
                Optional<Boolean> isTeacherOptional = ofNullable(req.getParameter("isTeacher")).map(s -> s.equals("on"));
                if (isTeacherOptional.isPresent() && isTeacherOptional.get()) {
                    userDao.createTeacher(firstNameOptional.get(), lastNameOptional.get(), emailOptional.get(),
                            passwordOptional.get());
                } else {
                    userDao.createStudent(firstNameOptional.get(), lastNameOptional.get(), emailOptional.get(),
                            passwordOptional.get());
                }
                resp.sendRedirect("/my");
            } catch (SameEmailRegistrationException e) {
                giveAnotherTryWithMessage(req, resp, "This email is already in use");
            }
        }
    }

    private void giveAnotherTryWithMessage(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("message", message);
        getServletContext().getRequestDispatcher("/signUp.jsp").forward(req, resp);
    }
}
