package com.epam.rd.izh;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.*;
import com.epam.rd.izh.repository.CourseRepository;
import com.epam.rd.izh.repository.RoleRepository;
import com.epam.rd.izh.repository.UserRepository;
import com.epam.rd.izh.service.RoleService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLErrorCodes;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.servlet.http.Part;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "com.epam.rd.izh")
@ActiveProfiles("test")
public class WorkWithDBTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DateTimeFormatter dateTimeFormatter;

    private static AuthorizedUser authorizedUser;
    private static Course course;
    private static Theme theme;
    private static Material material;
    private static Schedule schedule;

    @BeforeAll
    public static void init()
    {
        authorizedUser = new AuthorizedUser().builder()
                .id(1)
                .firstName("test")
                .secondName("test")
                .lastName("test")
                .birthDate(LocalDate.of(1980, 6, 5))
                .role("TEACHER")
                .login("testAdd")
                .password("19801980")
                .build();

        course = new Course().builder()
                .title("testCourse")
                .description("test Course")
                .hours(2)
                .id_teacher(1)
                .build();

        theme = new Theme().builder()
                .id_Course(1)
                .title("testTheme")
                .build();

        material = new Material().builder()
                .idTheme(1)
                .idCourse(1)
                .type("Лекция")
                .title("test Material")
                .path("testPath\\testPath")
                .build();

        schedule = new Schedule().builder()
                .id(4)
                .idTheme(2)
                .startDate(LocalDate.of(2021, 1, 5))
                .build();
    }

    @Test
    @DisplayName("Тест метода - получение Map<String, String> ролей")
    void getRolesTest() {
        System.out.println("");
        Map<String, String> rolesMap = roleRepository.getRoles();

        assertTrue(rolesMap.containsKey("Студент"));
    }

    @Test
    @DisplayName("Тест метода - getRolesTitles() - получение названия ролей на русском")
    void getRolesTitlesTest() {
        assertTrue(roleService.getRolesTitles().size() > 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"IIIvan"})
    @DisplayName("Тест метода - getUserByLogin()")
    void getAuthorizedUserByLoginTest(String login) {
        assertNotNull( userRepository.getUserByLogin(login));
    }

    @Test
    @DisplayName("Тест метода - addUser")
    void addUserTest()
    {
        assertTrue(userRepository.addUser(authorizedUser));
    }

    @Test
    @DisplayName("Тест метода - getUserIdByLogin()")
    void getUserIdByLoginTest(){
        long idUserExpected = 2;

        assertEquals(idUserExpected, userRepository.getUserIdByLogin("PPPetrov"));
    }

    @Test
    @DisplayName("Тест метода - getLoginById()")
    void getLoginById(){
        String loginExpected = "admin";

        assertEquals(loginExpected, userRepository.getLoginById(3));
    }

    @Test
    @DisplayName("Тест метода - IsUserInDB()")
    void IsUserInDBTestShouldReturnTrue(){
        String login = "IIIvan";

        assertTrue(userRepository.IsUserInDB(login));
    }

    @Test
    @DisplayName("Тест метода - IsUserInDB()")
    void IsUserInDBTestShouldReturnFalse(){
        String login = "Ivan";

        assertFalse(userRepository.IsUserInDB(login));
    }

    @Test
    @DisplayName("Тест метода - addCourse()")
    void addCourseTest(){
        assertTrue(courseRepository.addCourse(course));
    }

    @Test
    @DisplayName("Тест метода - addTheme()")
    void addThemeTest(){
        assertTrue(courseRepository.addTheme(theme));
    }

    @Test
    @DisplayName("Тест метода - addMaterial()")
    void addMaterialTest(){
        assertTrue(courseRepository.addMaterial(material));
    }

    @Test
    @DisplayName("Тест метода - getCourseById()")
    void getCourseByIdTest(){
        course = new Course().builder()
                .id(1)
                .title("Методы оптимизации")
                .description("Цели данного курса – развить навыки формализации проблемы в виде оптимизационной задачи, освоение методов нахождения наилучших решений, приобрести навыки применения соответствующего программного обеспечения.")
                .hours(36)
                .id_teacher(1)
                .build();

        Course actualCourse = courseRepository.getCourseById(1);
        assertEquals(course, actualCourse);
    }

    @Test
    @DisplayName("Тест метода - getCourseById()")
    void getCourseByIdTestShoudThrowException(){

        assertThrows(EmptyResultDataAccessException.class, ()->courseRepository.getCourseById(40));
    }

    @Test
    @DisplayName("Тест метода - getCourses()")
    void getCoursesTest(){

        assertThat(courseRepository.getCourses(2),
                Matchers.hasItem(Matchers.<Map<String, Object>>allOf(hasEntry("id", 3),
                        hasEntry("title", "test Course1"))));

        assertThat(courseRepository.getCourses(2),
                Matchers.hasItem(Matchers.<Map<String, Object>>allOf(hasEntry("id", 4),
                        hasEntry("title", "test Course2"))));

    }


    @Test
    @DisplayName("Тест метода - getThemeList()")
    void getThemeListTest(){
        Theme theme1 = new Theme().builder()
                .id(4)
                .id_Course(2)
                .title("Теория множеств")
                .build();

        Theme theme2 = new Theme().builder()
                .id(5)
                .id_Course(2)
                .title("Булевы функции")
                .build();

        List<Theme> themeList = courseRepository.getThemeList(2);

        assertThat(themeList, hasItems(theme1, theme2));
    }

    @Test
    @DisplayName("Тест метода - getMaterialsList()")
    void getMaterialsListTest(){
        Material material1 = new Material().builder()
                                .id(4)
                                .title("Операции над множествами")
                                .type("Лекция")
                                .path("path4_2")
                                .idTheme(4)
                                .idCourse(2)
                                .build();
        Material material2 = new Material().builder()
                                .id(5)
                                .title("Декартово произведение")
                                .type("Лекция")
                                .path("path4_2")
                                .idTheme(4)
                                .idCourse(2)
                                .build();

        List<Material> expectedList = courseRepository.getMaterialsList(4);
        assertThat(expectedList, hasItems(material1, material2));
    }

    @Test
    @DisplayName("Тест метода - deleteCourse()")
    void deleteCourseTest(){
        assertTrue(courseRepository.deleteCourse(3));
    }

    @Test
    @DisplayName("Тест метода - updateCourse()")
    void updateCourseTest(){
        course = new Course().builder()
                .id(2)
                .title("Прикладная математика")
                .description("test Course")
                .hours(40)
                .id_teacher(2)
                .build();

        assertTrue(courseRepository.updateCourse(course));
    }

    @Test
    @DisplayName("Тест метода - updateTheme()")
    void updateThemeTest(){
        theme = new Theme().builder()
                .id(4)
                .id_Course(2)
                .title("Численные методы вычисления производной")
                .build();

        assertTrue(courseRepository.updateTheme(theme));
    }

    @Test
    @DisplayName("Тест метода - updateMaterial()")
    void updateMaterialTest(){
        material = new Material().builder()
                .id(4)
                .idTheme(4)
                .idCourse(2)
                .type("Лабораторная")
                .title("Вычисление производных")
                .path("")
                .build();

        assertTrue(courseRepository.updateMaterial(material));
    }

    @Test
    @DisplayName("Тест метода - deleteTheme()")
    void deleteThemeTest(){
        assertTrue(courseRepository.deleteTheme(5));
    }

    @Test
    @DisplayName("Тест метода - deleteMaterial()")
    void deleteMaterialTest(){
        assertTrue(courseRepository.deleteMaterial(5));
    }

    @Test
    @DisplayName("Тест метода - getCourseSchedule()")
    void getCourseScheduleTest(){

        List<Schedule> scheduleCourse = courseRepository.getCourseSchedule(1);

        Schedule expectedSchedule = new Schedule().builder()
                                        .id(1)
                                        .idTheme(1)
                                        .themeTitle("Основные понятия в теории экстремальных задач")
                                        .startDate(LocalDate.of(2020, 12,12))
                                        .endDate(LocalDate.of(2020, 12, 18))
                                        .build();

        assertThat(scheduleCourse, hasItem(expectedSchedule));
    }

    @Test
    @DisplayName("Тест метода - addThemeInSchedule()")
    void addThemeInScheduleTest(){

        assertThrows(DataAccessException.class, () -> {
            courseRepository.addThemeInSchedule(1);
        });
    }

    @Test
    @DisplayName("Тест метода - updateSchedule()")
    void updateScheduleTest(){

        assertTrue(courseRepository.updateSchedule(schedule));

    }

    @Test
    @DisplayName("Тест метода - getCourseMarks()")
    void  getCourseMarksTest(){
        MarkDto expectedMarkDto = new MarkDto().builder()
                                    .id(2)
                                    .idLab(6)
                                    .labDescription("Множества")
                                    .idStudent(4)
                                    .firstName("Мария")
                                    .lastName("Ромашкина")
                                    .mark(0)
                                    .pathToLab("testPath\\\\testPath1")
                                    .build();

        List<MarkDto> marks = courseRepository.getCourseMarks(2);
        assertThat(marks, hasItem(expectedMarkDto));
    }

    @Test
    @DisplayName("Тест метода - updateMark()")
    void  updateMarkTest(){
        Mark mark = new Mark().builder()
                .id(2)
                .idLab(3)
                .idStudent(4)
                .path("testPath\\testPath")
                .mark(5)
                .build();

        assertTrue(courseRepository.updateMark(mark));
    }

    @Test
    @DisplayName("Тест метода - addLab()")
    void  addLabTest(){
       List<Theme> themeList = courseRepository.getThemeList(2);

        Mark mark = new Mark().builder()
                .idLab(4)
                .idStudent(4)
                .path("testPath\\testPath")
                .build();

        assertTrue(courseRepository.addLab(mark));
    }

    @Test
    @DisplayName("Тест метода - addLab()")
    void  addLabTestShouldThrowException(){
        Mark mark = new Mark().builder()
                .idLab(15)
                .idStudent(4)
                .path("testPath\\testPath")
                .build();

        assertThrows(DataIntegrityViolationException.class, ()-> courseRepository.addLab(mark));
    }

    @Test
    @DisplayName("Тест метода - updateLab()")
    void  updateLabTestShouldThrowException(){
        Mark mark = new Mark().builder()
                .idLab(15)
                .idStudent(4)
                .path("testPath\\testPath")
                .build();

        assertThrows(DataIntegrityViolationException.class, ()-> courseRepository.addLab(mark));
    }

    @Test
    @DisplayName("Тест метода - deleteStudentLab()")
    void  deleteStudentLabTest(){
        Mark lab = new Mark().builder()
                .idLab(150)
                .idStudent(4)
                .path("testPath\\testPath")
                .build();

        assertTrue(courseRepository.deleteStudentLab(lab));
    }

    @Test
    @DisplayName("Tecт метода getCourseParticipants()")
    void getCourseParticipantsTest(){
        List<ThemeAttendenceDto> themeAttendenceDtoList = new ArrayList<>();
        themeAttendenceDtoList.add(new ThemeAttendenceDto(1,true));
        themeAttendenceDtoList.add(new ThemeAttendenceDto(2,false));
        themeAttendenceDtoList.add(new ThemeAttendenceDto(3,false));

        ParticipantDto participantDto = new ParticipantDto().builder()
                                                            .idStudent(4)
                                                            .lastName("Ромашкина")
                                                            .firstName("Мария")
                                                            .secondName("Федоровна")
                                                            .birthDate("2000-03-07")
                                                            .attendenceList(themeAttendenceDtoList)
                                                            .build();

        List<ParticipantDto> participantDtoList = courseRepository.getCourseParticipants(1);

        assertThat(participantDtoList, hasItem(participantDto));
    }

    @Test
    @DisplayName("Тест метода updateAttendence()")
    void updateAttendenceTest(){
        Attendence attendence = new Attendence().builder()
                                                .idStudent(4)
                                                .idTheme(1)
                                                .attended(true)
                                                .build();

        assertTrue(courseRepository.updateAttendence(attendence));
    }

    @Test
    @DisplayName("Тест метода getStudentCourse()")
    void getStudentCourseTest(){
        StudentCourseDto studentCourseDto = new StudentCourseDto().builder().idCourse(1).courseTitle("Методы оптимизации").build();

        assertThat(courseRepository.getStudentCourses(4), hasItem(studentCourseDto));
    }

    @Test
    @DisplayName("Тест метода getStudentPossibleCourses()")
    void getStudentPossibleCoursesTest(){
        StudentPossibleCourseDto studentPossibleCourseDto = new StudentPossibleCourseDto().builder()
                .idCourse(3)
                .hours(4)
                .courseTitle("test Course1")
                .teacherName("Петров П.П.")
                .build();

        List<StudentPossibleCourseDto> studentPossibleCourseDtoList = courseRepository.getStudentPossibleCourses(4);
        assertThat(studentPossibleCourseDtoList, hasItem(studentPossibleCourseDto));
    }

    @Test
    @DisplayName("Тест метода getCourseLabsList()")
    void getCourseLabsListTest(){
        StudentCourseLabDto courseLabDto = new StudentCourseLabDto().builder()
                                                .idLab(11)
                                                .labTitle("test material2")
                                                .build();

        List<StudentCourseLabDto> studentLabs = courseRepository.getStudentCourseLabList(4,3);

        assertThat(studentLabs, hasItem(courseLabDto));
    }

    @Test
    @DisplayName("Тест метода - addStudentCourseAdmission()")
    void addStudentCourseAdmissionTest(){
        Admission admission = new Admission().builder().idStudent(4).idCourse(4).build();

        assertTrue(courseRepository.addStudentCourseAdmission(admission));
        assertTrue(courseRepository.getStudentCourseScheduleWithAttendence(4,4).size()>0);
    }

    @Test
    @DisplayName("Тест метода - getFullUserName()")
    void getFullUserNameTest(){
        assertEquals("Иванов Иван Иванович", userRepository.getFullUserName("IIIvan"));
    }

    @Test
    @DisplayName("Тест метода -getStudentCourseScheduleWithAttendence()")
    void getStudentCourseScheduleWithAttendence(){
        StudentThemeScheduleWithAttendenceDto studentThemeScheduleWithAttendenceDto = new StudentThemeScheduleWithAttendenceDto().builder()
                                                                                                .themeTitle("Теория множеств")
                                                                                                .attended(false)
                                                                                                .startDate("2021-01-05")
                                                                                                .build();

        List<StudentThemeScheduleWithAttendenceDto> courseStudentScheduleAttendence = courseRepository.getStudentCourseScheduleWithAttendence(4,2);

        assertThat(courseStudentScheduleAttendence, hasItem(studentThemeScheduleWithAttendenceDto));
    }

    @Test
    @DisplayName("Тест метода - deleteStudentCourseAdmission()")
    void deleteStudentCourseAdmissionTest(){
        Admission admission = new Admission().builder()
                .idCourse(3)
                .idStudent(4)
                .build();

        assertTrue(courseRepository.deleteStudentCourseAdmission(admission));
        assertTrue(courseRepository.getStudentCourseScheduleWithAttendence(4,3).isEmpty());
    }

    @Test
    @DisplayName("Тест метода - deleteUser()")
    void deleteUserTest(){
        assertTrue(userRepository.deleteUser(4));
        assertTrue(courseRepository.getStudentCourseScheduleWithAttendence(4,3).isEmpty());
    }
}
