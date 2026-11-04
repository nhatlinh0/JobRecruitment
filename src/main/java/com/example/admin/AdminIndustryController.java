package com.example.admin;

import com.example.dto.IndustryRequest;
import com.example.service.IndustryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/industries")
public class AdminIndustryController {
    @Autowired
    private IndustryService industryService;

    @PostMapping
    public ResponseEntity<?> createIndustry(@Valid @RequestBody IndustryRequest industryRequest){
        industryService.saveIndustry(industryRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateIndustry(@Valid @RequestBody IndustryRequest industryRequest, @PathVariable Integer id){
        industryService.updateIndustry(industryRequest ,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIndustry(@PathVariable Integer id) {
        industryService.deleteIndustry(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
