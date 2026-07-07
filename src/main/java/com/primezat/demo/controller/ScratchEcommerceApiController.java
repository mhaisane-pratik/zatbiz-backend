package com.primezat.demo.controller;

import com.primezat.demo.model.ScratchEcommerce;
import com.primezat.demo.repository.ScratchEcommerceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/scratch-ecommerce")
@CrossOrigin(origins = "*")
public class ScratchEcommerceApiController {

    @Autowired
    private ScratchEcommerceRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<ScratchEcommerce> getStoreInfo(@RequestParam Long projectId) {
        Optional<ScratchEcommerce> opt = repository.findByProjectId(projectId);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        ScratchEcommerce defaultStore = new ScratchEcommerce();
        defaultStore.setProjectId(projectId);
        defaultStore.setStoreName("My Store");
        defaultStore.setStatus("Draft");
        return ResponseEntity.ok(defaultStore);
    }

    @PostMapping
    public ResponseEntity<ScratchEcommerce> createStoreInfo(@RequestParam Long projectId, @RequestBody ScratchEcommerce scratchStore) {
        scratchStore.setProjectId(projectId);
        Optional<ScratchEcommerce> opt = repository.findByProjectId(projectId);
        if (opt.isPresent()) {
            ScratchEcommerce existing = opt.get();
            updateFields(existing, scratchStore);
            existing.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(repository.save(existing));
        }
        scratchStore.setCreatedAt(LocalDateTime.now());
        scratchStore.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(repository.save(scratchStore));
    }

    @PutMapping
    public ResponseEntity<ScratchEcommerce> updateStoreInfo(@RequestParam Long projectId, @RequestBody ScratchEcommerce scratchStore) {
        scratchStore.setProjectId(projectId);
        Optional<ScratchEcommerce> opt = repository.findByProjectId(projectId);
        ScratchEcommerce toSave;
        if (opt.isPresent()) {
            toSave = opt.get();
            updateFields(toSave, scratchStore);
            toSave.setUpdatedAt(LocalDateTime.now());
        } else {
            toSave = scratchStore;
            toSave.setCreatedAt(LocalDateTime.now());
            toSave.setUpdatedAt(LocalDateTime.now());
        }
        return ResponseEntity.ok(repository.save(toSave));
    }

    @PostMapping("/publish")
    public ResponseEntity<ScratchEcommerce> publishStore(@RequestParam Long projectId, @RequestParam(required = false) String subdomain) {
        Optional<ScratchEcommerce> opt = repository.findByProjectId(projectId);
        ScratchEcommerce store;
        if (opt.isPresent()) {
            store = opt.get();
        } else {
            store = new ScratchEcommerce();
            store.setProjectId(projectId);
        }
        store.setStatus("Published");
        if (subdomain != null && !subdomain.trim().isEmpty()) {
            store.setSubdomain(subdomain.trim());
        } else if (store.getStoreName() != null) {
            String sanitized = store.getStoreName().toLowerCase().replaceAll("[^a-z0-9]", "");
            if (sanitized.isEmpty()) sanitized = "store" + projectId;
            store.setSubdomain(sanitized + ".zatbiz.site");
        } else {
            store.setSubdomain("store" + projectId + ".zatbiz.site");
        }
        store.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(repository.save(store));
    }

    // --- PRODUCTS ENDPOINTS ---
    @GetMapping("/products")
    public ResponseEntity<List<Map<String, Object>>> listProducts(@RequestParam Long projectId) {
        String sql = "SELECT * FROM scratch_product WHERE project_id = ? ORDER BY id DESC";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, projectId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Map<String, Object> body) {
        String sql = "INSERT INTO scratch_product (project_id, name, description, price, image_url, variants, stock, available, brand, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                body.get("projectId"),
                body.get("name"),
                body.get("description"),
                body.get("price"),
                body.get("imageUrl"),
                body.get("variants"),
                body.get("stock"),
                body.get("available") != null ? body.get("available") : true,
                body.get("brand"),
                body.get("color")
        );
        body.put("id", id);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String sql = "UPDATE scratch_product SET name = ?, description = ?, price = ?, image_url = ?, variants = ?, stock = ?, available = ?, brand = ?, color = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                body.get("name"),
                body.get("description"),
                body.get("price"),
                body.get("imageUrl"),
                body.get("variants"),
                body.get("stock"),
                body.get("available") != null ? body.get("available") : true,
                body.get("brand"),
                body.get("color"),
                id
        );
        body.put("id", id);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        String sql = "DELETE FROM scratch_product WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // --- CATEGORIES ENDPOINTS ---
    @GetMapping("/categories")
    public ResponseEntity<List<Map<String, Object>>> listCategories(@RequestParam Long projectId) {
        String sql = "SELECT * FROM scratch_category WHERE project_id = ? ORDER BY id DESC";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, projectId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/categories")
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody Map<String, Object> body) {
        String sql = "INSERT INTO scratch_category (project_id, name, description, image_url) VALUES (?, ?, ?, ?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                body.get("projectId"),
                body.get("name"),
                body.get("description"),
                body.get("imageUrl")
        );
        body.put("id", id);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
        String sql = "DELETE FROM scratch_category WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // --- ORDERS ENDPOINTS ---
    @GetMapping("/orders")
    public ResponseEntity<List<Map<String, Object>>> listOrders(@RequestParam Long projectId) {
        String sql = "SELECT * FROM scratch_order WHERE project_id = ? ORDER BY id DESC";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, projectId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String sql = "UPDATE scratch_order SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, body.get("status"), id);
        return ResponseEntity.ok(Map.of("id", id, "status", body.get("status")));
    }

    // --- CUSTOMERS ENDPOINTS ---
    @GetMapping("/customers")
    public ResponseEntity<List<Map<String, Object>>> listCustomers(@RequestParam Long projectId) {
        String sql = "SELECT * FROM scratch_customer WHERE project_id = ? ORDER BY id DESC";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, projectId);
        return ResponseEntity.ok(list);
    }

    private void updateFields(ScratchEcommerce dest, ScratchEcommerce src) {
        if (src.getStoreName() != null) dest.setStoreName(src.getStoreName());
        if (src.getBusinessName() != null) dest.setBusinessName(src.getBusinessName());
        if (src.getOwnerName() != null) dest.setOwnerName(src.getOwnerName());
        if (src.getEmail() != null) dest.setEmail(src.getEmail());
        if (src.getPhone() != null) dest.setPhone(src.getPhone());
        if (src.getLogoUrl() != null) dest.setLogoUrl(src.getLogoUrl());
        if (src.getFaviconUrl() != null) dest.setFaviconUrl(src.getFaviconUrl());
        if (src.getPrimaryColor() != null) dest.setPrimaryColor(src.getPrimaryColor());
        if (src.getSecondaryColor() != null) dest.setSecondaryColor(src.getSecondaryColor());
        if (src.getFont() != null) dest.setFont(src.getFont());
        if (src.getButtonStyle() != null) dest.setButtonStyle(src.getButtonStyle());
        if (src.getCurrency() != null) dest.setCurrency(src.getCurrency());
        if (src.getCountry() != null) dest.setCountry(src.getCountry());
        if (src.getLanguage() != null) dest.setLanguage(src.getLanguage());
        if (src.getTimeZone() != null) dest.setTimeZone(src.getTimeZone());
        if (src.getTaxRate() != null) dest.setTaxRate(src.getTaxRate());
        
        if (src.getPaymentStripe() != null) dest.setPaymentStripe(src.getPaymentStripe());
        if (src.getPaymentRazorpay() != null) dest.setPaymentRazorpay(src.getPaymentRazorpay());
        if (src.getPaymentPaypal() != null) dest.setPaymentPaypal(src.getPaymentPaypal());
        if (src.getPaymentCod() != null) dest.setPaymentCod(src.getPaymentCod());
        
        if (src.getShippingFlatRate() != null) dest.setShippingFlatRate(src.getShippingFlatRate());
        if (src.getShippingFlatRateFee() != null) dest.setShippingFlatRateFee(src.getShippingFlatRateFee());
        if (src.getShippingFree() != null) dest.setShippingFree(src.getShippingFree());
        if (src.getShippingPickup() != null) dest.setShippingPickup(src.getShippingPickup());
        
        if (src.getStatus() != null) dest.setStatus(src.getStatus());
        if (src.getWizardCompleted() != null) dest.setWizardCompleted(src.getWizardCompleted());
        if (src.getBuilderJson() != null) dest.setBuilderJson(src.getBuilderJson());
        if (src.getThemeJson() != null) dest.setThemeJson(src.getThemeJson());
        if (src.getSubdomain() != null) dest.setSubdomain(src.getSubdomain());
    }
}
