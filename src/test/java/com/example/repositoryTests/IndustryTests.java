package com.example.repositoryTests;

import com.example.entity.Industry;
import com.example.repository.IndustryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
public class IndustryTests {
    @Autowired
    private IndustryRepository industryRepository;
    @Test
    void deleteIndustry () {
//        Industry industry =
        industryRepository.deleteById(1);
//        Assertions.assertNotNull(newIndustry);
    }
}
