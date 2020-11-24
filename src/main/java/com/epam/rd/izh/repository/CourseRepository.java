package com.epam.rd.izh.repository;

import com.epam.rd.izh.entity.AuthorizedUser;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Theme;
import com.epam.rd.izh.service.CourseMapper;
import com.epam.rd.izh.service.MaterialMapper;
import com.epam.rd.izh.service.ThemeMapper;
import com.epam.rd.izh.service.UserService;
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
    public boolean addMaterial(@Nullable Material material) {

        if (material != null) {

            String query_insertMaterial = "insert into material (title, type, path, id_theme) "+"" +
                    "                 VALUES (?, ?, ?, ?)";
            return jdbcTemplate.update(
                    query_insertMaterial,
                    material.getTitle(), material.getType(), material.getPath(),
                    material.getId_Theme()
            ) > 0;
        }
        return false;
    }

    public Course getCourseById(long id){
        String query_getCourseByLogin = "SELECT * FROM course WHERE course.id = ?";

        Course course = jdbcTemplate.queryForObject(query_getCourseByLogin, new Object[]{ id }, courseMapper);

        return course;
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
}
