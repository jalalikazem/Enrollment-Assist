package koofti;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.proprog.enrollassist.DataInitializer;
import ir.proprog.enrollassist.EnrollAssistApplication;
import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.controller.course.CourseController;
import ir.proprog.enrollassist.controller.course.CourseMajorView;
import ir.proprog.enrollassist.controller.course.CourseView;
import ir.proprog.enrollassist.domain.course.AddCourseService;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.enrollmentList.EnrollmentList;
import ir.proprog.enrollassist.domain.major.Faculty;
import ir.proprog.enrollassist.domain.major.Major;
import ir.proprog.enrollassist.domain.program.Program;
import ir.proprog.enrollassist.domain.program.ProgramType;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.PresentationSchedule;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.repository.CourseRepository;
import net.minidev.json.parser.JSONParser;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EnrollAssistApplication.class)
@AutoConfigureMockMvc
public class CourseControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    CourseRepository courseRepository;
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Before
//    void addCourseToDb() throws Exception {
//
//        Course farsi = new Course("1212121", "FA", 2, "Undergraduate");
//        Course english = new Course("1010101", "EN", 2, "Undergraduate");
//        Course akhlagh = new Course("1111110", "AKHLAGH", 2, "Undergraduate");
//        Course karafarini = new Course("1313131", "KAR", 3, "Undergraduate");
//        courseRepository.saveAll(List.of(farsi, english, akhlagh, karafarini));
//
//    }
//
//    @Test
//    void getAllCourse_thenReturns200() throws Exception {
//        mockMvc.perform(get("/courses")
//                        .contentType("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    void getCourse_thenReturns404() throws Exception {
//        mockMvc.perform(get("/courses/434354545")
//                        .contentType("application/json"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void getCourse_thenReturnsOk() throws Exception {
//
//        mockMvc.perform(get("/courses/1")
//                        .contentType("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));;
//    }
//
//    @Test
//    void addCourse_thenReturnsOk() throws Exception {
//        Course dm = new Course("3333333", "DM", 3, "Undergraduate");
//
//        var course = new CourseMajorView(dm, new HashSet<Long>(), new HashSet<Long>());
//        mockMvc.perform(post("/courses")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(course)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void addCourse_thenReturnsBadRequest() throws Exception {
//        mockMvc.perform(post("/courses")
//                        .contentType("application/json"))
//                .andExpect(status().isBadRequest());
//    }

    private static String getRandomNumericString(int length) {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public Course getRandomCourse() throws ExceptionList {
        return new Course(getRandomNumericString(7), "ECE_" + getRandomNumericString(5), 3 , "Undergraduate");
    }
    public String getRandomDayOfWeek(){
        var dayOfWeeks = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday"};
        Random rand = new Random();
        return dayOfWeeks[rand.nextInt(5)];
    }
    public String getRandomTime(int index){
        var times = new String[]{"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"};
        return times[index];
    }
    public Set<PresentationSchedule> getRandomPresentationSchedual() throws ExceptionList {
        Random random = new Random();
        var index1 = random.nextInt(7);
        var index2 = random.nextInt(7);
        var p1 = new PresentationSchedule(getRandomDayOfWeek(), getRandomTime(index1), getRandomTime(index1 + 2));
        var p2 = new PresentationSchedule(getRandomDayOfWeek(), getRandomTime(index2), getRandomTime(index2 + 2));
        Set<PresentationSchedule> set = new HashSet<PresentationSchedule>(){{
            add(p1);
            add(p2);
        }};
        return set;
    }

    @Test
    public void checkGPALimitTest() throws Exception {

        Student student = new Student("810197485", "Undergraduate");
        var major = new Major("01", "CE", Faculty.Engineering.toString());
        Course course1 = new Course(getRandomNumericString(7), "ece1", 2, "Undergraduate");
        Course course2 = new Course(getRandomNumericString(7), "ece1", 2, "Undergraduate");
        Course course3 = new Course(getRandomNumericString(7), "ece1", 3, "Undergraduate");
        Course course4 = new Course(getRandomNumericString(7), "ece1", 2, "Undergraduate");
        Course course5 = new Course(getRandomNumericString(7), "ece1", 3, "Undergraduate");

        Program program = new Program(major, "Undergraduate", 1, 3, ProgramType.Major.name());
        program.addCourse(course1,course2, course3, course4, course5);

        student.addProgram(program);
        student.setGrade("00001", course1, 0);
        student.setGrade("00001", course2, 10);
        student.setGrade("00001", course3, 9);
        student.setGrade("00001", course4, 11);
        student.setGrade("00001", course5, 10);
//        Section s1 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime("2021-06-23T08:00","2021-06-23T10:00"), getRandomPresentationSchedual());
//        Section s2 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime("2021-06-22T08:00","2021-06-22T10:00"), getRandomPresentationSchedual());
//        Section s3 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime("2021-06-21T08:00","2021-06-21T10:00"), getRandomPresentationSchedual());
//        Section s4 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime("2021-06-20T08:00","2021-06-20T10:00"), getRandomPresentationSchedual());

        Section s1 = new Section(course1, getRandomNumericString(2), new ExamTime("2021-06-23T08:00","2021-06-23T10:00"), getRandomPresentationSchedual());
        Section s2 = new Section(course2, getRandomNumericString(2), new ExamTime("2021-06-22T08:00","2021-06-22T10:00"), getRandomPresentationSchedual());
        Section s3 = new Section(course3, getRandomNumericString(2), new ExamTime("2021-06-21T08:00","2021-06-21T10:00"), getRandomPresentationSchedual());
        Section s4 = new Section(course4, getRandomNumericString(2), new ExamTime("2021-06-20T08:00","2021-06-20T10:00"), getRandomPresentationSchedual());
        Section s5 = new Section(course5, getRandomNumericString(2), new ExamTime("2021-06-19T08:00","2021-06-19T10:00"), getRandomPresentationSchedual());
        EnrollmentList enrollmentList = new EnrollmentList("List", student);
        enrollmentList.addSection(s1);
        enrollmentList.addSection(s2);
        enrollmentList.addSection(s3);
        enrollmentList.addSection(s4);
        enrollmentList.addSection(s5);

        var violets = enrollmentList.checkValidGPALimit();
        Assert.assertEquals(violets.size(), 0);
    }
}
