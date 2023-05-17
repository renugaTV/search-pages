package io.search.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pages {
    private int maxNumberOfPagesToDisplay = 10;
    private final List<Page> pageList = new ArrayList<>();

    Pages() {

    }

    Pages(List<Page> pages) {
        this.pageList.addAll(pages);
    }

    public void addPages(Page page) {
        pageList.add(page);
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
