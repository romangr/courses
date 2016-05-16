package taghandlers;

import dao_and_model.Course;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Creates simple table with elements from {@code set}
 * Roman 27.04.2016.
 */
public class CoursesTableTag extends TagSupport {

    private JSPSetBean<Course> set;

    public void setSet(JSPSetBean<Course> set) {
        this.set = set;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            out.write("<table style=\"border: 1px solid #000;\">");
            for (Course course : set){
                out.write("<tr><td><a href=\"/course?id=" + course.getId() + "\">" + course.getName() + "</a></td>" +
                                "<td>" + course.getDescription() + "</td></tr>");
            }
            out.write("</table>");

        }catch(IOException e){
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}
