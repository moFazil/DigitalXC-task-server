package com.acme.secretsanta.service;

import com.acme.secretsanta.model.Employee;
import com.acme.secretsanta.utils.CSVUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SecretSantaService {
    public List<Employee> assignSecretSanta(List<Employee> employees){
        List<Employee> shuffledEmployee = new ArrayList<>(employees);
        Collections.shuffle(shuffledEmployee);

        List<Employee> secretSantaAssignments = new ArrayList<>();

        for(int i=0;i<employees.size();i++){
            Employee employee = employees.get(i);
            Employee secretChild = shuffledEmployee.get((i+1)%shuffledEmployee.size());

            if(!employee.getEmployeeEmail().equals(secretChild.getEmployeeEmail())){
                secretSantaAssignments.add(secretChild);
            }else{
                Collections.shuffle(shuffledEmployee);
                i= -1;
            }
        }
        return secretSantaAssignments;
    }

    public List<Employee> loadEmployees(String csvFile){
        return CSVUtils.parseEmployeeCSV(csvFile);
    }
    public void writeAssignmentsToCSV(String outputFile,List<Employee> employees,List<Employee> assignments){
        CSVUtils.writeCSV(outputFile,employees,assignments);
    }
}
