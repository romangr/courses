package taghandlers;

import dao_and_model.Student;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Roman 07.05.2016.
 */
public class CloseCourseTableTag extends TagSupport {
    private JSPSetBean<Student> set;

    public void setSet(JSPSetBean<Student> set) {
        this.set = set;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();

            out.write("<form method=\"post\" action=\"/kek\" >");
            out.write("<table style=\"border: 1px solid #000;\">");
            for (Student student : set){
                out.write("<tr>" +
                            "<td>" + student.getFirstName() + " " + student.getLastName() + "</td>" +
                            "<td>" + "<select name = \"mark\">" +
                                        "<option value=\"5\">" + 5 + "</option>" +
                                        "<option value=\"4\">" + 4 + "</option>" +
                                        "<option value=\"3\">" + 3 + "</option>" +
                                        "<option value=\"2\">" + 2 + "</option>" +
                                        "<option value=\"1\">" + 1 + "</option>" +
                                    "</select>" +
                            "</td>" +
                            "<td> <textarea name = \"note\"></textarea> </td>" +
                        "</tr>");
            }
            out.write("<tr><td><input type=\"submit\" value=\"Сохранить\"/></td></tr>");
            out.write("</table>");
            out.write("</form>");

        }catch(IOException e){
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}
