package com.primezat.demo.controller;

import com.primezat.demo.model.*;
import com.primezat.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/travel")
@CrossOrigin(origins = "*")
public class TravelBusinessApiController {

    @Autowired
    private TravelAgencyInfoRepository travelAgencyInfoRepository;

    @Autowired
    private TravelPackageRepository travelPackageRepository;

    @Autowired
    private TravelBookingRepository travelBookingRepository;

    @Autowired
    private TravelDestinationRepository travelDestinationRepository;

    @Autowired
    private TravelHotelRepository travelHotelRepository;

    @Autowired
    private TravelFlightRepository travelFlightRepository;

    @Autowired
    private TravelVisaRepository travelVisaRepository;

    @Autowired
    private TravelThemeSettingsRepository travelThemeSettingsRepository;

    // ==========================================
    // 1. Travel Agency Info Mappings (Original Base Endpoints)
    // ==========================================

    @GetMapping
    public ResponseEntity<TravelAgencyInfo> getTravelInfo(@RequestParam Long projectId) {
        Optional<TravelAgencyInfo> infoOpt = travelAgencyInfoRepository.findByProjectId(projectId);
        if (infoOpt.isPresent()) {
            return ResponseEntity.ok(infoOpt.get());
        }
        
        TravelAgencyInfo defaultInfo = new TravelAgencyInfo();
        defaultInfo.setProjectId(projectId);
        defaultInfo.setSubcategory("Domestic Travel");
        defaultInfo.setBusinessName("Wanderlust Travel");
        return ResponseEntity.ok(defaultInfo);
    }

    @PostMapping
    public ResponseEntity<TravelAgencyInfo> createTravelInfo(@RequestParam Long projectId, @RequestBody TravelAgencyInfo travelInfo) {
        travelInfo.setProjectId(projectId);
        Optional<TravelAgencyInfo> opt = travelAgencyInfoRepository.findByProjectId(projectId);
        if (opt.isPresent()) {
            TravelAgencyInfo existing = opt.get();
            updateTravelInfoFields(existing, travelInfo);
            return ResponseEntity.ok(travelAgencyInfoRepository.save(existing));
        }
        return ResponseEntity.ok(travelAgencyInfoRepository.save(travelInfo));
    }

    @PutMapping
    public ResponseEntity<TravelAgencyInfo> updateTravelInfo(@RequestParam Long projectId, @RequestBody TravelAgencyInfo travelInfo) {
        travelInfo.setProjectId(projectId);
        Optional<TravelAgencyInfo> opt = travelAgencyInfoRepository.findByProjectId(projectId);
        TravelAgencyInfo toSave;
        if (opt.isPresent()) {
            toSave = opt.get();
            updateTravelInfoFields(toSave, travelInfo);
        } else {
            toSave = travelInfo;
        }
        return ResponseEntity.ok(travelAgencyInfoRepository.save(toSave));
    }

    private void updateTravelInfoFields(TravelAgencyInfo dest, TravelAgencyInfo src) {
        if (src.getSubcategory() != null) dest.setSubcategory(src.getSubcategory());
        if (src.getBusinessName() != null) dest.setBusinessName(src.getBusinessName());
        if (src.getOwnerName() != null) dest.setOwnerName(src.getOwnerName());
        if (src.getEmail() != null) dest.setEmail(src.getEmail());
        if (src.getWhatsappNo() != null) dest.setWhatsappNo(src.getWhatsappNo());
        if (src.getPhoneNo() != null) dest.setPhoneNo(src.getPhoneNo());
        if (src.getGstNumber() != null) dest.setGstNumber(src.getGstNumber());
        if (src.getWebsiteName() != null) dest.setWebsiteName(src.getWebsiteName());
        if (src.getCountry() != null) dest.setCountry(src.getCountry());
        if (src.getState() != null) dest.setState(src.getState());
        if (src.getAddress() != null) dest.setAddress(src.getAddress());
        if (src.getDescription() != null) dest.setDescription(src.getDescription());
        if (src.getLogoUrl() != null) dest.setLogoUrl(src.getLogoUrl());
        if (src.getThemeColor() != null) dest.setThemeColor(src.getThemeColor());
    }

    // ==========================================
    // 2. Travel Packages Mappings
    // ==========================================

    @GetMapping("/packages")
    public List<TravelPackage> getPackages(@RequestParam Long projectId) {
        return travelPackageRepository.findByProjectId(projectId);
    }

    @PostMapping("/packages")
    public TravelPackage createPackage(@RequestParam Long projectId, @RequestBody TravelPackage travelPackage) {
        travelPackage.setProjectId(projectId);
        return travelPackageRepository.save(travelPackage);
    }

    @PutMapping("/packages/{id}")
    public ResponseEntity<TravelPackage> updatePackage(@PathVariable Long id, @RequestParam Long projectId, @RequestBody TravelPackage updated) {
        Optional<TravelPackage> opt = travelPackageRepository.findById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        TravelPackage existing = opt.get();
        existing.setProjectId(projectId);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getDestination() != null) existing.setDestination(updated.getDestination());
        if (updated.getCountry() != null) existing.setCountry(updated.getCountry());
        if (updated.getDuration() != null) existing.setDuration(updated.getDuration());
        if (updated.getPrice() != null) existing.setPrice(updated.getPrice());
        if (updated.getDiscount() != null) existing.setDiscount(updated.getDiscount());
        if (updated.getImagesJson() != null) existing.setImagesJson(updated.getImagesJson());
        if (updated.getVideosJson() != null) existing.setVideosJson(updated.getVideosJson());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getInclusions() != null) existing.setInclusions(updated.getInclusions());
        if (updated.getExclusions() != null) existing.setExclusions(updated.getExclusions());
        if (updated.getItineraryJson() != null) existing.setItineraryJson(updated.getItineraryJson());
        if (updated.getPickup() != null) existing.setPickup(updated.getPickup());
        if (updated.getDropPoint() != null) existing.setDropPoint(updated.getDropPoint());
        if (updated.getMeals() != null) existing.setMeals(updated.getMeals());
        if (updated.getHotel() != null) existing.setHotel(updated.getHotel());
        if (updated.getFlightIncluded() != null) existing.setFlightIncluded(updated.getFlightIncluded());
        if (updated.getGuideIncluded() != null) existing.setGuideIncluded(updated.getGuideIncluded());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        return ResponseEntity.ok(travelPackageRepository.save(existing));
    }

    @DeleteMapping("/packages/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id, @RequestParam Long projectId) {
        if (travelPackageRepository.existsById(id)) {
            travelPackageRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // 3. Travel Bookings Mappings
    // ==========================================

    @GetMapping("/bookings")
    public List<TravelBooking> getBookings(@RequestParam Long projectId) {
        return travelBookingRepository.findByProjectId(projectId);
    }

    @PostMapping("/bookings")
    public TravelBooking createBooking(@RequestParam Long projectId, @RequestBody TravelBooking booking) {
        booking.setProjectId(projectId);
        return travelBookingRepository.save(booking);
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<TravelBooking> updateBooking(@PathVariable Long id, @RequestParam Long projectId, @RequestBody TravelBooking updated) {
        Optional<TravelBooking> opt = travelBookingRepository.findById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        TravelBooking existing = opt.get();
        existing.setProjectId(projectId);
        if (updated.getCustomerName() != null) existing.setCustomerName(updated.getCustomerName());
        if (updated.getCustomerEmail() != null) existing.setCustomerEmail(updated.getCustomerEmail());
        if (updated.getCustomerPhone() != null) existing.setCustomerPhone(updated.getCustomerPhone());
        if (updated.getPackageId() != null) existing.setPackageId(updated.getPackageId());
        if (updated.getPackageName() != null) existing.setPackageName(updated.getPackageName());
        if (updated.getTravelDate() != null) existing.setTravelDate(updated.getTravelDate());
        if (updated.getGuestsCount() != null) existing.setGuestsCount(updated.getGuestsCount());
        if (updated.getTotalPrice() != null) existing.setTotalPrice(updated.getTotalPrice());
        if (updated.getPaymentStatus() != null) existing.setPaymentStatus(updated.getPaymentStatus());
        if (updated.getBookingStatus() != null) existing.setBookingStatus(updated.getBookingStatus());
        if (updated.getBookingSettings() != null) existing.setBookingSettings(updated.getBookingSettings());
        if (updated.getPdfUrl() != null) existing.setPdfUrl(updated.getPdfUrl());
        return ResponseEntity.ok(travelBookingRepository.save(existing));
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id, @RequestParam Long projectId) {
        if (travelBookingRepository.existsById(id)) {
            travelBookingRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // 4. Travel Destinations Mappings
    // ==========================================

    @GetMapping("/destinations")
    public List<TravelDestination> getDestinations(@RequestParam Long projectId) {
        return travelDestinationRepository.findByProjectId(projectId);
    }

    @PostMapping("/destinations")
    public TravelDestination createDestination(@RequestParam Long projectId, @RequestBody TravelDestination destination) {
        destination.setProjectId(projectId);
        return travelDestinationRepository.save(destination);
    }

    @PutMapping("/destinations/{id}")
    public ResponseEntity<TravelDestination> updateDestination(@PathVariable Long id, @RequestParam Long projectId, @RequestBody TravelDestination updated) {
        Optional<TravelDestination> opt = travelDestinationRepository.findById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        TravelDestination existing = opt.get();
        existing.setProjectId(projectId);
        if (updated.getCountry() != null) existing.setCountry(updated.getCountry());
        if (updated.getState() != null) existing.setState(updated.getState());
        if (updated.getCity() != null) existing.setCity(updated.getCity());
        if (updated.getTouristPlaces() != null) existing.setTouristPlaces(updated.getTouristPlaces());
        if (updated.getImageUrl() != null) existing.setImageUrl(updated.getImageUrl());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        return ResponseEntity.ok(travelDestinationRepository.save(existing));
    }

    @DeleteMapping("/destinations/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id, @RequestParam Long projectId) {
        if (travelDestinationRepository.existsById(id)) {
            travelDestinationRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // 5. Travel Hotels Mappings
    // ==========================================

    @GetMapping("/hotels")
    public List<TravelHotel> getHotels(@RequestParam Long projectId) {
        return travelHotelRepository.findByProjectId(projectId);
    }

    @PostMapping("/hotels")
    public TravelHotel createHotel(@RequestParam Long projectId, @RequestBody TravelHotel hotel) {
        hotel.setProjectId(projectId);
        return travelHotelRepository.save(hotel);
    }

    @PutMapping("/hotels/{id}")
    public ResponseEntity<TravelHotel> updateHotel(@PathVariable Long id, @RequestParam Long projectId, @RequestBody TravelHotel updated) {
        Optional<TravelHotel> opt = travelHotelRepository.findById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        TravelHotel existing = opt.get();
        existing.setProjectId(projectId);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getCity() != null) existing.setCity(updated.getCity());
        if (updated.getRooms() != null) existing.setRooms(updated.getRooms());
        if (updated.getPricing() != null) existing.setPricing(updated.getPricing());
        if (updated.getAvailability() != null) existing.setAvailability(updated.getAvailability());
        if (updated.getImagesJson() != null) existing.setImagesJson(updated.getImagesJson());
        return ResponseEntity.ok(travelHotelRepository.save(existing));
    }

    @DeleteMapping("/hotels/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id, @RequestParam Long projectId) {
        if (travelHotelRepository.existsById(id)) {
            travelHotelRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // 6. Travel Flights Mappings
    // ==========================================

    @GetMapping("/flights")
    public List<TravelFlight> getFlights(@RequestParam Long projectId) {
        return travelFlightRepository.findByProjectId(projectId);
    }

    @PostMapping("/flights")
    public TravelFlight createFlight(@RequestParam Long projectId, @RequestBody TravelFlight flight) {
        flight.setProjectId(projectId);
        return travelFlightRepository.save(flight);
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<TravelFlight> updateFlight(@PathVariable Long id, @RequestParam Long projectId, @RequestBody TravelFlight updated) {
        Optional<TravelFlight> opt = travelFlightRepository.findById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        TravelFlight existing = opt.get();
        existing.setProjectId(projectId);
        if (updated.getAirline() != null) existing.setAirline(updated.getAirline());
        if (updated.getSchedule() != null) existing.setSchedule(updated.getSchedule());
        if (updated.getAvailability() != null) existing.setAvailability(updated.getAvailability());
        if (updated.getPricing() != null) existing.setPricing(updated.getPricing());
        return ResponseEntity.ok(travelFlightRepository.save(existing));
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id, @RequestParam Long projectId) {
        if (travelFlightRepository.existsById(id)) {
            travelFlightRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // 7. Travel Visas Mappings
    // ==========================================

    @GetMapping("/visas")
    public List<TravelVisa> getVisas(@RequestParam Long projectId) {
        return travelVisaRepository.findByProjectId(projectId);
    }

    @PostMapping("/visas")
    public TravelVisa createVisa(@RequestParam Long projectId, @RequestBody TravelVisa visa) {
        visa.setProjectId(projectId);
        return travelVisaRepository.save(visa);
    }

    @PutMapping("/visas/{id}")
    public ResponseEntity<TravelVisa> updateVisa(@PathVariable Long id, @RequestParam Long projectId, @RequestBody TravelVisa updated) {
        Optional<TravelVisa> opt = travelVisaRepository.findById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        TravelVisa existing = opt.get();
        existing.setProjectId(projectId);
        if (updated.getVisaType() != null) existing.setVisaType(updated.getVisaType());
        if (updated.getCountry() != null) existing.setCountry(updated.getCountry());
        if (updated.getDocumentsRequired() != null) existing.setDocumentsRequired(updated.getDocumentsRequired());
        if (updated.getFees() != null) existing.setFees(updated.getFees());
        if (updated.getApprovalStatus() != null) existing.setApprovalStatus(updated.getApprovalStatus());
        return ResponseEntity.ok(travelVisaRepository.save(existing));
    }

    @DeleteMapping("/visas/{id}")
    public ResponseEntity<Void> deleteVisa(@PathVariable Long id, @RequestParam Long projectId) {
        if (travelVisaRepository.existsById(id)) {
            travelVisaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // 8. Travel Theme Settings Mappings
    // ==========================================

    @GetMapping("/theme-settings")
    public ResponseEntity<TravelThemeSettings> getThemeSettings(@RequestParam Long projectId) {
        Optional<TravelThemeSettings> opt = travelThemeSettingsRepository.findByProjectId(projectId);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        TravelThemeSettings defaultSettings = new TravelThemeSettings();
        defaultSettings.setProjectId(projectId);
        defaultSettings.setThemeColor("Blue");
        return ResponseEntity.ok(defaultSettings);
    }

    @PostMapping("/theme-settings")
    public ResponseEntity<TravelThemeSettings> createThemeSettings(@RequestParam Long projectId, @RequestBody TravelThemeSettings settings) {
        settings.setProjectId(projectId);
        Optional<TravelThemeSettings> opt = travelThemeSettingsRepository.findByProjectId(projectId);
        if (opt.isPresent()) {
            TravelThemeSettings existing = opt.get();
            updateThemeSettingsFields(existing, settings);
            return ResponseEntity.ok(travelThemeSettingsRepository.save(existing));
        }
        return ResponseEntity.ok(travelThemeSettingsRepository.save(settings));
    }

    @PutMapping("/theme-settings")
    public ResponseEntity<TravelThemeSettings> updateThemeSettings(@RequestParam Long projectId, @RequestBody TravelThemeSettings settings) {
        settings.setProjectId(projectId);
        Optional<TravelThemeSettings> opt = travelThemeSettingsRepository.findByProjectId(projectId);
        TravelThemeSettings toSave;
        if (opt.isPresent()) {
            toSave = opt.get();
            updateThemeSettingsFields(toSave, settings);
        } else {
            toSave = settings;
        }
        return ResponseEntity.ok(travelThemeSettingsRepository.save(toSave));
    }

    private void updateThemeSettingsFields(TravelThemeSettings dest, TravelThemeSettings src) {
        if (src.getThemeColor() != null) dest.setThemeColor(src.getThemeColor());
        if (src.getCustomColorHex() != null) dest.setCustomColorHex(src.getCustomColorHex());
        if (src.getLogoUrl() != null) dest.setLogoUrl(src.getLogoUrl());
        if (src.getFaviconUrl() != null) dest.setFaviconUrl(src.getFaviconUrl());
        if (src.getBannerUrl() != null) dest.setBannerUrl(src.getBannerUrl());
        if (src.getHeroVideoUrl() != null) dest.setHeroVideoUrl(src.getHeroVideoUrl());
        if (src.getCompanyImages() != null) dest.setCompanyImages(src.getCompanyImages());
        if (src.getGalleryImages() != null) dest.setGalleryImages(src.getGalleryImages());
        if (src.getSectionsLayoutJson() != null) dest.setSectionsLayoutJson(src.getSectionsLayoutJson());
        if (src.getDomainType() != null) dest.setDomainType(src.getDomainType());
        if (src.getSubdomainName() != null) dest.setSubdomainName(src.getSubdomainName());
        if (src.getCustomDomainName() != null) dest.setCustomDomainName(src.getCustomDomainName());
        if (src.getBookingSettingsJson() != null) dest.setBookingSettingsJson(src.getBookingSettingsJson());
    }
}
