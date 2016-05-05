package taghandlers;

import DaoAndModel.Course;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Iterator;

/**
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
           /* pageContext.getOut().write("Size = <b>(" + set.getSize() + ")</b>"
                    + "<table style=\"border: 1px solid #000;\"><tr><td>"
                    + set.elements().collect(Collectors.joining("</td></tr><tr><td>"))
                    + "</td></tr></table>");*/
            out.write("<table style=\"border: 1px solid #000;\">");
            Iterator<Course> iterator = set.getIterator();
            while (iterator.hasNext()){
                Course course = iterator.next();
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