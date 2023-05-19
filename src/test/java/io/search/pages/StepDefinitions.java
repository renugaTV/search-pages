package io.search.pages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import io.cucumber.core.logging.Logger;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.search.InputLengthException;

@ExtendWith(MockitoExtension.class)
public class StepDefinitions {
    @Mock
    Context context;

    @InjectMocks
    Pages pagesLambda;

    Pages pages = new Pages();
    private List<String> actualResult;

    private String incorrectParamErrorMessage;

    private String numberOfParamsErrorMessage;

    @Given("I want to display {int} pages")
    public void i_want_to_display_pages(int maxNumberOfPagesToDisplay) {
        pages.setMaxNumberOfPagesToDisplay(maxNumberOfPagesToDisplay);
    }

    @When("I click page Number {int} AND I have {int} total available results pages")
    public void i_click_page_number_and_i_have_total_available_results_pages(int currentPageNumber, int totalPages) {
        actualResult = pages.retrievePages(currentPageNumber, totalPages);
    }

    @Then("^I should get page Numbers of$")
    public void i_should_get_page_numbers_of(DataTable expectedDataTable) {
        assertEquals(expectedDataTable.transpose().asList(String.class), actualResult);
    }

    @ParameterType("\\[([0-9, ]*)\\]")
    public List<Integer> listOfIntegers(String integers) {
        return Arrays.stream(integers.split(", ?"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @Given("I have a pages Lambda")
    public void i_have_a_pages_lambda() {
        MockitoAnnotations.openMocks(this);
    }

    @When("I sent event with currentPage as {int} totalAvailablePages as {int} and maximumNumberOfPagesToDisplay as {int}")
    @Test
    public void i_sent_event_with_currentpage_totalavailable_pages_and_maximum_number_of_pgaes_to_display(
            int currentPage, int totalAvailablePages, int maximumNumberOfPagesToDisplay) {
        HashMap<String, Integer> inputMap = new HashMap<String, Integer>();
        inputMap.put(Pages.CURRENT_PAGE, currentPage);
        inputMap.put(Pages.TOTAL_AVAILABLE_PAGES, totalAvailablePages);
        inputMap.put(Pages.MAXIMUM_NUMBER_OF_PAGES_TO_DISPLAY, maximumNumberOfPagesToDisplay);
        actualResult = pagesLambda.handleRequest(inputMap, context);
    }

    @When("I sent event with incorrect params of {string} as currentPage")
    public void i_sent_event_with_incorrect_params(String incorrectParam) {
        // Write code here that turns the phrase above into concrete actions
        HashMap<String, Integer> inputMap = new HashMap<String, Integer>();
        inputMap.put(incorrectParam, 1);
        inputMap.put(Pages.TOTAL_AVAILABLE_PAGES, 2);
        inputMap.put(Pages.MAXIMUM_NUMBER_OF_PAGES_TO_DISPLAY, 10);
        try {
            pagesLambda.handleRequest(inputMap, context);
        } catch (IllegalArgumentException e) {
            incorrectParamErrorMessage = e.getMessage();
        }
    }

    @Then("I should get IllegalArgumentException with the message of {string}")
    public void i_should_get_illegal_argument_exception_with_the_message_of(String expectedErrorMessage) {
        assertTrue(incorrectParamErrorMessage.contentEquals(expectedErrorMessage));
    }

    @When("I sent event with no params")
    public void i_sent_event_with_no_params() {
        HashMap<String, Integer> inputMap = new HashMap<String, Integer>();
        try {
            pagesLambda.handleRequest(inputMap, context);
        } catch (InputLengthException e) {
            numberOfParamsErrorMessage = e.getMessage();
        }
    }

    @Then("I should get InputLengthException with the message of {string}")
    public void i_should_get_input_length_exception_with_the_message_of(String expectedErrorMessage) {
        assertTrue(numberOfParamsErrorMessage.contentEquals(expectedErrorMessage));
    }
}
