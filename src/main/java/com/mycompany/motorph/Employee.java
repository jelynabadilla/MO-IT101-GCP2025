package com.mycompany.motorph;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class Employee {
    private String employeeId;
    private String lastName;
    private String firstName;
    private LocalDate birthday;
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philhealthNumber;
    private String tinNumber;
    private String pagibigNumber;
    private String status;
    private String position;
    private String supervisor;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossRate;
    private double hourlyRate;
    
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DMY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter MDY_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public Employee() {}

    public Employee(Map<String, String> data) {
        this.employeeId = data.get("EmployeeID");
        this.lastName = data.get("LastName");
        this.firstName = data.get("FirstName");
        this.birthday = parseDate(data.get("Birthday"));
        
        // Remove any surrounding quotes from the address field
        String addressValue = data.get("Address");
        if (addressValue != null && addressValue.startsWith("\"") && addressValue.endsWith("\"")) {
            addressValue = addressValue.substring(1, addressValue.length() - 1);
        }
        this.address = addressValue;
        
        this.phoneNumber = data.get("PhoneNumber");
        this.sssNumber = data.get("SSS");
        this.philhealthNumber = data.get("Philhealth");
        this.tinNumber = data.get("TIN");
        this.pagibigNumber = data.get("Pagibig");
        this.status = data.get("Status");
        
        // Remove any surrounding quotes from the position field
        String positionValue = data.get("Position");
        if (positionValue != null && positionValue.startsWith("\"") && positionValue.endsWith("\"")) {
            positionValue = positionValue.substring(1, positionValue.length() - 1);
        }
        this.position = positionValue;
        
        // Remove any surrounding quotes from the supervisor field
        String supervisorValue = data.get("Supervisor");
        if (supervisorValue != null && supervisorValue.startsWith("\"") && supervisorValue.endsWith("\"")) {
            supervisorValue = supervisorValue.substring(1, supervisorValue.length() - 1);
        }
        this.supervisor = supervisorValue;
        
        this.basicSalary = parseFormattedDouble(data.get("BasicSalary"));
        this.riceSubsidy = parseFormattedDouble(data.get("RiceSubsidy"));
        this.phoneAllowance = parseFormattedDouble(data.get("PhoneAllowance"));
        this.clothingAllowance = parseFormattedDouble(data.get("ClothingAllowance"));
        this.grossRate = parseFormattedDouble(data.get("GrossRate"));
        this.hourlyRate = parseFormattedDouble(data.get("HourlyRate"));
    }
    
    private double parseFormattedDouble(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        String cleanValue = value.replaceAll("[^\\d.]", "");
        return Double.parseDouble(cleanValue);
    }
    
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        
        try {
            return LocalDate.parse(dateStr, MDY_FORMATTER);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(dateStr, ISO_FORMATTER);
            } catch (DateTimeParseException e2) {
                try {
                    return LocalDate.parse(dateStr, DMY_FORMATTER);
                } catch (DateTimeParseException e3) {
                    throw new IllegalArgumentException("Unable to parse date: " + dateStr + 
                                                      ". Expected formats: MM/dd/yyyy, YYYY-MM-DD, or DD/MM/YYYY");
                }
            }
        }
    }

    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public LocalDate getBirthday() { return birthday; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilhealthNumber() { return philhealthNumber; }
    public String getTinNumber() { return tinNumber; }
    public String getPagibigNumber() { return pagibigNumber; }
    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getSupervisor() { return supervisor; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getGrossRate() { return grossRate; }
    public double getHourlyRate() { return hourlyRate; }

    // Setters
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }
    public void setBirthday(String birthdayStr) { this.birthday = parseDate(birthdayStr); }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setSssNumber(String sssNumber) { this.sssNumber = sssNumber; }
    public void setPhilhealthNumber(String philhealthNumber) { this.philhealthNumber = philhealthNumber; }
    public void setTinNumber(String tinNumber) { this.tinNumber = tinNumber; }
    public void setPagibigNumber(String pagibigNumber) { this.pagibigNumber = pagibigNumber; }
    public void setStatus(String status) { this.status = status; }
    public void setPosition(String position) { this.position = position; }
    public void setSupervisor(String supervisor) { this.supervisor = supervisor; }
    public void setBasicSalary(double basicSalary) { 
        this.basicSalary = basicSalary;
        this.hourlyRate = basicSalary / (22 * 8);
    }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllowance = phoneAllowance; }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; }
    public void setGrossRate(double grossRate) { this.grossRate = grossRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("EmployeeID", employeeId);
        map.put("LastName", lastName);
        map.put("FirstName", firstName);
        map.put("Birthday", birthday != null ? birthday.format(MDY_FORMATTER) : "");
        map.put("Address", address); // No quotes added
        map.put("PhoneNumber", phoneNumber);
        map.put("SSS", sssNumber);
        map.put("Philhealth", philhealthNumber);
        map.put("TIN", tinNumber);
        map.put("Pagibig", pagibigNumber);
        map.put("Status", status);
        map.put("Position", position); // No quotes added
        map.put("Supervisor", supervisor); // No quotes added
        map.put("BasicSalary", String.format("%.2f", basicSalary));
        map.put("RiceSubsidy", String.format("%.2f", riceSubsidy));
        map.put("PhoneAllowance", String.format("%.2f", phoneAllowance));
        map.put("ClothingAllowance", String.format("%.2f", clothingAllowance));
        map.put("GrossRate", String.format("%.2f", grossRate));
        map.put("HourlyRate", String.format("%.2f", hourlyRate));
        return map;
    }
    
    public String toCSV() {
        String formattedBirthday = birthday != null ? birthday.format(MDY_FORMATTER) : "";
        
        // Only quote fields that contain commas, and escape existing quotes by doubling them
        String addressField = address != null ? address.replace("\"", "\"\"") : "";
        if (addressField.contains(",")) {
            addressField = "\"" + addressField + "\"";
        }
        
        String positionField = position != null ? position.replace("\"", "\"\"") : "";
        if (positionField.contains(",")) {
            positionField = "\"" + positionField + "\"";
        }
        
        String supervisorField = supervisor != null ? supervisor.replace("\"", "\"\"") : "";
        if (supervisorField.contains(",")) {
            supervisorField = "\"" + supervisorField + "\"";
        }
        
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f",
            employeeId, lastName, firstName, formattedBirthday, 
            addressField, phoneNumber, sssNumber,
            philhealthNumber, tinNumber, pagibigNumber,
            status, positionField, supervisorField,
            basicSalary, riceSubsidy, phoneAllowance,
            clothingAllowance, grossRate, hourlyRate);
    }
}