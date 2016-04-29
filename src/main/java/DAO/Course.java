package DAO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Roman on 25.04.2016.
 */

@Data
@AllArgsConstructor
public class Course {

    private final int id;
    private final Teacher teacher;
    private String name;
    private String description;
    private int status;
    static final int OPEN = 0;
    static final int IN_PROCESS = 1;
    static final int FINISHED = 2;

    /*public Course(int id, Teacher teacher) {
        this.id = id;
        this.teacher = teacher;
    }

    public Course(int id, Teacher teacher, String name, String description, int status) {
        this.teacher = teacher;
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }*/
}
