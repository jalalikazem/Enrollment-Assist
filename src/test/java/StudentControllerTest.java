import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.controller.student.StudentController;
import ir.proprog.enrollassist.controller.student.StudentView;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.program.Program;
import ir.proprog.enrollassist.domain.program.ProgramType;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.domain.student.StudentNumber;
import ir.proprog.enrollassist.domain.user.User;
import ir.proprog.enrollassist.repository.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentControllerTest {
    @Mock StudentRepository studentRepository;
    @Mock CourseRepository courseRepository;
    @Mock SectionRepository sectionRepository;
    @Mock EnrollmentListRepository enrollmentListRepository;
    @Mock UserRepository userRepository;

    @InjectMocks private StudentController studentController;

    @Test////behavior verification -- spy
    public void getAllStudentTest(){
        var response = studentController.all();// using spy
        verify(studentRepository).findAll();
    }

    @Test//behavior verification -- Stub
    public void getStudentWithNumberTest(){

        when(studentRepository.findByStudentNumber(new StudentNumber("1234")))
                .thenReturn(Optional.of(new Student("1234")));

        var response = studentController.one("1234");
        Assert.assertEquals(response.getStudentNo().getNumber(), "1234");
    }

    @Test//behavior verification -- Mockisty -- Mock
    public void getStudentFailedTest(){

        when(studentRepository.findByStudentNumber(new StudentNumber("1234")))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        try{
            var response = studentController.one("1234");
            Assert.fail();
        }catch (ResponseStatusException ex){
        }
    }

    @Test//state verification -- Mockisty -- Mock
    public void addStudentTest(){

        StudentView studentView = mock(StudentView.class);
        when(studentView.getStudentNo()).thenReturn(new StudentNumber("1234"));
        when(studentView.getUserId()).thenReturn("1");
        when(studentView.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);

        when(userRepository.findByUserId("1"))
                .thenReturn(Optional.of(new User("afshin", "1")));

        var response = studentController.addStudent(studentView);

        Assert.assertEquals(response.getStudentNo().getNumber(), "1234");
        Assert.assertEquals(response.getGraduateLevel(), GraduateLevel.Undergraduate);
    }

    @Test//state verification -- Mockisty -- Mock
    public void addStudentAlreadyExists() throws ExceptionList {

        StudentView studentView = mock(StudentView.class);
        when(studentView.getStudentNo()).thenReturn(new StudentNumber("1234"));
        when(studentView.getUserId()).thenReturn("1");
        when(studentView.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);

        when(userRepository.findByUserId("1"))
                .thenReturn(Optional.of(new User("afshin", "1")));
        when(studentRepository.findByStudentNumber(studentView.getStudentNo()))
                .thenReturn(Optional.of(new Student("1234", GraduateLevel.Undergraduate.toString())));
        try{
            var response = studentController.addStudent(studentView);
            Assert.fail();
        }catch (ResponseStatusException exception){

        }
    }

    @Test//behavior verification -- Mockisty -- Mock
    public void findTackableSectionByMajorTest() throws Exception {
        var studentMock = mock(Student.class);
        when(studentRepository.findByStudentNumber(new StudentNumber("1234")))
                .thenReturn(Optional.of(studentMock));

        List<Section> sections = new ArrayList<>();
        sections.add(new Section(new Course("4253545", "title", 1, GraduateLevel.Undergraduate.toString()),"1" ));
        when(sectionRepository.findAll()).thenReturn(sections);
        when(studentMock.getTakeableSections(any(sections.getClass()))).thenReturn(sections);
        var response = studentController.findTakeableSectionsByMajor("1234");

        Assert.assertEquals(StreamSupport.stream(response.spliterator(), false).toList().get(0).getSectionNo(), "1");
    }

    @Test//behavior verification -- Mockisty -- dummy object
    public void findTackableSectionByMajorFaildTest(){
        try{
            var response = studentController.findTakeableSectionsByMajor("1234");
            Assert.fail();
        }catch (ResponseStatusException ex){

        }
    }
}
