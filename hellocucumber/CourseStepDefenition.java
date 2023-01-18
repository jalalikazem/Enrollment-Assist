import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.course.Course;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseStepDefenition {

    private String title;
    private String courseNumber;
    private int credits;
    private String graduateLevel;

    private Course course;

    private ExceptionList exceptionList = new ExceptionList();

    @Given("title {string} and CourseNumber {string} and credits {int} and graduateLevel {string}")
    public void title_and_course_number_and_credits_and_graduate_level(String title, String courseNumber, Integer credits, String graduateLevel) {
        this.title = title;
        this.credits = credits;
        this.courseNumber = courseNumber;
        this.graduateLevel = graduateLevel;
    }

    @When("I Create a new Course")
    public void i_create_a_new_course() {
        try {
           course = new Course(this.courseNumber, this.title, this.credits, this.graduateLevel);
        }
        catch (ExceptionList exceptionList) {
            this.exceptionList = exceptionList;
        }

    }

    @Then("Course Should be created with {int} exception")
    public void course_should_be_created(int exceptionCount) {
        assertEquals(exceptionList.getExceptions().size(), exceptionCount);
    }
}
