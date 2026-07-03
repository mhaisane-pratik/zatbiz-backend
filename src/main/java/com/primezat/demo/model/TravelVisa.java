package com.primezat.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "travel_visa")
public class TravelVisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;
    private String visaType;
    private String country;

    @Column(columnDefinition = "TEXT")
    private String documentsRequired;

    private Double fees;
    private String approvalStatus;

    public TravelVisa() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public String getVisaType() { return visaType; }
    public void setVisaType(String visaType) { this.visaType = visaType; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDocumentsRequired() { return documentsRequired; }
    public void setDocumentsRequired(String documentsRequired) { this.documentsRequired = documentsRequired; }

    public Double getFees() { return fees; }
    public void setFees(Double fees) { this.fees = fees; }

    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
}
