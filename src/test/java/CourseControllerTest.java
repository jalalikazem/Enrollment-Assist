import com.fasterxml.jackson.databind.ObjectMapper;
import ir.proprog.enrollassist.DataInitializer;
import ir.proprog.enrollassist.EnrollAssistApplication;
import ir.proprog.enrollassist.controller.course.CourseController;
import ir.proprog.enrollassist.controller.course.CourseMajorView;
import ir.proprog.enrollassist.controller.course.CourseView;
import ir.proprog.enrollassist.domain.course.AddCourseService;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.repository.CourseRepository;
import net.minidev.json.parser.JSONParser;
import org.json.JSONObject;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CourseRepository courseRepository;


    @Autowired
    private ObjectMapper objectMapper;

    @Before
    void addCourseToDb() throws Exception {

        Course farsi = new Course("1212121", "FA", 2, "Undergraduate");
        Course english = new Course("1010101", "EN", 2, "Undergraduate");
        Course akhlagh = new Course("1111110", "AKHLAGH", 2, "Undergraduate");
        Course karafarini = new Course("1313131", "KAR", 3, "Undergraduate");
        courseRepository.saveAll(List.of(farsi, english, akhlagh, karafarini));

    }

    @Test
    void getAllCourse_thenReturns200() throws Exception {
        mockMvc.perform(get("/courses")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getCourse_thenReturns404() throws Exception {
        mockMvc.perform(get("/courses/434354545")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCourse_thenReturnsOk() throws Exception {

        mockMvc.perform(get("/courses/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));;
    }

    @Test
    void addCourse_thenReturnsOk() throws Exception {
        Course dm = new Course("3333333", "DM", 3, "Undergraduate");

        var course = new CourseMajorView(dm, new HashSet<Long>(), new HashSet<Long>());
        mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk());
    }

    @Test
    void addCourse_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/courses")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
