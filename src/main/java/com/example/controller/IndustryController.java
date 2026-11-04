package com.example.controller;

import com.example.dto.IndustryRequest;
import com.example.dto.IndustryResponse;
import com.example.dto.PageResponse;
import com.example.entity.Industry;
import com.example.service.IndustryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/industries")
public class IndustryController {


}
