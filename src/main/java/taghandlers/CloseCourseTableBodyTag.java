package taghandlers;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Handle {@code <jspclosecoursebody>} tag.
 * Creates table with elements from body.
 * If there are no elements, message about it will be printed.
 * Roman 07.05.2016.
 */
public class CloseCourseTableBodyTag extends BodyTagSupport {

    private int num;
    private int courseId;
    private Locale locale;
    private ResourceBundle localeStrings;

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public int doStartTag() throws JspException {
        localeStrings = ResourceBundle.getBundle("locale", ((locale != null) ? locale : Locale.forLanguageTag("ru")));
        try {
            pageContext.getOut().write(
                    "<form method=\"post\" action=\"/course/manage\"><table style=\"border: 3px solid #000; width: 100%\">");
            if (num > 0) {
                return EVAL_BODY_INCLUDE;
            } else {
                pageContext.getOut().write(localeStrings.getString("closeCourse.noStudents"));
                return SKIP_BODY;
            }
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
        String submit = localeStrings.getString("closeCourse.submit");
        try {
            if (num > 0) {
                pageContext.getOut().write("<tr>" +
                        "<td> <input type=\"submit\" value=\""+ submit +"\"/>" +
                        "<input type=\"hidden\" value=\"closeCourse\" name=\"action\">" +
                        "<input type=\"hidden\" value=\"" + courseId + "\" name=\"courseId\"> </td>" +
                        "</tr></table></form>");
            } else {
                pageContext.getOut().write(
                        "<tr><td><a href=\"/course?id=" + courseId + "\">" +
                                localeStrings.getString("closeCourse.backToCoursePage") + "</td>" +
                        "</tr></table></form>");
            }
            return SKIP_BODY;
        } catch (IOException e) {
            throw new JspTagException(e);
        }
    }
}
