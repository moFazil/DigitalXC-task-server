package com.acme.secretsanta.utils;

import com.acme.secretsanta.model.Employee;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVUtils {
    public static List<Employee> parseEmployeeCSV(String csvFilePath) {
        List<Employee> employees = new ArrayList<>();
        try (Reader reader = new FileReader(csvFilePath)) {
            CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            Map<String, Integer> headerMap = parser.getHeaderMap();

            // Check for required headers
            if (!headerMap.containsKey("Employee_Name") || !headerMap.containsKey("Employee_Email")) {
                throw new IllegalArgumentException("CSV file must contain headers: Employee_Name and Employee_Email");
            }

            for (CSVRecord record : parser) {
                String name = record.get("Employee_Name");
                String email = record.get("Employee_Email");
                employees.add(new Employee(name, email));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static void writeCSV(String outputFile,List<Employee> employees,List<Employee> assignments){
        try(Writer writer = new FileWriter(outputFile);
            CSVPrinter csvPrinter = new CSVPrinter(writer,CSVFormat.DEFAULT.withHeader("Employee_Name","Employee_EmailId","Secret_Child_Name","Secret_Child_EmailId"))){

            for (int i=0;i<employees.size();i++){
                Employee employee = employees.get(i);
                Employee assignment = assignments.get(i);
                csvPrinter.printRecord(employee.getEmployeeName(),employee.getEmployeeEmail(),assignment.getEmployeeName(),assignment.getEmployeeEmail());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
