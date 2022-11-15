import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.section.PresentationSchedule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class HasConflictTest {

    PresentationSchedule testData;
    PresentationSchedule testClass;
    @Before
    public void provideTestData() throws ExceptionList {
        testData = new PresentationSchedule("Monday", "08:00", "10:00");
        testClass = new PresentationSchedule();
    }
    @Test
    public void testEndBeforeStart() {
        try {
            testData = new PresentationSchedule("Monday", "10:00", "08:00");
            testClass.hasConflict(testData);
        } catch (ExceptionList e) {
            Assert.assertEquals("End time can not be before start time.", e.getExceptions().get(0).getMessage());
        }
    }
    @Test
    public void testInvalidDayOfWeek() {
        try {
            testData = new PresentationSchedule("Mondday", "10:00", "12:00");
            testClass.hasConflict(testData);
        } catch (ExceptionList e) {
            Assert.assertEquals(String.format("%s is not valid week day.", "Mondday"), e.getExceptions().get(0).getMessage());
        }
    }

    @Test
    public void testInvalidTimeFormat() {
        try {
            testData = new PresentationSchedule("Monday", "10", "12:00");
            testClass.hasConflict(testData);
        } catch (ExceptionList e) {
            Assert.assertEquals("Time format is not valid", e.getExceptions().get(0).getMessage());
        }
    }

    @Test(expected = Exception.class)
    public void testMultipleErrors() throws ExceptionList {
        testData = new PresentationSchedule("Shanbe", "10", "08:00");
        testClass.hasConflict(testData);
    }

    @Test
    public void testSuccess() throws ExceptionList {
        testData = new PresentationSchedule("Saturday", "10:00", "12:00");
        var result = testClass.hasConflict(testData);
        Assert.assertEquals(true, result);
    }
}
