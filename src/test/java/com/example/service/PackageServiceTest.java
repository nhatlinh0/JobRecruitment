package com.example.service;

import com.example.dto.PackageRequest;
import com.example.dto.PackageResponse;
import com.example.entity.Package;
import com.example.repository.PackageRepository;
import com.example.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PackageServiceTest {
    private final PackageRepository packages = mock(PackageRepository.class);
    private final PaymentRepository payments = mock(PaymentRepository.class);
    private final PackageService service = new PackageService(packages, payments);

    @Test
    void createsAndUpdatesPackage() {
        PackageRequest request = request("100000.00");
        when(packages.save(any(Package.class))).thenAnswer(invocation -> invocation.getArgument(0));
        PackageResponse created = service.create(request);
        assertEquals("Basic", created.getName());
        assertEquals(new BigDecimal("100000.00"), created.getPrice());
        assertEquals(10, created.getCredits());

        Package existing = new Package();
        existing.setId(5);
        when(packages.findById(5)).thenReturn(Optional.of(existing));
        PackageResponse updated = service.update(5, request);
        assertEquals(5, updated.getId());
        assertEquals("Description", existing.getDescription());
    }

    @Test
    void rejectsFractionalPricesAndMissingPackages() {
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.create(request("1.50"))).getStatusCode().value());
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.getById(5)).getStatusCode().value());
        verify(packages, never()).save(any());
    }

    @Test
    void protectsPaymentHistoryWhenDeleting() {
        Package item = new Package();
        when(packages.findById(5)).thenReturn(Optional.of(item));
        when(payments.existsByPaymentPackageId(5)).thenReturn(true);
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.delete(5)).getStatusCode().value());
        verify(packages, never()).delete(any());

        when(payments.existsByPaymentPackageId(5)).thenReturn(false);
        doThrow(new DataIntegrityViolationException("foreign key")).when(packages).flush();
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.delete(5)).getStatusCode().value());
    }

    @Test
    void deletesUnusedPackage() {
        Package item = new Package();
        when(packages.findById(5)).thenReturn(Optional.of(item));
        service.delete(5);
        verify(packages).delete(item);
        verify(packages).flush();
    }

    private PackageRequest request(String price) {
        PackageRequest request = new PackageRequest();
        request.setName("Basic");
        request.setPrice(new BigDecimal(price));
        request.setCredits(10);
        request.setDescription("Description");
        return request;
    }
}
