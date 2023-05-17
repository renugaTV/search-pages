package io.search.pages;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {
    Pages pages;
    private List<Integer> actualResult;

    @Given("I want to display {int} pages")
    public void i_want_to_display_pages(int maxNumberOfPagesToDisplay) {
        pages = new Pages();
        pages.setMaxNumberOfPagesToDisplay(maxNumberOfPagesToDisplay);
    }

    @When("I click page Number {int} AND I have {int} total available results pages")
    public void i_click_page_number_and_i_have_total_available_results_pages(int currentPageNumber, int totalPages) {
        actualResult = pages.retrievePages(currentPageNumber, totalPages);
        System.out.println("actualResult");
        System.out.println(actualResult);
    }

    @ParameterType("\\[([0-9, ]*)\\]")
    public List<Integer> listOfIntegers(String integers) {
        return Arrays.stream(integers.split(", ?"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @Then("I should get page Numbers of {listOfIntegers}")
    public void i_should_get_page_numbers_of(List<Integer> expectedList) {
        assertEquals(expectedList, actualResult);
    }
}
