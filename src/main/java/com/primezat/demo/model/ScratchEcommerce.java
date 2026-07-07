package com.primezat.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "scratch_ecommerce")
public class ScratchEcommerce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long projectId;

    private String storeName = "My Store";
    private String businessName;
    private String ownerName;
    private String email;
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String logoUrl;

    @Column(columnDefinition = "TEXT")
    private String faviconUrl;

    private String primaryColor = "#5300b7";
    private String secondaryColor = "#fea619";
    private String font = "Plus Jakarta Sans";
    private String buttonStyle = "rounded";

    private String currency = "USD";
    private String country = "United States";
    private String language = "en";
    private String timeZone = "UTC";
    private Double taxRate = 0.0;

    private Boolean paymentStripe = false;
    private Boolean paymentRazorpay = false;
    private Boolean paymentPaypal = false;
    private Boolean paymentCod = false;

    private Boolean shippingFlatRate = false;
    private Double shippingFlatRateFee = 0.0;
    private Boolean shippingFree = false;
    private Boolean shippingPickup = false;

    private String status = "Draft";
    private Boolean wizardCompleted = false;

    @Column(columnDefinition = "TEXT")
    private String builderJson;

    @Column(columnDefinition = "TEXT")
    private String themeJson;

    private String subdomain;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public ScratchEcommerce() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getFaviconUrl() {
        return faviconUrl;
    }

    public void setFaviconUrl(String faviconUrl) {
        this.faviconUrl = faviconUrl;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getButtonStyle() {
        return buttonStyle;
    }

    public void setButtonStyle(String buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Boolean getPaymentStripe() {
        return paymentStripe;
    }

    public void setPaymentStripe(Boolean paymentStripe) {
        this.paymentStripe = paymentStripe;
    }

    public Boolean getPaymentRazorpay() {
        return paymentRazorpay;
    }

    public void setPaymentRazorpay(Boolean paymentRazorpay) {
        this.paymentRazorpay = paymentRazorpay;
    }

    public Boolean getPaymentPaypal() {
        return paymentPaypal;
    }

    public void setPaymentPaypal(Boolean paymentPaypal) {
        this.paymentPaypal = paymentPaypal;
    }

    public Boolean getPaymentCod() {
        return paymentCod;
    }

    public void setPaymentCod(Boolean paymentCod) {
        this.paymentCod = paymentCod;
    }

    public Boolean getShippingFlatRate() {
        return shippingFlatRate;
    }

    public void setShippingFlatRate(Boolean shippingFlatRate) {
        this.shippingFlatRate = shippingFlatRate;
    }

    public Double getShippingFlatRateFee() {
        return shippingFlatRateFee;
    }

    public void setShippingFlatRateFee(Double shippingFlatRateFee) {
        this.shippingFlatRateFee = shippingFlatRateFee;
    }

    public Boolean getShippingFree() {
        return shippingFree;
    }

    public void setShippingFree(Boolean shippingFree) {
        this.shippingFree = shippingFree;
    }

    public Boolean getShippingPickup() {
        return shippingPickup;
    }

    public void setShippingPickup(Boolean shippingPickup) {
        this.shippingPickup = shippingPickup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getWizardCompleted() {
        return wizardCompleted;
    }

    public void setWizardCompleted(Boolean wizardCompleted) {
        this.wizardCompleted = wizardCompleted;
    }

    public String getBuilderJson() {
        return builderJson;
    }

    public void setBuilderJson(String builderJson) {
        this.builderJson = builderJson;
    }

    public String getThemeJson() {
        return themeJson;
    }

    public void setThemeJson(String themeJson) {
        this.themeJson = themeJson;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
