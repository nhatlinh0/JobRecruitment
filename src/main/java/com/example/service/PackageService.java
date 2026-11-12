package com.example.service;

import com.example.dto.PackageRequest;
import com.example.dto.PackageResponse;
import com.example.dto.PageResponse;
import com.example.entity.Package;
import com.example.repository.PackageRepository;
import com.example.repository.PaymentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PackageService {
    private final PackageRepository packages;
    private final PaymentRepository payments;

    public PackageService(PackageRepository packages, PaymentRepository payments) {
        this.packages = packages;
        this.payments = payments;
    }

    @Transactional(readOnly = true)
    public PageResponse<PackageResponse> getAll(Pageable pageable) {
        return PageResponse.from(packages.findAll(pageable).map(PackageResponse::new));
    }

    @Transactional(readOnly = true)
    public PackageResponse getById(Integer id) {
        return new PackageResponse(findById(id));
    }

    @Transactional
    public PackageResponse create(PackageRequest request) {
        Package item = new Package();
        applyRequest(item, request);
        return new PackageResponse(packages.save(item));
    }

    @Transactional
    public PackageResponse update(Integer id, PackageRequest request) {
        Package item = findById(id);
        applyRequest(item, request);
        return new PackageResponse(packages.save(item));
    }

    @Transactional
    public void delete(Integer id) {
        Package item = findById(id);
        if (payments.existsByPaymentPackageId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Không thể xóa package đã có giao dịch");
        }
        try {
            packages.delete(item);
            packages.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Không thể xóa package đang được sử dụng", ex);
        }
    }

    private Package findById(Integer id) {
        return packages.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy package"));
    }

    private void applyRequest(Package item, PackageRequest request) {
        if (request.getPrice().stripTrailingZeros().scale() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price phải là số nguyên VND");
        }
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setCredits(request.getCredits());
        item.setDescription(request.getDescription());
    }
}
