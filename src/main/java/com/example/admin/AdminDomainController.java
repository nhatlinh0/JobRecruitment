package com.example.admin;

import com.example.dto.DomainRequest;
import com.example.service.DomainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/domains")
public class AdminDomainController {
    @Autowired
    public DomainService domainService;

    @PostMapping()
    public ResponseEntity<?> createDomain(@Valid @RequestBody DomainRequest domainRequest) {
        domainService.saveDomain(domainRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDomain(@Valid @RequestBody DomainRequest domainRequest, @PathVariable Integer id) {
        domainService.updateDomain(domainRequest, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDomain(@PathVariable int id) {
        try {
            domainService.deleteDomain(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
