package io.search.pages;

public class Page {
    private int pageNumber;
    private String content;

    Page(int pageNumber, String content) {
        this.pageNumber = pageNumber;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void addContent(String newString) {
        this.content.concat(newString);
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
