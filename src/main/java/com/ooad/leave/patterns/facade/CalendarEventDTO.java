package com.ooad.leave.patterns.facade;

import java.time.LocalDate;

public class CalendarEventDTO {
    private String title;
    private LocalDate start;
    private LocalDate end;
    private String color;
    private String status;

    public CalendarEventDTO() {}

    public CalendarEventDTO(String title, LocalDate start, LocalDate end, String color, String status) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.color = color;
        this.status = status;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getStart() { return start; }
    public void setStart(LocalDate start) { this.start = start; }

    public LocalDate getEnd() { return end; }
    public void setEnd(LocalDate end) { this.end = end; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
