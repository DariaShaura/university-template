package com.epam.rd.izh.repository;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.*;
import com.epam.rd.izh.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    ThemeAttendenceDtoMapper themeAttendenceDtoMapper;

    @Autowired
    StudentCourseDtoMapper studentCourseDtoMapper;

    @Autowired
    StudentPossibleCourseDtoMapper studentPossibleCourseDtoMapper;

    @Autowired
    StudentCourseLabDtoMapper studentCourseLabDtoMapper;

    @Autowired
    StudentThemeScheduleWithAttendenceMapper studentThemeScheduleWithAttendenceMapper;

    @Transactional
    public void addCourseThemesMaterials(@Nullable Course course, List<Theme> themeList, List<List<Material>> materialList){
        addCourse(course);

        themeList.stream().forEach(t -> {t.setId_Course(course.getId()); addTheme(t);});

        for(int i=0; i< themeList.size(); i++){
            int finalI = i;
            materialList.get(i).stream().forEach(m -> {m.setIdCourse(course.getId());
                                                       m.setIdTheme(themeList.get(finalI).getId());
                                                       addMaterial(m);});
        }
    }

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

            String query_insertMaterial = "insert into material (title, type, path, id_theme, id_course) "+
                    "VALUES (?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query_insertMaterial, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, material.getTitle());
                ps.setString(2, material.getType());
                ps.setString(3, material.getPath());
                ps.setLong(4, material.getIdTheme());
                ps.setLong(5, material.getIdCourse());
                return ps;
            }, keyHolder);

            long materialId = keyHolder.getKey().longValue();

            if(materialId > 0){

                material.setId(materialId);

                return true;
            }
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
        String queryGetCourseParticipants = "SELECT id_student, lastName, firstName, secondName, birthDate FROM admission " +
                                            "LEFT JOIN user ON admission.id_student=user.id " +
                                            "WHERE admission.id_course=?";

        String queryGetParticipantAttendence = "SELECT id_theme, attended FROM attendence LEFT JOIN theme ON attendence.id_theme=theme.id "+
                                                "WHERE (id_student = ?) and (theme.id_course = ?)";

        List<ParticipantDto> courseParticipantList = jdbcTemplate.query(queryGetCourseParticipants, new Object[]{idCourse}, participantDtoMapper);

        for(ParticipantDto courseParticipant: courseParticipantList){
            List<ThemeAttendenceDto> themeAttendenceDtoList =
                    jdbcTemplate.query(queryGetParticipantAttendence, new Object[]{courseParticipant.getIdStudent(), idCourse}, themeAttendenceDtoMapper);

            courseParticipant.setAttendenceList(themeAttendenceDtoList);
        }

        return courseParticipantList;
    }

    public List<MarkDto> getCourseMarks(long idCourse){
        String queryGetCourseMarks = "SELECT mark.id, mark.id_lab, material.title, mark.id_student, user.lastName, user.firstName, mark.path, mark.mark " +
                "FROM mark " +
                "LEFT JOIN user ON mark.id_student=user.id " +
                "LEFT JOIN material ON mark.id_lab=material.id "+
                "WHERE material.id_course=?";

        List<MarkDto> markDtoList = jdbcTemplate.query(queryGetCourseMarks, new Object[]{idCourse}, markDtoMapper);

        return markDtoList;
    }

    public boolean updateMark(Mark mark)
    {
        String queryUpdateMark = "update mark set mark = ? where id = ?";

        return jdbcTemplate.update(
                queryUpdateMark, mark.getMark(), mark.getId()
        ) > 0;
    }

    public boolean addLab(Mark mark)
    {
        String queryAddLab = "insert into mark (id_student, id_lab, path) VALUES (?, ?, ?)";

        return jdbcTemplate.update(
                queryAddLab, mark.getIdStudent(), mark.getIdLab(), mark.getPath()
        ) > 0;
    }

    public boolean updateLab(Mark mark)
    {
        String queryUpdateLabPath = "update mark set path = ? where id = ?";

        return jdbcTemplate.update(
                queryUpdateLabPath, mark.getPath(), mark.getId()
        ) > 0;
    }

    public boolean updateAttendence(Attendence attendence){
        String queryUpdateAttendence = "update attendence set attended = ? where (id_student = ?)AND(id_theme=?)";

        return jdbcTemplate.update(
                queryUpdateAttendence, attendence.isAttended(), attendence.getIdStudent(), attendence.getIdTheme()
        ) > 0;
    }

    public List<StudentCourseDto> getStudentCourses(long idStudent){
        String queryGetStudentCourses = "SELECT id_course, course.title FROM admission "+
                                        "LEFT JOIN course ON course.id = admission.id_course WHERE id_student=?";

        return jdbcTemplate.query(queryGetStudentCourses, new Object[]{idStudent}, studentCourseDtoMapper);
    }

    public List<StudentPossibleCourseDto> getStudentPossibleCourses(long idStudent){
        String queryGetStudentPossibleCourses = "SELECT course.id, course.title, concat(lastName, ' ',substring(firstName,1,1), '.',substring(secondName,1,1),'.') as teacher_name," +
                            "course.hours FROM university_.course left join user ON user.id=course.id_teacher " +
                            "where course.id not in (select id_course from admission where id_student=?)";

        return jdbcTemplate.query(queryGetStudentPossibleCourses, new Object[]{idStudent}, studentPossibleCourseDtoMapper);
    }

    public List<StudentCourseLabDto> getStudentCourseLabList(long idStudent, long idCourse){
        String getStudentCourseLabsList = "SELECT material.id as idLab, mark.id as idMark, material.title as labTitle, mark.path, mark.mark FROM material " +
                "LEFT JOIN mark ON material.id=mark.id_lab " +
                "WHERE (material.id_course=?)AND(material.type='Лабораторная')and((mark.id is null) or (id_student=?))";

        return jdbcTemplate.query(getStudentCourseLabsList, new Object[]{idCourse, idStudent}, studentCourseLabDtoMapper);
    }

    public boolean addStudentCourseAdmission(Admission admission){
        String queryAddAdmission = "INSERT INTO admission (id_student,id_course) VALUES (?,?)";

        String queryAddAttendence = "insert into attendence (id_student, id_theme) " +
                "select ?, id from theme where id_course=?";

        return (jdbcTemplate.update(
                queryAddAdmission, admission.getIdStudent(), admission.getIdCourse()
        ) > 0) && (jdbcTemplate.update(
                queryAddAttendence, admission.getIdStudent(), admission.getIdCourse()
        ) > 0);
    }

    @Transactional
    public boolean deleteStudentCourseAdmission(Admission admission){

        String queryDeleteAdmission = "delete from admission where (id_student=?)AND(id_course=?)";

        //удалить студента из таблицы Attendence
        String queryDeleteAttendence = "delete from attendence " +
                                        "where (id_student=?)and" +
                                        "(id_theme in (select id from theme where id_course=?)); ";
        //удалить студента из таблицы Mark
        String queryDeleteMark = "delete FROM mark "+
                                    "where (id_student=?)and"+
                                    "(id_lab in (select id from material where id_course=?))";

        jdbcTemplate.update(
                queryDeleteAdmission, admission.getIdStudent(), admission.getIdCourse());
        jdbcTemplate.update(
                        queryDeleteAttendence, admission.getIdStudent(), admission.getIdCourse());
        jdbcTemplate.update(
                        queryDeleteMark, admission.getIdStudent(), admission.getIdCourse());
        return true;
    }

    public List<StudentThemeScheduleWithAttendenceDto> getStudentCourseScheduleWithAttendence(long idStudent, long idCourse){
        String queryGetScheduleWithAttendence = "SELECT theme.title, schedule.start_date, schedule.end_date, attendence.attended FROM theme " +
                                                "left join schedule on theme.id = schedule.id_theme " +
                                                "left join attendence on theme.id = attendence.id_theme " +
                                                "where (theme.id_course=?)and(attendence.id_student=?)";

        return jdbcTemplate.query(queryGetScheduleWithAttendence, new Object[]{idCourse, idStudent}, studentThemeScheduleWithAttendenceMapper);
    }

    public boolean deleteStudentLab(Mark lab){
        String queryDeleteStudentLab = "DELETE FROM mark " +
                                        "where id=?";

        jdbcTemplate.update(queryDeleteStudentLab, lab.getId());

        return true;
    }

}
