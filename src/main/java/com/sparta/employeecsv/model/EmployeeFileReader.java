package com.sparta.employeecsv.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sparta.employeecsv.CSVMain.logger;

/**
 * A class which is in charge of reading the CSV file
 */
public class EmployeeFileReader {

    private ArrayList<Employee> employeesList;
    private HashMap<String, Employee> employeesMap;
    private ArrayList<Employee> duplicates;

    /**
     * Reads the CSV file via lambda expressions and streams
     * @param fileName - String value of the file name
     */
    public void readFileLambda(String fileName) {

        EmployeeParser employeeParser = new EmployeeParser();

        try {    //read data from the file
            List<Employee> original = Files.lines(Path.of(fileName))
                    .map(s -> {
                        String[] data = s.split(",");
                        return employeeParser.parseEmployee(data[0], data[1], data[2],
                                data[3], data[4], data[5], data[6],
                                data[7], data[8], data[9]);
                    })
                    .toList();

            List<Employee> distinctList = original.stream()    // Gets the employee list
                    .distinct().toList();

            List<Employee> duplicatesList = original.stream()    // Gets the duplicates list
                     .collect(Collectors.groupingBy(Function.identity()
                             , Collectors.counting()))
                    .entrySet().stream()
                    .filter((m -> m.getValue() > 1))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            employeesList = new ArrayList<>(distinctList);
            duplicates = new ArrayList<>(duplicatesList);

            logger.info("File provided has been read");

        } catch (IOException e) {
            logger.warn("Error reading the file", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * Reads the CSV file via buffed file reader
     * @param fileName - String value of the file name
     */
    public void readFileMap(String fileName) {

        employeesMap = new HashMap<>();
        duplicates = new ArrayList<>();

        EmployeeParser employeeParser = new EmployeeParser();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {    //read data from the file
            //list to collect Employee objects
            employeesMap = new HashMap<String,Employee>();
            String line = "";
            while ((line = br.readLine()) != null) {    //read the file line by line
                String[] data = line.split(",");    //extract individual fields from each line
                //create new Employee object
                Employee employee = employeeParser.parseEmployee(data[0], data[1], data[2],
                        data[3], data[4], data[5], data[6],
                        data[7], data[8], data[9]);
                if (employeesMap.containsKey(data[0])) {
                    duplicates.add(employee);
                    employeesMap.remove(data[0]);
                } else {
                    employeesMap.put(data[0], employee);    //add the object to the List
                }
            }
            logger.info("File provided has been read");
        } catch (IOException e) {
            logger.error("Error reading the file", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * @return the list of employees that were read from the CSV file
     */
    public ArrayList<Employee> getEmployeesList() {
        return employeesList;
    }

    /**
     * @return the list of duplicated employees that were read from the CSV file
     */
    public ArrayList<Employee> getDuplicates() {
        return duplicates;
    }

    /**
     * @return a converted list from a hash map
     */
    public ArrayList<Employee> getEmployeeMapAsList() {
        ArrayList<Employee> employeesList = new ArrayList<Employee>(employeesMap.size());

        for (Employee employee : employeesMap.values()) {
            employeesList.add(employee);
        }
        return employeesList;
    }
}
