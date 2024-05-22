package io.codelex.flightplanner.dto.response;

import io.codelex.flightplanner.domain.Flight;

import java.util.List;
import java.util.Objects;

public class SearchFlightResponse {
    private int totalItems;
    private int page;
    private List<Flight> items;

    public SearchFlightResponse(int totalItems, int page, List<Flight> items) {
        this.totalItems = totalItems;
        this.page = page;
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<Flight> getItems() {
        return items;
    }

    public void setItems(List<Flight> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchFlightResponse that = (SearchFlightResponse) o;
        return page == that.page && totalItems == that.totalItems && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, totalItems, items);
    }
}
