package dao_and_model;

import lombok.*;

/**
 * Roman 17.05.2016.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class TeachersConclusion {
    private final Course course;
    private final Student student;
    private final int mark;
    private final String note;
}
