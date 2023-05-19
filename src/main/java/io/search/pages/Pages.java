package io.search.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import io.search.InputLengthException;

public class Pages implements RequestHandler<Map<String, Integer>, List<String>> {
    private int maxNumberOfPagesToDisplay = 10;
    public static final String NEXT = "Next";
    public static final String PREVIOUS = "Previous";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String TOTAL_AVAILABLE_PAGES = "totalAvailablePages";
    public static final String MAXIMUM_NUMBER_OF_PAGES_TO_DISPLAY = "maximumNumberOfPagesToDisplay";

    @Override
    public List<String> handleRequest(Map<String, Integer> event, Context context) {
        if (event.size() != 3) {
            throw new InputLengthException(
                    "Input must be a Map that contains 3 params " + CURRENT_PAGE + ", "
                            + MAXIMUM_NUMBER_OF_PAGES_TO_DISPLAY + " and " + TOTAL_AVAILABLE_PAGES);
        }
        if (!event.containsKey(CURRENT_PAGE) || !event.containsKey(TOTAL_AVAILABLE_PAGES)
                || !event.containsKey(MAXIMUM_NUMBER_OF_PAGES_TO_DISPLAY)) {
            throw new IllegalArgumentException(
                    "input is missing required params of " + CURRENT_PAGE + ", " + MAXIMUM_NUMBER_OF_PAGES_TO_DISPLAY
                            + " or " + TOTAL_AVAILABLE_PAGES);
        }
        this.setMaxNumberOfPagesToDisplay(event.get(MAXIMUM_NUMBER_OF_PAGES_TO_DISPLAY));
        List<String> response = this.retrievePages(event.get(CURRENT_PAGE), event.get(TOTAL_AVAILABLE_PAGES));
        return response;
    }

    public List<String> retrievePages(int currentPageNumber, int totalPagesOfResults) {
        List<Integer> resultPages = new ArrayList<>();
        int leftIndex = currentPageNumber - 1;
        int rightIndex = currentPageNumber + 1;
        int count = 1;
        if (totalPagesOfResults < this.maxNumberOfPagesToDisplay) {
            this.setMaxNumberOfPagesToDisplay(totalPagesOfResults);
        }
        resultPages.add(currentPageNumber);
        while (count < this.maxNumberOfPagesToDisplay) {
            if (leftIndex > 0) {
                resultPages.add(leftIndex);
                count++;
                leftIndex--;
            }
            if (count < this.maxNumberOfPagesToDisplay && rightIndex <= totalPagesOfResults) {
                resultPages.add(rightIndex);
                count++;
                rightIndex++;
            }
        }
        Collections.sort(resultPages);
        List<String> listOfResults = resultPages.stream().map(Object::toString)
                .collect(Collectors.toList());
        if (currentPageNumber < totalPagesOfResults) {
            listOfResults.add(resultPages.size(), NEXT);
        }
        if (currentPageNumber > 1) {
            listOfResults.add(0, PREVIOUS);
        }
        return listOfResults;
    }

    public void setMaxNumberOfPagesToDisplay(int maxNumberOfPagesToDisplay) {
        this.maxNumberOfPagesToDisplay = maxNumberOfPagesToDisplay;
    }
}
