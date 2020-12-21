package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Schedule;
import com.epam.rd.izh.entity.Theme;
import com.epam.rd.izh.exception.IncorrectDataException;
import com.epam.rd.izh.repository.CourseRepository;
import org.hibernate.validator.cfg.defs.NegativeDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Course getCourse(CourseDto courseDto){

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
    public List<Map<String, Object>> getStudentCourses(String login){
        long id_student = userService.getAuthorizedUserId(login);

        return courseRepository.getDescribedOnCourses(id_student);
    }

    @Override
    public Theme getTheme(long idCourse, ThemeDto themeDto){

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
    public Material getMaterial(long idTheme, MaterialDto materialDto){
        return new Material().builder()
                .id(materialDto.getId())
                .title(materialDto.getTitle())
                .type(materialDto.getType())
                .path(materialDto.getPath())
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
    public CourseDto updateCourseThemesMaterials(CourseDto courseDto){

            Course course = getCourse(courseDto);

        if(courseDto.getNeedAction() == NeedAction.UPDATE) {
            courseRepository.updateCourse(course);
        }

            for(ThemeDto themeDto: courseDto.getThemes()) {
                Theme theme = getTheme(course.getId(), themeDto);

                switch (themeDto.getNeedAction()) {
                    case NONE:{
                        updateMaterials(theme.getId(), themeDto.getMaterials());
                        break;
                    }
                    case UPDATE:
                        courseRepository.updateTheme(theme);
                        updateMaterials(theme.getId(), themeDto.getMaterials());
                        break;
                    case ADD:
                        courseRepository.addTheme(theme);
                        updateMaterials(theme.getId(), themeDto.getMaterials());
                        break;
                    case DELETE:
                        courseRepository.deleteTheme(theme.getId());
                        break;
                }
            }

            // обновить CourseDto
            String login = courseDto.getTeacherLogin();

        return getCourseDto(login, course);
    }

    private boolean updateMaterials(long idTheme, List<MaterialDto> materialDtoList){
        boolean result = true;

        for(MaterialDto materialDto: materialDtoList){
            Material material = getMaterial(idTheme, materialDto);

            switch (materialDto.getNeedAction()) {
                case UPDATE:
                    result = result && courseRepository.updateMaterial(material);
                    break;
                case ADD:
                    result = result && courseRepository.addMaterial(material);
                    //TODO
                    // обновить поле id в materialDto
                    break;
                case DELETE:
                    result = result && courseRepository.deleteMaterial(material.getId());
                    // TODO
                    // удалить materialDto из списка
                    break;
            }
        }
        return  result;
    }

    public List<ScheduleDto> getCourseScheduleDto(long id){
        List<Schedule> scheduleList = courseRepository.getCourseSchedule(id);

        return scheduleList.stream().map(p -> getScheduleDto(p)).collect(Collectors.toList());
    }

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

    public boolean updateCourseSchedule(List<ScheduleDto> scheduleDtoList)
                            throws IncorrectDataException {

       for(ScheduleDto sheduleDto: scheduleDtoList){
               Schedule schedule = getSchedule(sheduleDto);
               courseRepository.updateSchedule(schedule);
       }

        return true;
    }

    public List<ParticipantDto> getCourseParticipants(long idCourse){
        return courseRepository.getCourseParticipants(idCourse);
    }

    public List<MarkDto> getCourseMarks(long idCourse){
        return courseRepository.getCourseMarks(idCourse);
    }
}
