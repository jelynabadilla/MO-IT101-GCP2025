package com.mycompany.motorph;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

public class MotorPH {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Employee> employees = initializeEmployees();

        // Prompt for attendance record file path
        System.out.print("Enter CSV file path for attendance record: ");
        String csvFilePath = scanner.nextLine().trim();

        if (csvFilePath.isEmpty()) {
            csvFilePath = "attendance.csv"; // Default file path
        }

        // Read attendance records
        Map<String, Set<String>> monthToWeeksMap = readEmployeeAttendance(csvFilePath, employees);
        System.out.println("Attendance records updated.");

        // Main menu
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. View Employee Details");
            System.out.println("2. View Payroll");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewEmployeeDetails(scanner, employees);
                    break;
                case 2:
                    viewPayroll(scanner, employees, monthToWeeksMap);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Initialize employee details
    private static List<Employee> initializeEmployees() {
        List<Employee> employees = new ArrayList<>();
        // Add all employees with their details
        employees.add(new Employee("10001", "Garcia", "Manuel III", "10/11/1983", "Regular", "Chief Executive Officer", 535.71, 90000, 1500, 2000, 1000));
        employees.add(new Employee("10002", "Lim", "Antonio", "06/19/1988","Regular", "Chief Operating Officer", 357.14, 60000, 1500, 2000, 1000));
        employees.add(new Employee("10003", "Aquino", "Bianca Sofia", "08/04/1989", "Regular", "Chief Finance Officer", 357.14, 60000, 1500, 2000, 1000));
        employees.add(new Employee("10004", "Reyes", "Isabella", "06/16/1994", "Regular", "Chief Marketing Officer", 357.14, 60000, 1500, 2000, 1000));
        employees.add(new Employee("10005", "Hernandez", "Eduard", "09/23/1989", "Regular", "IT Operations and Systems", 313.51, 52670, 1500, 1000, 1000));
        employees.add(new Employee("10006", "Villanueva", "Andrea Mae", "02/14/1988","Regular", "HR Manager", 313.51, 52670, 1500, 1000, 1000));
        employees.add(new Employee("10007", "San Jose", "Brad", "03/15/1996", "Regular", "HR Team Leader", 255.80, 42975, 1500, 800, 800));
        employees.add(new Employee("10008", "Romualdez", "Alice", "05/14/1992", "Regular", "HR Rank and File", 133.93, 22500, 1500, 500, 500));
        employees.add(new Employee("10009", "Atienza", "Rosie", "09/24/1948", "Regular", "HR Rank and File", 133.93, 22500, 1500, 500, 500));
        employees.add(new Employee("10010", "Alvaro", "Roderick", "03/30/1988", "Regular", "Accounting Head", 313.51, 52670, 1500, 1000, 1000));
        employees.add(new Employee("10011", "Salcedo", "Anthony", "09/14/1993", "Regular", "Payroll Manager", 302.53, 50825, 1500, 1000, 1000));
        employees.add(new Employee("10012", "Lopez", "Josie", "01/14/1987", "Regular", "Payroll Team Leader", 229.02, 38475, 1500, 800, 800));
        employees.add(new Employee("10013", "Farala", "Martha", "01/11/1942", "Regular", "Payroll Rank and File", 142.86, 24000, 1500, 500, 500));
        employees.add(new Employee("10014", "Martinez", "Leila", "07/11/1970", "Regular", "Payroll Rank and File", 142.86, 24000, 1500, 500, 500));
        employees.add(new Employee("10015", "Romualdez", "Fredrick", "03/10/1985", "Regular", "Account Manager",  318.45, 53500, 1500, 1000, 1000));
        employees.add(new Employee("10016", "Mata", "Christian", "10/21/1987", "Regular", "Account Team Leader",  255.80, 42975, 1500, 800, 800));
        employees.add(new Employee("10017", "De Leon", "Selena", "02/20/1975", "Regular", "Account Team Leader", 249.11, 41850, 1500, 800, 800));
        employees.add(new Employee("10018", "San Jose", "Allison", "06/24/1986", "Regular", "Account Rank and File", 133.93, 22500, 1500, 500, 500));
        employees.add(new Employee("10019", "Rosario", "Cydney", "10/06/1996","Regular", "Account Rank and File", 133.93, 22500, 1500, 500, 500));
        employees.add(new Employee("10020", "Bautista", "Mark", "02/12/1991", "Regular", "Account Rank and File", 138.39, 23250, 1500, 500, 500));
        employees.add(new Employee("10021", "Lazaro", "Darlene", "11/25/1985", "Probationary", "Account Rank and File", 138.39, 23250, 1500, 500, 500));
        employees.add(new Employee("10022", "Delos Santos", "Kolby", "02/26/1980","Probationary", "Account Rank and File", 142.86, 24000, 1500, 500, 500));
        employees.add(new Employee("10023", "Santos", "Vella", "12/31/1983", "Probationary", "Account Rank and File", 133.93, 22500, 1500, 500, 500));
        employees.add(new Employee("10024", "Del Rosario", "Tomas", "12/18/1978", "Probationary", "Account Rank and File", 133.93, 22500, 1500, 500, 500));
        employees.add(new Employee("10025", "Tolentino", "Jacklyn", "05/19/1984", "Probationary", "Account Rank and File", 142.86, 24000, 1500, 500, 500));
        employees.add(new Employee("10026", "Gutierrez", "Percival", "12/18/1970", "Probationary", "Account Rank and File",  147.32, 24750, 1500, 500, 500));
        employees.add(new Employee("10027", "Manalaysay", "Garfield", "08/28/1986","Probationary", "Account Rank and File",  147.32, 24750, 1500, 500, 500));
        employees.add(new Employee("10028", "Villegas", "Lizeth", "12/12/1981", "Probationary", "Account Rank and File",  142.86, 24000, 1500, 500, 500));
        employees.add(new Employee("10029", "Ramos", "Carol", "08/20/1978", "Probationary", "Account Rank and File", 133.93, 22500, 1500, 500, 500));
        employees.add(new Employee("10030", "Maceda", "Emelia", "04/14/1973","Probationary", "Account Rank and File",  133.93, 22500, 1500, 500, 500));
        employees.add(new Employee("10031", "Aguilar", "Delia", "01/27/1989", "Probationary", "Account Rank and File", 133.93, 22500, 1500, 500, 500));
        employees.add(new Employee("10032", "Castro", "John Rafael", "02/09/1992", "Regular", "Sales & Marketing", 313.51, 52670, 1500, 1000, 1000));
        employees.add(new Employee("10033", "Martinez", "Carlos Ian", "11/16/1990", "Regular", "Supply Chain and Logistics", 313.51, 52670, 1500, 1000, 1000));
        employees.add(new Employee("10034", "Santos", "Beatriz", "08/07/1990", "Regular", "Customer Service and Relations", 313.51, 52670, 1500, 1000, 1000));
        return employees;
    }

    // View employee details
    private static void viewEmployeeDetails(Scanner scanner, List<Employee> employees) {
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. View details for a specific employee");
            System.out.println("2. View details for all employees");
            System.out.println("3. Back to main menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            System.out.println();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter employee number: ");
                    String empNumber = scanner.nextLine().trim();
                    boolean found = false;
                    for (Employee emp : employees) {
                        if (emp.getEmployeeNumber().equals(empNumber)) {
                            
                            System.out.println("----------------------------------------------------");
                            System.out.println("--------------- EMPLOYEE DETAILS -------------------");
                            System.out.println("Employee Number | Employee Name " + emp.getEmployeeNumber());
                            System.out.println("Employee Name: " + emp.firstName + " " + emp.lastName);
                            System.out.println("Birthdate: " + emp.birthDate);
                            System.out.println("Status: " + emp.status);
                            System.out.println("Position: " + emp.position);
                            System.out.println("--------------------------------------------------");
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Employee not found.");
                    }
                    break;
                case 2:
                    System.out.println("----------------------------------------------------");
                    System.out.println("--------------- EMPLOYEES DETAILS -------------------");
                    for (Employee emp : employees) {
                        System.out.println("Employee Number: " + emp.getEmployeeNumber());
                        System.out.println("Employee Name: " + emp.firstName + " " + emp.lastName);
                        System.out.println("Birthdate: " + emp.birthDate);
                        System.out.println("Status: " + emp.status);
                        System.out.println("Position: " + emp.position);
                        System.out.println("--------------------------------------------------");
                    }
                    break;
                case 3:
                    return; // Go back to the main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // View payroll details
    private static void viewPayroll(Scanner scanner, List<Employee> employees, Map<String, Set<String>> monthToWeeksMap) {
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. View payroll for a specific employee");
            System.out.println("2. View payroll for all employees");
            System.out.println("3. Back to main menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter employee number: ");
                    String empNumber = scanner.nextLine().trim();
                    boolean found = false;
                    for (Employee emp : employees) {
                        if (emp.getEmployeeNumber().equals(empNumber)) {
                            // Let the user choose a month
                            String selectedMonth = chooseMonth(scanner, monthToWeeksMap);
                            if (selectedMonth != null) {
                                // Get the weeks for the selected month
                                Set<String> weeksInMonth = monthToWeeksMap.get(selectedMonth);
                                if (weeksInMonth != null) {
                                    // Let the user choose specific weeks or all weeks
                                    Set<String> selectedWeeks = chooseWeeks(scanner, weeksInMonth, selectedMonth);
                                    if (selectedWeeks != null) 
                                         {
                                        // Display payroll for the selected weeks
                                        System.out.println("----------------------------------------");
                                        System.out.println("Employee Number: " + emp.employeeNumber);
                                        System.out.println("Employee Name: " + emp.firstName + " " + emp.lastName);
                                        for (String weekKey : selectedWeeks) {
                                            emp.displayPayrollDetailsForWeek(weekKey);
                                        }
                                    }
                                } else {
                                    System.out.println("No weeks found for the selected month.");
                                }
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Employee not found.");
                    }
                    break;
                case 2:
                    // Let the user choose a month
                    String selectedMonth = chooseMonth(scanner, monthToWeeksMap);
                    if (selectedMonth != null) {
                        // Get the weeks for the selected month
                        Set<String> weeksInMonth = monthToWeeksMap.get(selectedMonth);
                        if (weeksInMonth != null) {
                            // Let the user choose specific weeks or all weeks
                            Set<String> selectedWeeks = chooseWeeks(scanner, weeksInMonth, selectedMonth);
                            if (selectedWeeks != null) {
                                // Display payroll for all employees for the selected weeks
                                for (Employee emp : employees) {
                                    System.out.println("Employee Number: " + emp.employeeNumber);
                                    System.out.println("Employee Name: " + emp.firstName + " " + emp.lastName);
                                    for (String weekKey : selectedWeeks) {
                                        emp.displayPayrollDetailsForWeek(weekKey);
                                    }
                                 
                                }
                            }
                        } else {
                            System.out.println("No weeks found for the selected month.");
                        }
                    }
                    break;
                case 3:
                    return; // Go back to the main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Choose a month from the available months
    private static String chooseMonth(Scanner scanner, Map<String, Set<String>> monthToWeeksMap) {
        System.out.println("\nAvailable months in the attendance records:");
        List<String> months = new ArrayList<>(monthToWeeksMap.keySet());
        Collections.sort(months); // Sort months chronologically

        if (months.isEmpty()) {
            System.out.println("No months found in the attendance records.");
            return null;
        }

        for (int i = 0; i < months.size(); i++) {
            System.out.println((i + 1) + ". " + months.get(i));
        }

        System.out.print("Enter the number of the month you want to view: ");
        int monthChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (monthChoice < 1 || monthChoice > months.size()) {
            System.out.println("Invalid choice. Please try again.");
            return null;
        }

        return months.get(monthChoice - 1);
    }

    // Choose specific weeks or all weeks in a month
    private static Set<String> chooseWeeks(Scanner scanner, Set<String> weeksInMonth, String selectedMonth) {
        List<String> weeks = new ArrayList<>(weeksInMonth);
        
        
        Collections.sort(weeks, (week1, week2) -> {
        LocalDate startDate1 = getStartDateFromWeekKey(week1);
        LocalDate startDate2 = getStartDateFromWeekKey(week2);
        return startDate1.compareTo(startDate2);
    });

        System.out.println("\nAvailable weeks in the selected month:");
        List<String> validWeeks = new ArrayList<>();

        for (int i = 0; i < weeks.size(); i++) {
            String weekKey = weeks.get(i);
            String dateRange = getWeekDateRange(weekKey); // Get the date range for the week

            // Check if the week falls within the selected month
            String[] parts = dateRange.split(" to ");
            String startDate = parts[0];
            String endDate = parts[1];

            // Extract the month from the start and end dates
            String startMonth = startDate.split("/")[0];
            String endMonth = endDate.split("/")[0];

            // Only include the week if it falls within the selected month
            if (startMonth.equals(selectedMonth.split("/")[0]) || endMonth.equals(selectedMonth.split("/")[0])) {
                System.out.println((validWeeks.size() + 1) + ". " + dateRange);
                validWeeks.add(weekKey);
            }
        }

        System.out.println((validWeeks.size() + 1) + ". All weeks");
        System.out.print("Enter the number of the week(s) you want to view (comma-separated for multiple): ");
        String input = scanner.nextLine().trim();
        String[] choices = input.split(",");

        Set<String> selectedWeeks = new HashSet<>();
        for (String choice : choices) {
            try {
                int weekChoice = Integer.parseInt(choice.trim());
                if (weekChoice == validWeeks.size() + 1) {
                    // User selected "All weeks"
                    return new HashSet<>(validWeeks);
                } else if (weekChoice >= 1 && weekChoice <= validWeeks.size()) {
                    selectedWeeks.add(validWeeks.get(weekChoice - 1));
                } else {
                    System.out.println("Invalid choice: " + weekChoice);
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + choice);
                return null;
            }
        }

        return selectedWeeks;
    }
    // Helper method to get the start date from a week key
    private static LocalDate getStartDateFromWeekKey(String weekKey) {
    String[] parts = weekKey.split("-");
    int year = Integer.parseInt(parts[0]);
    int week = Integer.parseInt(parts[1]);

    // Get the first day of the week (Monday) for the given year and week
    return LocalDate.of(year, 1, 1)
            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
}

    // Get the date range for a week
    private static String getWeekDateRange(String weekKey) {
        try {
            // Split the weekKey into year and week number
            String[] parts = weekKey.split("-");
            int year = Integer.parseInt(parts[0]);
            int week = Integer.parseInt(parts[1]);

            // Get the first day of the week (Monday) for the given year and week
            LocalDate startOfWeek = LocalDate.of(year, 1, 1) // Start from January 1st of the year
                    .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week) // Move to the correct week
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // Adjust to Monday

            // Calculate the end of the week (Sunday)
            LocalDate endOfWeek = startOfWeek.plusDays(6);

            // Format the date range
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return startOfWeek.format(dateFormatter) + " to " + endOfWeek.format(dateFormatter);
        } catch (Exception e) {
            return "Invalid Week";
        }
    }

    // Read employee attendance from CSV file
    public static Map<String, Set<String>> readEmployeeAttendance(String filePath, List<Employee> employees) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        Map<String, Set<String>> monthToWeeksMap = new HashMap<>();
        Set<String> availableMonths = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Use comma as the delimiter
                if (values.length >= 6) {
                    String employeeNumber = values[0].trim();
                    String dateStr = values[3].trim(); // Adjusted index for Date
                    String logInTime = values[4].trim(); // Adjusted index for Log In
                    String logOutTime = values[5].trim(); // Adjusted index for Log Out

                    if (logInTime.isEmpty() || logOutTime.isEmpty()) {
                        System.out.println("Skipping incomplete record for employee: " + employeeNumber);
                        continue;
                    }

                    try {
                        // Parse date and times
                        LocalDate logDate = LocalDate.parse(dateStr, dateFormatter);
                        LocalTime logIn = LocalTime.parse(logInTime, timeFormatter);
                        LocalTime logOut = LocalTime.parse(logOutTime, timeFormatter);

                        // Validate logIn and logOut times
                        if (logOut.isBefore(logIn)) {
                            System.out.println("Invalid logOut time for employee: " + employeeNumber + " on " + dateStr);
                            continue;
                        }

                        // Determine the month (e.g., "06/2023")
                        String monthKey = logDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));
                        availableMonths.add(monthKey);

                        // Determine the week number (e.g., "2023-23")
                        String weekKey = logDate.format(DateTimeFormatter.ofPattern("yyyy-ww"));

                        // Add the week to the corresponding month in the map
                        monthToWeeksMap.computeIfAbsent(monthKey, k -> new HashSet<>()).add(weekKey);

                        // Calculate hours worked for the day
                        double hoursWorked = Duration.between(logIn, logOut).toHours();

                        // Validate hours worked (must be between 0 and 24)
                        if (hoursWorked > 24 || hoursWorked < 0) {
                            System.out.println("Invalid hours worked for employee: " + employeeNumber + " on " + dateStr);
                            continue;
                        }

                        // Add hours worked to the corresponding employee's weekly hours
                        for (Employee emp : employees) {
                            if (emp.getEmployeeNumber().equals(employeeNumber)) {
                                emp.addHoursWorked(weekKey, hoursWorked);
                                break;
                            }
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing date or time for employee: " + employeeNumber + " on " + dateStr);
                        continue;
                    }
                } else {
                    System.out.println("Skipping malformed record: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
        }

        // Debugging: Print the contents of monthToWeeksMap
        System.out.println("Contents of monthToWeeksMap:");
        for (Map.Entry<String, Set<String>> entry : monthToWeeksMap.entrySet()) {
            System.out.println("Month: " + entry.getKey() + ", Weeks: " + entry.getValue());
        }

        // Print the available months
        System.out.println("Available months: " + availableMonths);

        return monthToWeeksMap;
    }

    // Nested Employee class
    static class Employee {
        String employeeNumber, lastName, firstName, birthDate, status, position;
        double hourlyRate, basicSalary, riceSubsidy, phoneAllowance, clothingAllowance;
        Map<String, Double> weeklyHours = new HashMap<>();

        public Employee(String empNum, String lName, String fName, String birthDate, String status, String position, double hRate, double bSalary, double rice, double phone, double clothing) {
            employeeNumber = empNum;
            lastName = lName;
            firstName = fName;
            this.birthDate = birthDate;
            this.status = status;
            this.position = position;
            hourlyRate = hRate;
            basicSalary = bSalary;
            riceSubsidy = rice;
            phoneAllowance = phone;
            clothingAllowance = clothing;
        }
   
        public String getEmployeeNumber() {
            return employeeNumber;
        }

        public void addHoursWorked(String week, double hours) {
            weeklyHours.put(week, weeklyHours.getOrDefault(week, 0.0) + hours);
            }

        public double calculateGrossSalary(String weekKey) {
            double hoursWorked = weeklyHours.getOrDefault(weekKey, 0.0);
            return hoursWorked * hourlyRate + riceSubsidy + phoneAllowance + clothingAllowance;
        }
        

        public double calculateSSS() {
            double monthlySalary = basicSalary;
            if (monthlySalary < 3250) return 135.00 / 4; // Weekly SSS
            else if (monthlySalary <= 3750) return 157.50 / 4;
            else if (monthlySalary <= 4250) return 180.00 / 4;
            else if (monthlySalary <= 4750) return 202.50 / 4;
            else if (monthlySalary <= 5250) return 225.00 / 4;
            else if (monthlySalary <= 5750) return 247.50 / 4;
            else if (monthlySalary <= 6250) return 270.00 / 4;
            else if (monthlySalary <= 6750) return 292.50 / 4;
            else if (monthlySalary <= 7250) return 315.00 / 4;
            else if (monthlySalary <= 7750) return 337.50 / 4;
            else if (monthlySalary <= 8250) return 360.00 / 4;
            else if (monthlySalary <= 8750) return 382.50 / 4;
            else if (monthlySalary <= 9250) return 405.00 / 4;
            else if (monthlySalary <= 9750) return 427.50 / 4;
            else if (monthlySalary <= 10250) return 450.00 / 4;
            else if (monthlySalary <= 10750) return 472.50 / 4;
            else if (monthlySalary <= 11250) return 495.00 / 4;
            else if (monthlySalary <= 11750) return 517.50 / 4;
            else if (monthlySalary <= 12250) return 540.00 / 4;
            else if (monthlySalary <= 12750) return 562.50 / 4;
            else if (monthlySalary <= 13250) return 585.00 / 4;
            else if (monthlySalary <= 13750) return 607.50 / 4;
            else if (monthlySalary <= 14250) return 630.00 / 4;
            else if (monthlySalary <= 14750) return 652.50 / 4;
            else if (monthlySalary <= 15250) return 675.00 / 4;
            else if (monthlySalary <= 15750) return 697.50 / 4;
            else if (monthlySalary <= 16250) return 720.00 / 4;
            else if (monthlySalary <= 16750) return 742.50 / 4;
            else if (monthlySalary <= 17250) return 765.00 / 4;
            else if (monthlySalary <= 17750) return 787.50 / 4;
            else if (monthlySalary <= 18250) return 810.00 / 4;
            else if (monthlySalary <= 18750) return 832.50 / 4;
            else if (monthlySalary <= 19250) return 855.00 / 4;
            else if (monthlySalary <= 19750) return 877.50 / 4;
            else if (monthlySalary <= 20250) return 900.00 / 4;
            else if (monthlySalary <= 20750) return 922.50 / 4;
            else if (monthlySalary <= 21250) return 945.00 / 4;
            else if (monthlySalary <= 21750) return 967.50 / 4;
            else if (monthlySalary <= 22250) return 990.00 / 4;
            else if (monthlySalary <= 22750) return 1012.50 / 4;
            else if (monthlySalary <= 23250) return 1035.00 / 4;
            else if (monthlySalary <= 23750) return 1057.50 / 4;
            else if (monthlySalary <= 24250) return 1080.00 / 4;
            else if (monthlySalary <= 24750) return 1102.50 / 4;
            else return 1125.00 / 4;
        }
        
        
        public double calculatePhilHealth() {
            double monthlySalary = basicSalary;
            if (monthlySalary <= 10000) return 300.00 / 4; // Weekly PhilHealth
            else if (monthlySalary < 60000) return (monthlySalary * 0.03) / 4;
            else return 1800.00 / 4;
        }

        public double calculatePagIbig() {
            double monthlySalary = basicSalary;
            if (monthlySalary <= 1500) {
                return (monthlySalary * 0.01) / 4; // Weekly Pag-IBIG (1%)
            } else {
                double contribution = monthlySalary * 0.02; // 2% for salaries over PHP 1,500
                return Math.min(contribution, 100) / 4; // Maximum contribution is PHP 100
            }
        }

        public double calculateWithholdingTax() {
            double monthlySalary = basicSalary;
            double taxableIncome = monthlySalary - (calculateSSS() * 4 + calculatePhilHealth() * 4 + calculatePagIbig() * 4);

            if (taxableIncome <= 20832) return 0;
            else if (taxableIncome < 33333) return (taxableIncome - 20833) * 0.20 / 4; // Weekly Withholding Tax
            else if (taxableIncome < 66667) return (2500 + (taxableIncome - 33333) * 0.25) / 4;
            else if (taxableIncome < 166667) return (10833 + (taxableIncome - 66667) * 0.30) / 4;
            else if (taxableIncome < 666667) return (40833.33 + (taxableIncome - 166667) * 0.32) / 4;
            else return (200833.33 + (taxableIncome - 666667) * 0.35) / 4;
        }

        public double calculateNetSalary(String weekKey) {
            return calculateGrossSalary(weekKey) - (calculateSSS() + calculatePhilHealth() + calculatePagIbig() + calculateWithholdingTax());
        }

        public void displayPayrollDetailsForWeek(String weekKey) {
            if (weeklyHours.containsKey(weekKey)) {
                String dateRange = getWeekDateRange(weekKey);
                System.out.println("Week: " + dateRange);
                System.out.println("Hours Worked: " + weeklyHours.get(weekKey));
                System.out.println("Gross Weekly Salary: PHP " + calculateGrossSalary(weekKey));
                System.out.println("SSS Deduction (Weekly): PHP " + calculateSSS());
                System.out.println("PhilHealth Deduction (Weekly): PHP " + calculatePhilHealth());
                System.out.println("Pag-IBIG Deduction (Weekly): PHP " + calculatePagIbig());
                System.out.println("Withholding Tax (Weekly): PHP " + calculateWithholdingTax());
                System.out.println("Net Weekly Salary: PHP " + calculateNetSalary (weekKey));
                System.out.println("--------------------------------------------------");
            }
        }

        public double getLatestWeekHours() {
            if (weeklyHours.isEmpty()) return 0.0;
            List<String> sortedWeeks = new ArrayList<>(weeklyHours.keySet());
            Collections.sort(sortedWeeks);
            return weeklyHours.get(sortedWeeks.get(sortedWeeks.size() - 1));
        }

        public String getLatestWeekKey() {
            if (weeklyHours.isEmpty()) return null;
            List<String> sortedWeeks = new ArrayList<>(weeklyHours.keySet());
            Collections.sort(sortedWeeks);
            return sortedWeeks.get(sortedWeeks.size() - 1);
        }
    }
}

/* Group: GCP2025
Porte, Ethan Malcolm
Abadilla, Jelyn 
Barcarse, Gemmarie Sabinah
Ignacio, Charlene Mae
Lopez, Kristian Joy Emmanuel
Santiago, Kiarra Anne
Santos, Sarah Natalie Jean
Turgo, Althea
*/