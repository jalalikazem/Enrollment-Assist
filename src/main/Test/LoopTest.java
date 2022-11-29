import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.controller.course.CourseMajorView;
import ir.proprog.enrollassist.domain.course.AddCourseService;
import ir.proprog.enrollassist.domain.course.Course;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;


public class LoopTest {

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
    public Course GenerateRandomCourse() throws ExceptionList {
        String courseNumber = getRandomNumericString(7);
        String title = "ECE_Course";
        Random random = new Random();
        int credit = random.nextInt(2) + 1;
        String graduateLevel = "PHD";
        Course course = new Course(courseNumber, title, credit, graduateLevel);
        return course;
    }

    private AddCourseService addCourseService;

    @Test(expected = Exception.class)
    public void testCheckLoopFunc() throws ExceptionList {

        Course mainCourse = GenerateRandomCourse();
        Course pre1 = GenerateRandomCourse();
        Course pre2 = GenerateRandomCourse();
        var mainPreSet = new HashSet<Course>();
        mainPreSet.add(pre1);
        mainPreSet.add(pre2);
        mainCourse.setPrerequisites(mainPreSet);

        Course pre1Pre = GenerateRandomCourse();
        var pre1preSet = new HashSet<Course>();
        pre1preSet.add(pre1Pre);
        pre1.setPrerequisites(pre1preSet);

        var pre1prePreSet = new HashSet<Course>();
        pre1prePreSet.add(mainCourse);
        pre1Pre.setPrerequisites(pre1prePreSet);

        addCourseService.addCourse(new CourseMajorView(mainCourse, new HashSet<Long>(), new HashSet<Long>()));
        addCourseService.addCourse(new CourseMajorView(pre1, new HashSet<Long>(), new HashSet<Long>()));
        addCourseService.addCourse(new CourseMajorView(pre2, new HashSet<Long>(), new HashSet<Long>()));
        addCourseService.addCourse(new CourseMajorView(pre1Pre, new HashSet<Long>(), new HashSet<Long>()));

    }
}
