package DaoAndModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 25.04.2016 Roman.
 */

@Data
@AllArgsConstructor
public class Course implements Model {

    private final int id;
    private final Teacher teacher;
    private String name;
    private String description;
    private int status;
    static final int OPEN = 0;
    static final int IN_PROCESS = 1;
    static final int FINISHED = 2;
}
