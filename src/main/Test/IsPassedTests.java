import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.studyRecord.StudyRecord;
import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

@RunWith(value = Parameterized.class)
public class IsPassedTests {


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

    private static Stream<Arguments> provideData() {
        String term = "00001";
        String courseNumber = getRandomNumericString(7);
        String title = "ECE_Cource";

        return Stream.of(
                Arguments.of(term, courseNumber, title, 1, GraduateLevel.Undergraduate, 8, false),
                Arguments.of(term, courseNumber, title, 2, GraduateLevel.Undergraduate, 10, true),
                Arguments.of(term, courseNumber, title, 3, GraduateLevel.Undergraduate, 14, true),
                Arguments.of(term, courseNumber, title, 2, GraduateLevel.Masters, 8, false),
                Arguments.of(term, courseNumber, title, 1, GraduateLevel.Masters, 10, false),
                Arguments.of(term, courseNumber, title, 2, GraduateLevel.Masters, 14, true),
                Arguments.of(term, courseNumber, title, 3, GraduateLevel.PHD, 8, false),
                Arguments.of(term, courseNumber, title, 2, GraduateLevel.PHD, 12, false),
                Arguments.of(term, courseNumber, title, 1, GraduateLevel.PHD, 15, true)
        );
    }
    @ParameterizedTest
    @MethodSource("provideData")
    void testIsPassed(String term, String courseNumber, String title,
                                                    int credits, GraduateLevel graduateLevel, long grade, boolean expected) throws ExceptionList {

        Course course = new Course(courseNumber, title, credits, graduateLevel.toString());
        StudyRecord studyRecord = new StudyRecord(term, course, grade);
        Assert.assertEquals(expected, studyRecord.isPassed(course.getGraduateLevel()));
    }
}
