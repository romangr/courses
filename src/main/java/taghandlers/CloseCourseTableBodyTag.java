package taghandlers;

import DaoAndModel.Student;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Roman 07.05.2016.
 */
public class CloseCourseTableBodyTag extends BodyTagSupport {

    private int num;
    private int courseId;

    public void setNum(int num) {
        this.num = num;
    }
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().write(
                    "<form method=\"post\" action=\"/course/manage\"><table style=\"border: 3px solid #000; width: 100%\">");
            return EVAL_BODY_INCLUDE;
        } catch (IOException e) {
            throw new JspTagException(e);
        }
    }

    @Override
    public int doAfterBody() throws JspException {
        if (num-- <= 1) return SKIP_BODY;
            return EVAL_BODY_AGAIN;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().write("<tr>" +
                    "<td> <input type=\"submit\" value=\"Сохранить\"/>" +
                    "<input type=\"hidden\" value=\"closeCourse\" name=\"action\">" +
                    "<input type=\"hidden\" value=\"" + courseId + "\" name=\"courseId\"> </td>" +
                    "</tr></table></form>");
            return SKIP_BODY;
        } catch (IOException e) {
            throw new JspTagException(e);
        }
    }
}
