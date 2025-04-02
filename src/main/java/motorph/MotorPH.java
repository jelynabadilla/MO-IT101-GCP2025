package motorph;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MotorPH {
    public static void main(String[] args) {
        // Initialize Scanner for user input and FileHandler for file operations.
        Scanner scanner = new Scanner(System.in);
        FileHandler fileHandler = new FileHandler();
        PayrollCalculator payroll = new PayrollCalculator(fileHandler);

        // Print the main header for the MotorPH Payroll System.
        printSectionHeader("MOTORPH PAYROLL SYSTEM");

        // Main loop for the system menu.
        while (true) {
            System.out.println("MAIN MENU");
            System.out.println("1. Employee Management");
            System.out.println("2. Attendance Management");
            System.out.println("3. Payroll Calculation");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            // Switch case to handle user choices in the main menu.
            switch (choice) {
                case "1":
                    employeeMenu(scanner, fileHandler); // Navigate to Employee Management menu.
                    break;
                case "2":
                    attendanceMenu(scanner, fileHandler); // Navigate to Attendance Management menu.
                    break;
                case "3":
                    payrollMenu(scanner, payroll, fileHandler); // Navigate to Payroll Calculation menu.
                    break;
                case "0":
                    System.out.println("Exiting system. Goodbye!"); // Exit the system.
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again."); // Handle invalid input.
            }
        }
    }

    // Method to print a formatted section header.
    private static void printSectionHeader(String title) {
        System.out.println("\n----------------------------------------------------");
        System.out.println("---------------- " + title + " ----------------");
        System.out.println("----------------------------------------------------");
    }

    // Method to print a formatted section footer.
    private static void printSectionFooter() {
        System.out.println("----------------------------------------------------");
    }

    // Method to display and handle the Employee Management menu.
    private static void employeeMenu(Scanner scanner, FileHandler fileHandler) {
        while (true) {
            printSectionHeader("EMPLOYEE MANAGEMENT");
            System.out.println("1. View All Employees");
            System.out.println("2. View Specific Employee");
            System.out.println("3. Add New Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            // Switch case to handle Employee Management options.
            switch (choice) {
                case "1":
                    viewAllEmployees(fileHandler); // Display all employees.
                    break;
                case "2":
                    viewSpecificEmployee(scanner, fileHandler); // Display details of a specific employee.
                    break;
                case "3":
                    addEmployee(scanner, fileHandler); // Add a new employee.
                    break;
                case "4":
                    updateEmployee(scanner, fileHandler); // Update an existing employee's details.
                    break;
                case "5":
                    deleteEmployee(scanner, fileHandler); // Delete an employee.
                    break;
                case "0":
                    return; // Return to the main menu.
                default:
                    System.out.println("Invalid choice. Please try again."); // Handle invalid input.
            }
        }
    }

    private static void viewAllEmployees(FileHandler fileHandler) {
        List<Employee> employees = fileHandler.readEmployees();
        if (employees.isEmpty()) {
            printSectionHeader("EMPLOYEE LIST");
            System.out.println("No employees found.");
            printSectionFooter();
            return;
        }

        printSectionHeader("EMPLOYEE LIST");
        System.out.printf("%-8s %-20s %-20s %-30s %s\n", 
            "ID", "Last Name", "First Name", "Position", "Basic Salary");
        for (Employee emp : employees) {
            System.out.printf("%-8s %-20s %-20s %-30s PHP %,.2f\n",
                emp.getEmployeeId(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getPosition(),
                emp.getBasicSalary());
        }
        printSectionFooter();
    }

    private static void viewSpecificEmployee(Scanner scanner, FileHandler fileHandler) {
        printSectionHeader("VIEW EMPLOYEE DETAILS");
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();
        
        Employee employee = fileHandler.findEmployee(id);
        if (employee == null) {
            System.out.println("Employee not found!");
            printSectionFooter();
            return;
        }

        System.out.println("\nEmployee Details:");
        System.out.printf("%-20s: %s\n", "Employee ID", employee.getEmployeeId());
        System.out.printf("%-20s: %s, %s\n", "Name", employee.getLastName(), employee.getFirstName());
        System.out.printf("%-20s: %s\n", "Birthday", employee.getBirthday() != null ? 
            employee.getBirthday().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) : "N/A");
        System.out.printf("%-20s: %s\n", "Address", employee.getAddress());
        System.out.printf("%-20s: %s\n", "Phone Number", employee.getPhoneNumber());
        System.out.printf("%-20s: %s\n", "Status", employee.getStatus());
        System.out.printf("%-20s: %s\n", "Position", employee.getPosition());
        System.out.printf("%-20s: %s\n", "Supervisor", employee.getSupervisor());
        System.out.printf("%-20s: PHP %,.2f\n", "Basic Salary", employee.getBasicSalary());
        System.out.printf("%-20s: PHP %,.2f\n", "Rice Subsidy", employee.getRiceSubsidy());
        System.out.printf("%-20s: PHP %,.2f\n", "Phone Allowance", employee.getPhoneAllowance());
        System.out.printf("%-20s: PHP %,.2f\n", "Clothing Allowance", employee.getClothingAllowance());
        System.out.printf("%-20s: PHP %,.2f\n", "Gross Rate", employee.getGrossRate());
        System.out.printf("%-20s: PHP %,.2f\n", "Hourly Rate", employee.getHourlyRate());
        printSectionFooter();
    }

    private static void addEmployee(Scanner scanner, FileHandler fileHandler) {
        printSectionHeader("ADD NEW EMPLOYEE");
        Employee employee = new Employee();
        Map<String, String> data = new HashMap<>();

        System.out.print("Employee ID: ");
        data.put("EmployeeID", scanner.nextLine());
        System.out.print("Last Name: ");
        data.put("LastName", scanner.nextLine());
        System.out.print("First Name: ");
        data.put("FirstName", scanner.nextLine());
        System.out.print("Birthday (MM/DD/YYYY): ");
        data.put("Birthday", scanner.nextLine());
        System.out.print("Address: ");
        data.put("Address", scanner.nextLine());
        System.out.print("Phone Number: ");
        data.put("PhoneNumber", scanner.nextLine());
        System.out.print("SSS Number: ");
        data.put("SSS", scanner.nextLine());
        System.out.print("PhilHealth Number: ");
        data.put("Philhealth", scanner.nextLine());
        System.out.print("TIN Number: ");
        data.put("TIN", scanner.nextLine());
        System.out.print("Pag-IBIG Number: ");
        data.put("Pagibig", scanner.nextLine());
        System.out.print("Status: ");
        data.put("Status", scanner.nextLine());
        System.out.print("Position: ");
        data.put("Position", scanner.nextLine());
        System.out.print("Supervisor: ");
        data.put("Supervisor", scanner.nextLine());
        System.out.print("Basic Salary: ");
        data.put("BasicSalary", scanner.nextLine());
        System.out.print("Rice Subsidy: ");
        data.put("RiceSubsidy", scanner.nextLine());
        System.out.print("Phone Allowance: ");
        data.put("PhoneAllowance", scanner.nextLine());
        System.out.print("Clothing Allowance: ");
        data.put("ClothingAllowance", scanner.nextLine());
        System.out.print("Gross Semi-monthly Rate: ");
        data.put("GrossRate", scanner.nextLine());
        System.out.print("Hourly Rate: ");
        data.put("HourlyRate", scanner.nextLine());

        employee = new Employee(data);
        fileHandler.saveEmployee(employee);
        System.out.println("\nEmployee added successfully!");
        printSectionFooter();
    }

    private static void updateEmployee(Scanner scanner, FileHandler fileHandler) {
        printSectionHeader("UPDATE EMPLOYEE");
        System.out.print("Enter Employee ID to update: ");
        String id = scanner.nextLine();
        Employee employee = fileHandler.findEmployee(id);

        if (employee == null) {
            System.out.println("Employee not found!");
            printSectionFooter();
            return;
        }

        System.out.println("\nCurrent Employee Details:");
        System.out.println("1. Last Name: " + employee.getLastName());
        System.out.println("2. First Name: " + employee.getFirstName());
        System.out.println("3. Birthday: " + (employee.getBirthday() != null ? 
             employee.getBirthday().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) : "N/A"));
        System.out.println("4. Address: " + employee.getAddress());
        System.out.println("5. Phone Number: " + employee.getPhoneNumber());
        System.out.println("6. Basic Salary: PHP " + String.format("%,.2f", employee.getBasicSalary()));
        System.out.println("0. Save Changes");

        while (true) {
            System.out.print("\nEnter field number to update (0 to save): ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                fileHandler.saveEmployee(employee);
                System.out.println("\nEmployee updated successfully!");
                printSectionFooter();
                break;
            }

            switch (choice) {
                case "1":
                    System.out.print("Enter new Last Name: ");
                    employee.setLastName(scanner.nextLine());
                    break;
                case "2":
                    System.out.print("Enter new First Name: ");
                    employee.setFirstName(scanner.nextLine());
                    break;
                case "3":
                    System.out.print("Enter new Birthday (MM/DD/YYYY): ");
                    employee.setBirthday(scanner.nextLine());
                    break;
                case "4":
                    System.out.print("Enter new Address: ");
                    employee.setAddress(scanner.nextLine());
                    break;
                case "5":
                    System.out.print("Enter new Phone Number: ");
                    employee.setPhoneNumber(scanner.nextLine());
                    break;
                case "6":
                    System.out.print("Enter new Basic Salary: ");
                    employee.setBasicSalary(Double.parseDouble(scanner.nextLine()));
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void deleteEmployee(Scanner scanner, FileHandler fileHandler) {
        printSectionHeader("DELETE EMPLOYEE");
        System.out.print("Enter Employee ID to delete: ");
        String id = scanner.nextLine();

        if (fileHandler.deleteEmployee(id)) {
            System.out.println("\nEmployee deleted successfully!");
        } else {
            System.out.println("\nEmployee not found!");
        }
        printSectionFooter();
    }

    private static void attendanceMenu(Scanner scanner, FileHandler fileHandler) {
        while (true) {
            printSectionHeader("ATTENDANCE MANAGEMENT");
            System.out.println("1. View Employee Attendance Records");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewEmployeeAttendance(scanner, fileHandler);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewEmployeeAttendance(Scanner scanner, FileHandler fileHandler) {
        printSectionHeader("VIEW EMPLOYEE ATTENDANCE");
        
        System.out.print("Enter Employee ID: ");
        String employeeId = scanner.nextLine();
        
        Employee employee = fileHandler.findEmployee(employeeId);
        if (employee == null) {
            System.out.println("Employee not found!");
            printSectionFooter();
            return;
        }
        
        List<Attendance> allRecords = fileHandler.getAllAttendanceRecords().stream()
                .filter(r -> r.getEmployeeId().equals(employeeId))
                .sorted(Comparator.comparing(Attendance::getDate))
                .collect(Collectors.toList());
        
        if (allRecords.isEmpty()) {
            System.out.println("No attendance records found for this employee.");
            printSectionFooter();
            return;
        }
        
        List<YearMonth> availableMonths = allRecords.stream()
                .map(r -> YearMonth.from(r.getDate()))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        
        System.out.println("\nAvailable Months:");
        for (int i = 0; i < availableMonths.size(); i++) {
            System.out.printf("%d. %s%n", i+1, availableMonths.get(i).format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        }
        System.out.print("Select month: ");
        int monthChoice = Integer.parseInt(scanner.nextLine());
        
        List<Attendance> filteredRecords;
        if (monthChoice == 0) {
            filteredRecords = allRecords;
        } else if (monthChoice > 0 && monthChoice <= availableMonths.size()) {
            YearMonth selectedMonth = availableMonths.get(monthChoice - 1);
            filteredRecords = allRecords.stream()
                    .filter(r -> YearMonth.from(r.getDate()).equals(selectedMonth))
                    .collect(Collectors.toList());
        } else {
            System.out.println("Invalid month selection.");
            printSectionFooter();
            return;
        }
        
        System.out.println("\nWeek Options:");
        System.out.println("1. Week 1");
        System.out.println("2. Week 2");
        System.out.println("3. Week 3");
        System.out.println("4. Week 4");
        System.out.println("5. All Weeks");
        System.out.print("Select week (1-5): ");
        int weekChoice = Integer.parseInt(scanner.nextLine());
        
        Map<Integer, List<Attendance>> weeklyRecords = filteredRecords.stream()
                .collect(Collectors.groupingBy(
                    r -> r.getDate().get(WeekFields.ISO.weekOfMonth())
                ));
        
        System.out.println("ATTENDANCE RECORDS FOR " + employee.getLastName() + ", " + employee.getFirstName());
        
        if (weekChoice == 5) {
            weeklyRecords.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> printWeekAttendance(entry.getKey(), entry.getValue()));
        } else if (weekChoice >= 1 && weekChoice <= 4) {
            if (weeklyRecords.containsKey(weekChoice)) {
                printWeekAttendance(weekChoice, weeklyRecords.get(weekChoice));
            } else {
                System.out.println("No records found for week " + weekChoice);
            }
        } else {
            System.out.println("Invalid week selection.");
        }
        
        printSectionFooter();
    }

    private static void printWeekAttendance(int weekNumber, List<Attendance> records) {
        System.out.printf("\nWeek %d (%s to %s):\n", 
            weekNumber,
            records.get(0).getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
            records.get(records.size()-1).getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        
        System.out.printf("%-12s %-8s %-8s\n", "Date", "Time In", "Time Out");
        for (Attendance record : records) {
            System.out.printf("%-12s %-8s %-8s\n",
                record.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                record.getTimeIn(),
                record.getTimeOut());
        }
    }

    private static void addAttendance(Scanner scanner, FileHandler fileHandler) {
        printSectionHeader("ADD ATTENDANCE RECORD");
        System.out.print("Employee ID: ");
        String employeeId = scanner.nextLine();
        System.out.print("Date (MM/DD/YYYY): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        System.out.print("Time In (HH:MM): ");
        LocalTime timeIn = LocalTime.parse(scanner.nextLine());
        System.out.print("Time Out (HH:MM): ");
        LocalTime timeOut = LocalTime.parse(scanner.nextLine());

        fileHandler.recordAttendance(employeeId, date, timeIn, timeOut);
        System.out.println("\nAttendance record added successfully!");
        printSectionFooter();
    }

    private static void updateAttendance(Scanner scanner, FileHandler fileHandler) {
        printSectionHeader("UPDATE ATTENDANCE RECORD");
        System.out.print("Enter Employee ID: ");
        String employeeId = scanner.nextLine();
        System.out.print("Enter Date (MM/DD/YYYY): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        Attendance record = fileHandler.findAttendanceRecord(employeeId, date);
        if (record == null) {
            System.out.println("Attendance record not found!");
            printSectionFooter();
            return;
        }

        System.out.println("\nCurrent Attendance Record:");
        System.out.println("Time In: " + record.getTimeIn());
        System.out.println("Time Out: " + record.getTimeOut());

        System.out.print("\nEnter new Time In (HH:MM): ");
        record.setTimeIn(LocalTime.parse(scanner.nextLine()));
        System.out.print("Enter new Time Out (HH:MM): ");
        record.setTimeOut(LocalTime.parse(scanner.nextLine()));

        List<Attendance> records = fileHandler.getAllAttendanceRecords();
        records.removeIf(r -> r.getEmployeeId().equals(employeeId) && r.getDate().equals(date));
        records.add(record);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileHandler.getAttendanceFilePath()))) {
            writer.println(FileHandler.ATTENDANCE_HEADER);
            for (Attendance r : records) {
                writer.printf("%s,%s,%s,%s,%s,%s\n", 
                    r.getEmployeeId(), 
                    "", // Last name placeholder
                    "", // First name placeholder
                    r.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                    r.getTimeIn(),
                    r.getTimeOut());
            }
            System.out.println("\nAttendance record updated successfully!");
        } catch (IOException e) {
            System.out.println("Error updating attendance record: " + e.getMessage());
        }
        printSectionFooter();
    }

    private static void deleteAttendance(Scanner scanner, FileHandler fileHandler) {
        printSectionHeader("DELETE ATTENDANCE RECORD");
        System.out.print("Enter Employee ID: ");
        String employeeId = scanner.nextLine();
        System.out.print("Enter Date (MM/DD/YYYY): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        List<Attendance> records = fileHandler.getAllAttendanceRecords();
        boolean removed = records.removeIf(r -> 
            r.getEmployeeId().equals(employeeId) && r.getDate().equals(date));

        if (removed) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileHandler.getAttendanceFilePath()))) {
                writer.println(FileHandler.ATTENDANCE_HEADER);
                for (Attendance r : records) {
                    writer.printf("%s,%s,%s,%s,%s,%s\n", 
                        r.getEmployeeId(),
                        "", // Last name placeholder
                        "", // First name placeholder
                        r.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                        r.getTimeIn(),
                        r.getTimeOut());
                }
                System.out.println("\nAttendance record deleted successfully!");
            } catch (IOException e) {
                System.out.println("Error deleting attendance record: " + e.getMessage());
            }
        } else {
            System.out.println("\nAttendance record not found!");
        }
        printSectionFooter();
    }

    private static void payrollMenu(Scanner scanner, PayrollCalculator payroll, FileHandler fileHandler) {
        while (true) {
            printSectionHeader("PAYROLL CALCULATION");
            System.out.println("1. Calculate for Specific Employee");
            System.out.println("2. Calculate for All Employees");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    calculateEmployeePayroll(scanner, payroll, fileHandler);
                    break;
                case "2":
                    calculateAllEmployeesPayroll(scanner, payroll, fileHandler);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void calculateEmployeePayroll(Scanner scanner, PayrollCalculator payroll, FileHandler fileHandler) {
        printSectionHeader("PAYROLL CALCULATION");
        System.out.print("Enter Employee ID: ");
        String employeeId = scanner.nextLine();
        
        Employee employee = fileHandler.findEmployee(employeeId);
        if (employee == null) {
            System.out.println("Employee not found!");
            printSectionFooter();
            return;
        }
        
        List<YearMonth> availableMonths = payroll.getAvailableMonths(employeeId);
        if (availableMonths.isEmpty()) {
            System.out.println("No attendance records found for this employee.");
            printSectionFooter();
            return;
        }
        
        System.out.println("\nAvailable Months:");
        for (int i = 0; i < availableMonths.size(); i++) {
            System.out.printf("%d. %s%n", i+1, availableMonths.get(i).format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        }
        
        System.out.print("Select month (number): ");
        int monthChoice = Integer.parseInt(scanner.nextLine()) - 1;
        YearMonth selectedMonth = availableMonths.get(monthChoice);
        
        System.out.println("\nWeek Options:");
        System.out.println("1. Week 1");
        System.out.println("2. Week 2");
        System.out.println("3. Week 3");
        System.out.println("4. Week 4");
        System.out.println("5. All Weeks");
        System.out.print("Select week (1-5): ");
        int weekChoice = Integer.parseInt(scanner.nextLine());
        
        payroll.calculateWeeklyPayroll(employeeId, selectedMonth, weekChoice == 5 ? 0 : weekChoice);
        printSectionFooter();
    }

    private static void calculateAllEmployeesPayroll(Scanner scanner, PayrollCalculator payroll, FileHandler fileHandler) {
        printSectionHeader("PAYROLL CALCULATION FOR ALL EMPLOYEES");
        List<YearMonth> availableMonths = payroll.getAllAvailableMonths();
        if (availableMonths.isEmpty()) {
            System.out.println("No attendance records found.");
            printSectionFooter();
            return;
        }
        
        System.out.println("\nAvailable Months:");
        for (int i = 0; i < availableMonths.size(); i++) {
            System.out.printf("%d. %s%n", i+1, availableMonths.get(i).format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        }
        
        System.out.print("Select month (number): ");
        int monthChoice = Integer.parseInt(scanner.nextLine()) - 1;
        YearMonth selectedMonth = availableMonths.get(monthChoice);
        
        System.out.println("\nWeek Options:");
        System.out.println("1. Week 1");
        System.out.println("2. Week 2");
        System.out.println("3. Week 3");
        System.out.println("4. Week 4");
        System.out.println("5. All Weeks");
        System.out.print("Select week (1-5): ");
        int weekChoice = Integer.parseInt(scanner.nextLine());
        
        payroll.calculateAllWeeklyPayroll(selectedMonth, weekChoice == 5 ? 0 : weekChoice);
        printSectionFooter();
    }
}
