package io.search.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import io.search.InputLengthException;

public class Pages implements RequestHandler<Map<String, Integer>, List<Integer>> {
    private int maxNumberOfPagesToDisplay = 10;
    private final List<Page> pageList = new ArrayList<>();

    @Override
    public List<Integer> handleRequest(Map<String, Integer> event, Context context) {
        if (event.size() != 3) {
            throw new InputLengthException(
                    "Input must be a Map that contains 3 params currentPage, maximumNumberOfPagesToDisplay and totalAvailablePages");
        }
        if (!event.containsKey("currentPage") || !event.containsKey("totalAvailablePages")
                || !event.containsKey("maximumNumberOfPagesToDisplay")) {
            throw new IllegalArgumentException(
                    "input is missing required params of currentPage, maximumNumberOfPagesToDisplay or totalAvailablePages");
        }
        this.setMaxNumberOfPagesToDisplay(event.get("maximumNumberOfPagesToDisplay"));
        List<Integer> response = this.retrievePages(event.get("currentPage"), event.get("totalAvailablePages"));
        return response;
    }

    public List<Integer> retrievePages(int currentPageNumber, int totalPagesOfResults) {
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
        return resultPages;
    }

    public void setMaxNumberOfPagesToDisplay(int maxNumberOfPagesToDisplay) {
        this.maxNumberOfPagesToDisplay = maxNumberOfPagesToDisplay;
    }
}
