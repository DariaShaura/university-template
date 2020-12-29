package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.*;
import com.epam.rd.izh.exception.IncorrectDataException;
import com.epam.rd.izh.repository.CourseRepository;
import org.hibernate.validator.cfg.defs.NegativeDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseServiceMapper implements CourseService{

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserService userService;

    @Autowired
    DateTimeFormatter dateTimeFormatter;

    @Autowired
    UserFolderService userFolderService;

    @Override
    public Course getCourse(CourseDto courseDto) throws IncorrectDataException {

        if(courseDto.getTitle().equals("") || courseDto.getDescription().equals("") ||
                (courseDto.getHours() == 0)){
            throw new IncorrectDataException("Есть незаполненные свойства", courseDto);
        }


        Course course = new Course().builder()
                            .id(courseDto.getId())
                            .title(courseDto.getTitle())
                            .description(courseDto.getDescription())
                            .hours(courseDto.getHours())
                            .build();

        long id_teacher = userService.getAuthorizedUserId(courseDto.getTeacherLogin());

        course.setId_teacher(id_teacher);

        return course;
    }

    @Override
    public Course getCourse(long id){
        return courseRepository.getCourseById(id);
    }

    @Override
    public List<Map<String, Object>> getTeachersCourses(String login){

        long id_teacher = userService.getAuthorizedUserId(login);

        return courseRepository.getCourses(id_teacher);
    }

    @Override
    public Theme getTheme(long idCourse, ThemeDto themeDto) throws IncorrectDataException {

        if(themeDto.getTitle().equals("")){
            throw new IncorrectDataException("Есть незаполненные свойства", themeDto);
        }

        Theme theme = new Theme().builder()
                                .id(themeDto.getId())
                                .title(themeDto.getTitle())
                                .id_Course(idCourse)
                                .build();

        return theme;
    }

    @Override
    public List<Theme> getCourseThemes(long idCourse){
        return courseRepository.getThemeList(idCourse);
    }

    @Override
    public boolean addCourse(Course course){
        return courseRepository.addCourse(course);
    }

    @Override
    public CourseDto addCourse(CourseDto courseDto)
                throws IncorrectDataException{
        Course course = getCourse(courseDto);

        List<Theme> themeList = new ArrayList<>();
        for (ThemeDto themeDto : courseDto.getThemes()) {
            Theme theme = getTheme(course.getId(), themeDto);
            themeList.add(theme);
        }

        List<List<Material>> themeMaterialList = new ArrayList<>();

        for (ThemeDto p : courseDto.getThemes()) {
            List<Material> list = new ArrayList<>();
            for (MaterialDto materialDto : p.getMaterials()) {
                Material material = getMaterial(course.getId(),0, materialDto);
                list.add(material);
            }
            themeMaterialList.add(list);
        }

        try{
            courseRepository.addCourseThemesMaterials(course, themeList, themeMaterialList);
            courseDto.setId(course.getId());

            for(int i=0; i< themeList.size(); i++){
                courseDto.getThemes().get(i).setId(themeList.get(i).getId());

                for(int j=0; j<courseDto.getThemes().get(i).getMaterials().size(); j++){
                    courseDto.getThemes().get(i).getMaterials().get(j).setId(themeMaterialList.get(i).get(j).getId());
                }
            }
        }
        catch (RuntimeException e){
            throw new IncorrectDataException("Ошибка добавления курса в БД", null);
        }

        return courseDto;
    }

    @Override
    public boolean addTheme(Theme theme){

        return courseRepository.addTheme(theme) && courseRepository.addThemeInSchedule(theme.getId());
    }

    @Override
    public boolean addMaterial(Material material){
        return courseRepository.addMaterial(material);
    }

    @Override
    public MaterialDto getMaterialDto(Material material){
        return new MaterialDto().builder()
                .id(material.getId())
                .title(material.getTitle())
                .type(material.getType())
                .path(material.getPath())
                .build();
    }

    @Override
    public Material getMaterial(long idCourse, long idTheme, MaterialDto materialDto) throws IncorrectDataException {
        if(materialDto.getType().equals("") || materialDto.getPath().equals("")){
            throw  new IncorrectDataException("Есть незаполненные свойства", materialDto);
        }

        return new Material().builder()
                .id(materialDto.getId())
                .title(materialDto.getTitle())
                .type(materialDto.getType())
                .path(materialDto.getPath())
                .idCourse(idCourse)
                .idTheme(idTheme)
                .build();
    }

    @Override
    public List<Material> getThemeMaterials(long idTheme){
        return courseRepository.getMaterialsList(idTheme);
    }

    @Override
    public List<MaterialDto> getMaterialsDto(List<Material> materialList){
        return materialList.stream().map((s) -> getMaterialDto(s)).collect(Collectors.toList());
    }

    @Override
    public ThemeDto getThemeDto(Theme theme, List<MaterialDto> materialsDto){
        return new ThemeDto().builder()
                .id(theme.getId())
                .title(theme.getTitle())
                .materials(materialsDto)
                .build();
    }

    @Override
    public CourseDto getCourseDto(long idCourse){
        Course course = getCourse(idCourse);
        String teacherLogin = userService.getAuthorizedUserLogin(course.getId_teacher());
        String teacherName = userService.getFullUserName(teacherLogin);

        CourseDto courseDto = getCourseDto(teacherLogin, course);
        courseDto.setTeacherName(teacherName);

        return courseDto;
    }

    @Override
    public CourseDto getCourseDto(String login, Course course){

        List<Theme> themes = getCourseThemes(course.getId());

        List<ThemeDto> themeDtoList = new ArrayList<>();
        for(Theme theme: themes){
            long idTheme = theme.getId();
            // получить список материалов
            List<Material> materials = getThemeMaterials(idTheme);
            List<MaterialDto> materialDtoList = getMaterialsDto(materials);

            themeDtoList.add(getThemeDto(theme, materialDtoList));
        }

        return new CourseDto().builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .hours(course.getHours())
                .themes(themeDtoList)
                .teacherLogin(login)
                .build();
    }



    @Override
    public boolean deleteCourse(long id){
            return courseRepository.deleteCourse(id);
    }

    @Override
    public CourseDto updateCourseThemesMaterials(CourseDto courseDto) throws IncorrectDataException, IOException {

        Course course = getCourse(courseDto);

        // обновить CourseDto
        String login = courseDto.getTeacherLogin();

        if(courseDto.getNeedAction() == NeedAction.UPDATE) {
            courseRepository.updateCourse(course);
        }

            for(ThemeDto themeDto: courseDto.getThemes()) {
                Theme theme = getTheme(course.getId(), themeDto);

                switch (themeDto.getNeedAction()) {
                    case NONE:{
                        updateMaterials(login, theme.getId_Course(), theme.getId(), themeDto.getMaterials());
                        break;
                    }
                    case UPDATE:
                        courseRepository.updateTheme(theme);
                        updateMaterials(login, theme.getId_Course(), theme.getId(), themeDto.getMaterials());
                        break;
                    case ADD:
                        courseRepository.addTheme(theme);
                        updateMaterials(login, theme.getId_Course(), theme.getId(), themeDto.getMaterials());
                        break;
                    case DELETE:
                        courseRepository.deleteTheme(theme.getId());

                            themeDto.getMaterials().stream().forEach(
                                    materialDto -> {if(materialDto.getId() != -1){
                                        userFolderService.deleteUserDir(userFolderService.getUserDirFile(login +"\\" +course.getId() + "\\"+ materialDto.getId()));
                                    };
                                    }
                            );
                        break;
                }
            }

        return getCourseDto(login, course);
    }

    private boolean updateMaterials(String login, long idCourse, long idTheme, List<MaterialDto> materialDtoList) throws IncorrectDataException, IOException {
        boolean result = true;

        for(MaterialDto materialDto: materialDtoList){
            Material material = getMaterial(idCourse, idTheme, materialDto);

            switch (materialDto.getNeedAction()) {
                case UPDATE:
                    result = result && courseRepository.updateMaterial(material);
                    break;
                case ADD:
                    result = result && courseRepository.addMaterial(material);

                    break;
                case DELETE:
                    result = result && courseRepository.deleteMaterial(material.getId());

                    break;
            }
        }
        return  result;
    }

    @Override
    public List<ScheduleDto> getCourseScheduleDto(long id){
        List<Schedule> scheduleList = courseRepository.getCourseSchedule(id);

        return scheduleList.stream().map(p -> getScheduleDto(p)).collect(Collectors.toList());
    }

    @Override
    public Schedule getSchedule(ScheduleDto scheduleDto)
            throws IncorrectDataException{

        // проверка корректности данных
        String startDateString = scheduleDto.getStartDate();
        String endDateString = scheduleDto.getEndDate();

        LocalDate startDate = startDateString != "" ? LocalDate.parse(scheduleDto.getStartDate(), dateTimeFormatter) : null;
        LocalDate endDate = endDateString != "" ? LocalDate.parse(scheduleDto.getEndDate(), dateTimeFormatter) : null;

        if((startDate != null) && (endDate != null) && endDate.isBefore(startDate)){
                throw new IncorrectDataException("Дата окончания изучения темы должна быть позже даты начала!", scheduleDto);
        }

        return  new Schedule().builder()
                                .id(scheduleDto.getId())
                                .idTheme(scheduleDto.getIdTheme())
                                .themeTitle(scheduleDto.getThemeTitle())
                                .startDate(startDate)
                                .endDate(endDate)
                                .build();
    }

    @Override
    public ScheduleDto getScheduleDto(Schedule schedule){

        String startDate = schedule.getStartDate() != null ? schedule.getStartDate().toString() : "";
        String endDate = schedule.getEndDate() != null ? schedule.getEndDate().toString() : "";

        return  new ScheduleDto().builder()
                .id(schedule.getId())
                .idTheme(schedule.getIdTheme())
                .themeTitle(schedule.getThemeTitle())
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @Override
    public boolean updateCourseSchedule(List<ScheduleDto> scheduleDtoList)
                            throws IncorrectDataException {

       for(ScheduleDto sheduleDto: scheduleDtoList){
               Schedule schedule = getSchedule(sheduleDto);
               courseRepository.updateSchedule(schedule);
       }

        return true;
    }

    @Override
    public List<ParticipantDto> getCourseParticipants(long idCourse){
        return courseRepository.getCourseParticipants(idCourse);
    }

    @Override
    public List<MarkDto> getCourseMarks(long idCourse){
        return courseRepository.getCourseMarks(idCourse);
    }

    @Override
    public boolean updateCourseMarks(List<MarkDto> markDtoList)
                    throws IncorrectDataException{

        for(MarkDto markDto: markDtoList){
            Mark mark = getMark(markDto);
            courseRepository.updateMark(mark);
        }

        return true;
    }

    @Override
    public Mark getMark(MarkDto markDto)
            throws IncorrectDataException{
        if((markDto.getMark() < 2) || (markDto.getMark() > 5) ){
            throw new IncorrectDataException("Оценка должна быть от 2 до 5", markDto);
        }

        return new Mark().builder()
                            .id(markDto.getId())
                            .idStudent(markDto.getIdStudent())
                            .idLab(markDto.getIdLab())
                            .path(markDto.getPathToLab())
                            .mark(markDto.getMark())
                            .build();
    }

    @Override
    public boolean updateCourseAttendence(List<ParticipantDto> participantDtoList){

        for(ParticipantDto participantDto: participantDtoList){
            for(ThemeAttendenceDto themeAttendenceDto: participantDto.getAttendenceList()) {
                Attendence attendence = new Attendence().builder()
                        .idStudent(participantDto.getIdStudent())
                        .idTheme(themeAttendenceDto.getIdTheme())
                        .attended(themeAttendenceDto.getAttendence())
                        .build();

                courseRepository.updateAttendence(attendence);
            }
        }

        return true;
    }

    @Override
    public List<StudentCourseDto> getStudentsCourseList(String login){
        long idStudent = userService.getAuthorizedUserId(login);

        return courseRepository.getStudentCourses(idStudent);
    }

    @Override
    public List<StudentPossibleCourseDto> getStudentPossibleCourseList(String login){
        long idStudent = userService.getAuthorizedUserId(login);

        return courseRepository.getStudentPossibleCourses(idStudent);
    }

    @Override
    public List<StudentCourseLabDto> getStudentCourseLabList(String login, long idCourse){
        long idStudent = userService.getAuthorizedUserId(login);

        return  courseRepository.getStudentCourseLabList(idStudent, idCourse);
    }

    @Override
    public boolean addStudentAdmissionOnCourse(String login, long idCourse){
        long idStudent = userService.getAuthorizedUserId(login);

        Admission admission = new Admission().builder()
                                            .idStudent(idStudent)
                                            .idCourse(idCourse)
                                            .build();

        return courseRepository.addStudentCourseAdmission(admission);
    }

    @Override
    public boolean deleteStudentAdmissionOnCourse(String login, long idCourse){
        long idStudent = userService.getAuthorizedUserId(login);

        Admission admission = new Admission().builder()
                .idStudent(idStudent)
                .idCourse(idCourse)
                .build();



        return courseRepository.deleteStudentCourseAdmission(admission);
    }

    @Override
    public List<StudentThemeScheduleWithAttendenceDto> getStudentCourseScheduleWithAttendence(String login, long idCourse){
        long idStudent = userService.getAuthorizedUserId(login);

        return courseRepository.getStudentCourseScheduleWithAttendence(idStudent, idCourse);
    }

    @Override
    public boolean addLab(Mark mark){
        return courseRepository.addLab(mark);
    }

    @Override
    public boolean updateLab(Mark mark){
        return courseRepository.updateMark(mark);
    }

    @Override
    public Mark getLab(long idStudent, StudentCourseLabDto studentCourseLabDto){
        return new Mark().builder()
                .id(studentCourseLabDto.getIdMark())
                .idStudent(idStudent)
                .idLab(studentCourseLabDto.getIdLab())
                .path(studentCourseLabDto.getPath())
                .build();
    }

    @Override
    public boolean updateStudentLab(String login, StudentCourseLabDto studentCourseLabDto){
        long idStudent = userService.getAuthorizedUserId(login);

        Mark lab = getLab(idStudent, studentCourseLabDto);

        if(lab.getId() == 0){
            return courseRepository.addLab(lab);
        }
        else{
            return courseRepository.updateLab(lab);
        }
    }

    @Override
    public boolean deleteStudentLab(String login, StudentCourseLabDto studentCourseLabDto){
        long idStudent = userService.getAuthorizedUserId(login);

        Mark lab = getLab(idStudent, studentCourseLabDto);

        return courseRepository.deleteStudentLab(lab);
    }
}
