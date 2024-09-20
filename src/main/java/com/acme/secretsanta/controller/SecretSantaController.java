package com.acme.secretsanta.controller;

import com.acme.secretsanta.model.Employee;
import com.acme.secretsanta.service.SecretSantaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("api/santa")
@CrossOrigin(origins = "http://localhost:3000")
public class SecretSantaController {

    @Autowired
    private SecretSantaService secretSantaService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Log the file content
            String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            System.out.println("Uploaded File Content: \n" + fileContent);

            // Save the uploaded file to a temporary location for processing
            java.io.File tempFile = java.io.File.createTempFile("employee-upload-", ".csv");
            file.transferTo(tempFile);
            String tempFilePath = tempFile.getAbsolutePath();

            // Process the uploaded CSV file
            List<Employee> employees = secretSantaService.loadEmployees(tempFilePath);
            List<Employee> assignments = secretSantaService.assignSecretSanta(employees);

            // Save the result to a dynamically generated output file
            java.io.File outputFile = java.io.File.createTempFile("secret-santa-output-", ".csv");
            secretSantaService.writeAssignmentsToCSV(outputFile.getAbsolutePath(), employees, assignments);

            return "Assignments completed. Download the result from: " + outputFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to process the file";
        }
        }
    }

