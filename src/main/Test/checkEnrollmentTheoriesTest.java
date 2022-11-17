import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.enrollmentList.EnrollmentList;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.PresentationSchedule;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.domain.studyRecord.StudyRecord;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(Theories.class)
public class checkEnrollmentTheoriesTest {
    @DataPoints
    public static ExamTime[] examTimes() throws Exception {
        ExamTime et1 = new ExamTime("10:00", "12:00");
        ExamTime et2 = new ExamTime("08:00", "10:00");
        ExamTime et3 = new ExamTime("12:00", "14:00");
        ExamTime et4 = new ExamTime("14:00", "16:00");
        ExamTime et5 = new ExamTime("16:00", "18:00");
        ExamTime et6 = new ExamTime("08:00", "12:00");
        ExamTime et7 = new ExamTime("09:00", "11:00");
        ExamTime et8 = new ExamTime("13:00", "15:00");
        ExamTime et9 = new ExamTime("14:00", "17:00");
        ExamTime et10 = new ExamTime("10:00", "13:00");
        return new ExamTime[]{
            et1, et2, et3, et4, et5, et6, et7, et8, et9, et10
        };
    }
    @DataPoints
    public static String[] TimePicker(){
        return new String[]{
                "2021-06-21T08:00",
                "2021-06-21T10:00",
                "2021-06-21T09:00",
                "2021-06-21T10:00",
                "2021-06-21T13:00",
                "2021-06-21T14:00",
                "2021-06-22T08:00",
                "2021-06-22T10:00",
                "2021-06-22T12:00",
                "2021-06-22T16:00",
                "2021-06-23T08:00",
                "2021-06-23T10:00",
                "2021-06-24T08:00",
                "2021-06-24T10:00",

        };
    }

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
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Theory
    public void checkEnrollment(String t1, String t2, String t3, String t4) throws Exception {
        expectedException.expect(ExceptionList.class);
        Student student = new Student("810197485", "Undergraduate");
        Section s1 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime(t1,t2), getRandomPresentationSchedual());
        Section s2 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime(t2,t3), getRandomPresentationSchedual());
        Section s3 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime(t3,t4), getRandomPresentationSchedual());
        Section s4 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime(t4,t1), getRandomPresentationSchedual());
        EnrollmentList enrollmentList = new EnrollmentList("List", student);
        enrollmentList.addSection(s1);
        enrollmentList.addSection(s2);
        enrollmentList.addSection(s3);
        enrollmentList.addSection(s4);

        var violets = enrollmentList.checkEnrollmentRules();
        Assert.assertEquals(violets.size(), 0);
    }


}
