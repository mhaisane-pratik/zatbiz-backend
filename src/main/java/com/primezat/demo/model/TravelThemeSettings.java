package com.primezat.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "travel_theme_settings")
public class TravelThemeSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long projectId;

    private String themeColor;
    private String customColorHex;

    @Column(columnDefinition = "TEXT")
    private String logoUrl;

    @Column(columnDefinition = "TEXT")
    private String faviconUrl;

    @Column(columnDefinition = "TEXT")
    private String bannerUrl;

    @Column(columnDefinition = "TEXT")
    private String heroVideoUrl;

    @Column(columnDefinition = "TEXT")
    private String companyImages;

    @Column(columnDefinition = "TEXT")
    private String galleryImages;

    @Column(columnDefinition = "TEXT")
    private String sectionsLayoutJson;

    private String domainType;
    private String subdomainName;
    private String customDomainName;

    @Column(columnDefinition = "TEXT")
    private String bookingSettingsJson;

    public TravelThemeSettings() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public String getThemeColor() { return themeColor; }
    public void setThemeColor(String themeColor) { this.themeColor = themeColor; }

    public String getCustomColorHex() { return customColorHex; }
    public void setCustomColorHex(String customColorHex) { this.customColorHex = customColorHex; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getFaviconUrl() { return faviconUrl; }
    public void setFaviconUrl(String faviconUrl) { this.faviconUrl = faviconUrl; }

    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }

    public String getHeroVideoUrl() { return heroVideoUrl; }
    public void setHeroVideoUrl(String heroVideoUrl) { this.heroVideoUrl = heroVideoUrl; }

    public String getCompanyImages() { return companyImages; }
    public void setCompanyImages(String companyImages) { this.companyImages = companyImages; }

    public String getGalleryImages() { return galleryImages; }
    public void setGalleryImages(String galleryImages) { this.galleryImages = galleryImages; }

    public String getSectionsLayoutJson() { return sectionsLayoutJson; }
    public void setSectionsLayoutJson(String sectionsLayoutJson) { this.sectionsLayoutJson = sectionsLayoutJson; }

    public String getDomainType() { return domainType; }
    public void setDomainType(String domainType) { this.domainType = domainType; }

    public String getSubdomainName() { return subdomainName; }
    public void setSubdomainName(String subdomainName) { this.subdomainName = subdomainName; }

    public String getCustomDomainName() { return customDomainName; }
    public void setCustomDomainName(String customDomainName) { this.customDomainName = customDomainName; }

    public String getBookingSettingsJson() { return bookingSettingsJson; }
    public void setBookingSettingsJson(String bookingSettingsJson) { this.bookingSettingsJson = bookingSettingsJson; }
}
