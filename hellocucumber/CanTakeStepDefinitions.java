import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.EnrollmentRules.EnrollmentRuleViolation;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.student.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class CanTakeStepDefinitions {

    private Student student;
    private Course course;
    private Course preq1;
    private Course preq2;
    private List<EnrollmentRuleViolation> list;

    private void dataInit() throws ExceptionList {
        preq1 = new Course("6666666", "MATH2", 3, "Undergraduate");
        preq2 = new Course("9999999", "PHYS2", 3, "Undergraduate");
        course = new Course("8888888", "PHYS1", 3, "Undergraduate")
                .withPre(preq1)
                .withPre(preq2);
        student = new Student("810199999", "Undergraduate");
    }

    @Given("I have No Req")
    public void i_have_no_req() throws ExceptionList {
        dataInit();
    }

    @When("I Asking can take the course")
    public void i_asking_can_take_the_course() {
        list = course.canBeTakenBy(student);

    }

    @Then("Violation should not be {int}")
    public void i_can_not_take_the_course(int size) {
        assertNotEquals(list.size(), size);
    }

    @Given("I have Req")
    public void iHaveReq() throws ExceptionList {
        dataInit();
        addPreqForStudent();
    }

    private void addPreqForStudent() throws ExceptionList {
        student.setGrade("11112", preq1, 13);
        student.setGrade("11112", preq2, 16);
    }

    @Then("Violation should be {int}")
    public void violationShouldBe(int size) {
        assertEquals(list.size(), size);
    }
}
