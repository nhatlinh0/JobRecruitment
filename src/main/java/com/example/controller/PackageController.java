package com.example.controller;

import com.example.dto.PackageRequest;
import com.example.dto.PackageResponse;
import com.example.dto.PageResponse;
import com.example.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {
    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping
    public PageResponse<PackageResponse> getAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return packageService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public PackageResponse getById(@PathVariable Integer id) {
        return packageService.getById(id);
    }

    @PostMapping
    public ResponseEntity<PackageResponse> create(@Valid @RequestBody PackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packageService.create(request));
    }

    @PutMapping("/{id}")
    public PackageResponse update(@PathVariable Integer id, @Valid @RequestBody PackageRequest request) {
        return packageService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        packageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
