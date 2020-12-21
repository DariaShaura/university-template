package com.epam.rd.izh.repository;

import com.epam.rd.izh.dto.MarkDto;
import com.epam.rd.izh.dto.ParticipantDto;
import com.epam.rd.izh.entity.*;
import com.epam.rd.izh.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Component
public class CourseRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserService userService;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    ThemeMapper themeMapper;

    @Autowired
    MaterialMapper materialMapper;

    @Autowired
    ScheduleMapper scheduleMapper;

    @Autowired
    ParticipantDtoMapper participantDtoMapper;

    @Autowired
    MarkDtoMapper markDtoMapper;

    public boolean addCourse(@Nullable Course course) {

        if (course != null) {

            String query_insertCourse = "insert into course (title, description, hours, id_teacher) " +
                    "VALUES (?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query_insertCourse, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, course.getTitle());
                ps.setString(2, course.getDescription());
                ps.setInt(3, course.getHours());
                ps.setLong(4, course.getId_teacher());
                return ps;
            }, keyHolder);

            long courseId = keyHolder.getKey().longValue();

            if(courseId > 0){

                course.setId(courseId);

                return true;
            }
        }
        return false;
    }
    public boolean addTheme(@Nullable Theme theme) {

        if (theme != null) {

            String query_insertTheme = "insert into theme (title, id_course) " +
                    "VALUES (?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query_insertTheme, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, theme.getTitle());
                ps.setLong(2, theme.getId_Course());
                return ps;
            }, keyHolder);

            long themeId = keyHolder.getKey().longValue();

            if(themeId > 0){

                theme.setId(themeId);

                return true;
            }
        }
        return false;
    }

    public boolean addThemeInSchedule(long idTheme){
        String query_insertThemeInSchedule = "insert into schedule (id_theme) " +
                "VALUES (?)";

        return jdbcTemplate.update(
                query_insertThemeInSchedule, idTheme
        ) > 0;
    }

    public boolean addMaterial(@Nullable Material material) {

        if (material != null) {

            String query_insertMaterial = "insert into material (title, type, path, id_theme, id_course) "+"" +
                    "                 VALUES (?, ?, ?, ?, ?)";
            return jdbcTemplate.update(
                    query_insertMaterial,
                    material.getTitle(), material.getType(), material.getPath(),
                    material.getIdTheme(), material.getIdCourse()
            ) > 0;
        }
        return false;
    }

    public Course getCourseById(long id){
        String query_getCourseByLogin = "SELECT * FROM course WHERE course.id = ?";

        Course course = jdbcTemplate.queryForObject(query_getCourseByLogin, new Object[]{ id }, courseMapper);

        return course;
    }

    public List<Map<String, Object>> getCourses(long id_teacher){

        String query_getTeachersCourses = "SELECT id, title FROM course WHERE course.id_teacher = ?";

        return jdbcTemplate.queryForList(query_getTeachersCourses, id_teacher);
    }

    public List<Map<String, Object>> getDescribedOnCourses(long id_Student){

        String query_getStudentCourses = "SELECT course.id, course.title FROM admission left join course " +
                "ON admission.id_course = course.id "+
                "WHERE admission.id_student = ?";

        return jdbcTemplate.queryForList(query_getStudentCourses, id_Student);
    }

    public List<Theme> getThemeList(long idCourse){
        String query_getThemesByIdCourse = "SELECT * FROM theme WHERE theme.id_course = ?";

        List<Theme> themes = jdbcTemplate.query(query_getThemesByIdCourse, new Object[]{ idCourse }, themeMapper);

        return themes;
    }

    public List<Material> getMaterialsList(long idTheme){
        String query_getMaterialsByIdTheme = "SELECT * FROM material WHERE material.id_theme = ?";

        return jdbcTemplate.query(query_getMaterialsByIdTheme, new Object[]{ idTheme }, materialMapper);
    }

    public boolean deleteCourse(long id){
        String query_deleteCourse = "delete from course where id = ?";
        return jdbcTemplate.update(
                query_deleteCourse,
                id
        ) > 0;
    }

    public boolean updateCourse(Course course){
        String query_updateCourse = "update course set title = ?, description = ?, hours = ? where id = ?";

        return jdbcTemplate.update(
                query_updateCourse, course.getTitle(), course.getDescription(), course.getHours(), course.getId()
        ) > 0;
    }

    public boolean updateTheme(Theme theme){
        String query_updateTheme = "update theme set title = ? where id = ?";

        return jdbcTemplate.update(
                query_updateTheme, theme.getTitle(), theme.getId()
        ) > 0;
    }

    public boolean updateMaterial(Material material){
        String query_updateMaterial = "update material set title = ?, type = ?, path = ? where id = ?";

        return jdbcTemplate.update(
                query_updateMaterial, material.getTitle(), material.getType(), material.getPath(), material.getId()
        ) > 0;
    }

    public boolean deleteTheme(long id){
        String query_deleteTheme = "delete from theme where id = ?";

        return jdbcTemplate.update(
                query_deleteTheme,
                id
        ) > 0;
    }

    public boolean deleteMaterial(long id){
        String query_deleteMaterial = "delete from material where id = ?";

        return jdbcTemplate.update(
                query_deleteMaterial,
                id
        ) > 0;
    }

    public List<Schedule> getCourseSchedule(long idCourse){
        String query_getCourseSchedule = "SELECT schedule.id as idShedule, theme.id as idTheme, theme.title, schedule.start_date, schedule.end_date FROM theme " +
                "left join schedule on theme.id = schedule.id_theme where theme.id_course=?";

        return jdbcTemplate.query(query_getCourseSchedule, new Object[]{idCourse}, scheduleMapper);
    }

    public boolean updateSchedule(Schedule schedule){
        String query_updateSchedule = "update schedule set start_date = ?, end_date = ? where id = ?";
        String startDate = schedule.getStartDate() != null ? schedule.getStartDate().toString(): null;
        String endDate = schedule.getEndDate() != null ? schedule.getEndDate().toString() : null;

        return jdbcTemplate.update(
                query_updateSchedule, startDate, endDate, schedule.getId()
        ) > 0;
    }

    public List<ParticipantDto> getCourseParticipants(long idCourse){
        String queryGetCourseParticipants = "SELECT lastName, firstName, secondName, birthDate FROM admission " +
                                            "LEFT JOIN user ON admission.id_student=user.id " +
                                            "WHERE admission.id_course=?";

        return jdbcTemplate.query(queryGetCourseParticipants, new Object[]{idCourse}, participantDtoMapper);
    }

    public List<MarkDto> getCourseMarks(long idCourse){
        String queryGetCourseMarks = "SELECT mark.id, mark.id_lab, material.title, mark.id_student, user.lastName, user.firstName, mark.path, mark.mark " +
                "FROM mark " +
                "LEFT JOIN user ON mark.id_student=user.id " +
                "LEFT JOIN material ON mark.id_lab=material.id "+
                "WHERE material.id_course=?";

        return jdbcTemplate.query(queryGetCourseMarks, new Object[]{idCourse}, markDtoMapper);
    }
}
