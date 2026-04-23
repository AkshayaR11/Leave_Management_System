package com.ooad.leave.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @Column(length = 2000)
    private String content;
    
    private LocalDateTime generatedOn;
    
    @ManyToOne
    private Admin generatedBy;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public LocalDateTime getGeneratedOn() { return generatedOn; }
    public void setGeneratedOn(LocalDateTime generatedOn) { this.generatedOn = generatedOn; }
    
    public Admin getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(Admin generatedBy) { this.generatedBy = generatedBy; }
}
