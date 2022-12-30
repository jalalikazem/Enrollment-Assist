import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.enrollmentList.EnrollmentList;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.PresentationSchedule;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CA5 {

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
        Section s1 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime("2021-06-23T08:00","2021-06-23T10:00"), getRandomPresentationSchedual());
        Section s2 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime("2021-06-22T08:00","2021-06-22T10:00"), getRandomPresentationSchedual());
        Section s3 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime("2021-06-21T08:00","2021-06-21T10:00"), getRandomPresentationSchedual());
        Section s4 = new Section(getRandomCourse(), getRandomNumericString(2), new ExamTime("2021-06-20T08:00","2021-06-20T10:00"), getRandomPresentationSchedual());
        EnrollmentList enrollmentList = new EnrollmentList("List", student);
        enrollmentList.addSection(s1);
        enrollmentList.addSection(s2);
        enrollmentList.addSection(s3);
        enrollmentList.addSection(s4);

        var violets = enrollmentList.checkValidGPALimit();
        Assert.assertEquals(violets.size(), 0);
    }
    public CA5() throws ExceptionList {
    }
}
