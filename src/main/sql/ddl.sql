-- noinspection SqlNoDataSourceInspectionForFile
CREATE TABLE course
(
  id          INTEGER PRIMARY KEY NOT NULL,
  name        VARCHAR(30)         NOT NULL,
  description VARCHAR(255),
  teacher_id  INTEGER             NOT NULL,
  status      INTEGER DEFAULT 0   NOT NULL
);
CREATE TABLE student_course
(
  student_id INTEGER NOT NULL,
  course_id  INTEGER NOT NULL,
  mark       INTEGER,
  note       TEXT,
  reg_date   DATE DEFAULT ('now' :: TEXT) :: DATE,
  CONSTRAINT student_course_student_id_course_id_pk PRIMARY KEY (student_id, course_id)
);
CREATE TABLE user_roles
(
  email     VARCHAR(30) NOT NULL,
  role_name VARCHAR(15) NOT NULL,
  CONSTRAINT user_roles_pkey PRIMARY KEY (email, role_name)
);
CREATE TABLE users
(
  id         INTEGER PRIMARY KEY NOT NULL,
  email      VARCHAR(30)         NOT NULL,
  password   VARCHAR(32)         NOT NULL,
  first_name VARCHAR(30)         NOT NULL,
  last_name  VARCHAR(30),
  reg_date   DATE DEFAULT ('now' :: TEXT) :: DATE,
  type       INTEGER DEFAULT 0   NOT NULL
);
ALTER TABLE course
  ADD FOREIGN KEY (teacher_id) REFERENCES users (id);
ALTER TABLE student_course
  ADD FOREIGN KEY (student_id) REFERENCES users (id);
ALTER TABLE student_course
  ADD FOREIGN KEY (course_id) REFERENCES course (id);
CREATE UNIQUE INDEX "User_email_uindex" ON users (email);

/*CREATE OR REPLACE FUNCTION public.md5changepassword()
  RETURNS trigger
AS
$BODY$
  BEGIN
        IF char_length(new.password) <> 32 THEN
            new.password = md5(new.password);
        END IF;
        RETURN NEW;
    END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION public.md5password()
  RETURNS trigger
AS
$BODY$
  BEGIN
        NEW.password = md5(NEW.password);
        RETURN NEW;
    END;
$BODY$
LANGUAGE plpgsql VOLATILE;
*/