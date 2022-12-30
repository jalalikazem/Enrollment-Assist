import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.studyRecord.StudyRecord;
import org.junit.Assert;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class IsPassedTheoriesTest {
    @DataPoints
    public static long[] grades() {
        return new long[]{
                16, 15, 14,13, 10, 12};
    }

    @Theory
    public void testIsPassed(long grade) throws ExceptionList {
        String term = "00001";
        String courseNumber = "8101545";
        String title = "ECE_Cource";
        int credits = 3;

        GraduateLevel graduateLevel = GraduateLevel.Undergraduate;
        Course course = new Course(courseNumber, title, credits, graduateLevel.toString());
        StudyRecord studyRecord = new StudyRecord(term, course, grade);
//        Assert.assertEquals(expected, studyRecord.isPassed(course.getGraduateLevel()));
        var actual = studyRecord.isPassed(course.getGraduateLevel());
        Assert.assertEquals(actual, true);
    }

}
