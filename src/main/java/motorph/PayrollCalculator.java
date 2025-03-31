package motorph;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class PayrollCalculator {
    private FileHandler fileHandler;
    
    public PayrollCalculator(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }
    
    // Constants
    private static final double REGULAR_DAY_OT_MULTIPLIER = 1.25;    // 125% for weekdays
    private static final double REST_DAY_OT_MULTIPLIER = 1.30;       // 130% for weekends
    private static final LocalTime WORK_START_TIME = LocalTime.of(8, 0);
    private static final int GRACE_PERIOD_MINUTES = 10;
    private static final int REGULAR_HOURS_PER_DAY = 8;
    private static final int MINUTES_PER_HOUR = 60;

    
    //Calculates regular hours worked (max 8 hours per day)
    private double calculateRegularHours(LocalTime timeIn, LocalTime timeOut) {
        long totalMinutes = ChronoUnit.MINUTES.between(timeIn, timeOut);
        double totalHours = totalMinutes / (double) MINUTES_PER_HOUR;
        return Math.min(totalHours, REGULAR_HOURS_PER_DAY);
    }
    
    //Calculates overtime hours with proper rates. Returns the actual overtime hours (without multiplier)
    private double calculateOvertimeHours(LocalDate date, LocalTime timeIn, LocalTime timeOut) {
        long totalMinutes = ChronoUnit.MINUTES.between(timeIn, timeOut);
        double totalHours = totalMinutes / (double) MINUTES_PER_HOUR;
        double overtimeHours = Math.max(0, totalHours - REGULAR_HOURS_PER_DAY);
        return overtimeHours;
    }
    
    //Determines the appropriate overtime multiplier based on the day of the week
    private double getOvertimeMultiplier(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean isRestDay = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
        return isRestDay ? REST_DAY_OT_MULTIPLIER : REGULAR_DAY_OT_MULTIPLIER;
    }
    
    //If employee arrives after 8:10 AM, they are considered late at 8:11
    private int calculateLateMinutes(LocalTime timeIn) {
        LocalTime graceTime = WORK_START_TIME.plusMinutes(GRACE_PERIOD_MINUTES);
        
        // Employee is late if they arrive after the grace period (8:10 AM)
        if (timeIn.isAfter(graceTime)) {
            // Calculate minutes after the official start time (8:00 AM), not the grace period
            // This means lateness is calculated from 8:00 AM, not 8:10 AM
            return (int) ChronoUnit.MINUTES.between(WORK_START_TIME, timeIn);
        }
        
        return 0;
    }
    
    //Converts late minutes to monetary deduction
    private double calculateLateDeduction(int lateMinutes, double hourlyRate) {
        double minutesInHour = 60.0;
        return (lateMinutes / minutesInHour) * hourlyRate;
    }
    
    
    //SSS Contribution (2023 rates)
    private double calculateSSS(double monthlySalary) {
        if (monthlySalary < 3250) return 135.00;
        if (monthlySalary <= 3750) return 157.50;
        if (monthlySalary <= 4250) return 180.00;
        if (monthlySalary <= 4750) return 202.50;
        if (monthlySalary <= 5250) return 225.00;
        if (monthlySalary <= 5750) return 247.50;
        if (monthlySalary <= 6250) return 270.00;
        if (monthlySalary <= 6750) return 292.50;
        if (monthlySalary <= 7250) return 315.00;
        if (monthlySalary <= 7750) return 337.50;
        if (monthlySalary <= 8250) return 360.00;
        if (monthlySalary <= 8750) return 382.50;
        if (monthlySalary <= 9250) return 405.00;
        if (monthlySalary <= 9750) return 427.50;
        if (monthlySalary <= 10250) return 450.00;
        if (monthlySalary <= 10750) return 472.50;
        if (monthlySalary <= 11250) return 495.00;
        if (monthlySalary <= 11750) return 517.50;
        if (monthlySalary <= 12250) return 540.00;
        if (monthlySalary <= 12750) return 562.50;
        if (monthlySalary <= 13250) return 585.00;
        if (monthlySalary <= 13750) return 607.50;
        if (monthlySalary <= 14250) return 630.00;
        if (monthlySalary <= 14750) return 652.50;
        if (monthlySalary <= 15250) return 675.00;
        if (monthlySalary <= 15750) return 697.50;
        if (monthlySalary <= 16250) return 720.00;
        if (monthlySalary <= 16750) return 742.50;
        if (monthlySalary <= 17250) return 765.00;
        if (monthlySalary <= 17750) return 787.50;
        if (monthlySalary <= 18250) return 810.00;
        if (monthlySalary <= 18750) return 832.50;
        if (monthlySalary <= 19250) return 855.00;
        if (monthlySalary <= 19750) return 877.50;
        if (monthlySalary <= 20250) return 900.00;
        if (monthlySalary <= 20750) return 922.50;
        if (monthlySalary <= 21250) return 945.00;
        if (monthlySalary <= 21750) return 967.50;
        if (monthlySalary <= 22250) return 990.00;
        if (monthlySalary <= 22750) return 1012.50;
        if (monthlySalary <= 23250) return 1035.00;
        if (monthlySalary <= 23750) return 1057.50;
        if (monthlySalary <= 24250) return 1080.00;
        if (monthlySalary <= 24750) return 1102.50;
        return 1125.00; // Maximum contribution
    }
    
    //PhilHealth Contribution (2023 rates)
    private double calculatePhilHealth(double monthlySalary) {
        // Premium rate is 3% of monthly salary, divided equally between employee and employer
        // Employee pays 1.5% of monthly salary
        if (monthlySalary <= 10000.00) {
            return 150.00;  // Minimum contribution (employee share)
        } else if (monthlySalary < 60000.00) {
            return monthlySalary * 0.015;  // 1.5% for employee share
        } else {
            return 900.00;  // Maximum contribution (employee share)
        }
    }
    
    // Pag-IBIG Contribution (2023 rates)
    private double calculatePagIBIG(double monthlySalary) {
        if (monthlySalary <= 1500.00) {
            return monthlySalary * 0.01; // 1% for ≤1,500
        } else {
            double contribution = monthlySalary * 0.02; // 2% for >1,500
            return Math.min(contribution, 100.00); // Cap at PHP 100
        }
    }
    
    // Withholding Tax
    private double calculateWithholdingTax(double monthlySalary, 
                                         double sss, 
                                         double philhealth, 
                                         double pagibig) {
        double taxableIncome = monthlySalary - (sss + philhealth + pagibig);
        
        if (taxableIncome <= 20833.00) {
            return 0.00;
        } else if (taxableIncome <= 33333.00) {
            return (taxableIncome - 20833.00) * 0.20;
        } else if (taxableIncome <= 66667.00) {
            return 2500.00 + (taxableIncome - 33333.00) * 0.25;
        } else if (taxableIncome <= 166667.00) {
            return 10833.00 + (taxableIncome - 66667.00) * 0.30;
        } else if (taxableIncome <= 666667.00) {
            return 40833.33 + (taxableIncome - 166667.00) * 0.32;
        } else {
            return 200833.33 + (taxableIncome - 666667.00) * 0.35;
        }
    }
    
    
    public void processPayroll(String employeeId, YearMonth month, int weekNumber) {
        Employee employee = fileHandler.findEmployee(employeeId);
        if (employee == null) {
            System.out.println("Employee not found!");
            return;
        }
        
        List<Attendance> records = fileHandler.getAllAttendanceRecords().stream()
                .filter(r -> r.getEmployeeId().equals(employeeId))
                .filter(r -> YearMonth.from(r.getDate()).equals(month))
                .sorted(Comparator.comparing(Attendance::getDate))
                .collect(Collectors.toList());
        
        if (records.isEmpty()) {
            System.out.println("No records found for " + month);
            return;
        }
        
        // Group by week
        Map<Integer, List<Attendance>> weeklyData = records.stream()
                .collect(Collectors.groupingBy(
                    r -> r.getDate().get(WeekFields.ISO.weekOfMonth())
                ));
        
        printPayrollReport(employee, weeklyData, month, weekNumber);
    }
    
    private void printPayrollReport(Employee employee, 
                                  Map<Integer, List<Attendance>> weeklyData,
                                  YearMonth month,
                                  int weekNumber) {
        System.out.println("\n-------------------------------");
        System.out.println("       PAYROLL REPORT");
        System.out.println("-------------------------------");
        System.out.printf("Employee: %s, %s (%s)\n",
            employee.getLastName(),
            employee.getFirstName(),
            employee.getEmployeeId());
        System.out.println("Month: " + month.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        
        if (weekNumber == 0) {
            weeklyData.forEach((week, records) -> printWeekDetails(week, records, employee));
        } else {
            if (weeklyData.containsKey(weekNumber)) {
                printWeekDetails(weekNumber, weeklyData.get(weekNumber), employee);
            } else {
                System.out.println("Invalid week number");
            }
        }
    }
    
    private void printWeekDetails(int weekNumber, List<Attendance> records, Employee employee) {
        // Calculate totals
        double totalRegularHours = 0;
        double totalOvertimeHours = 0;
        int totalLateMinutes = 0;
        
        // Track overtime days to calculate appropriate rates
        Map<LocalDate, Double> overtimeByDay = new HashMap<>();
        Map<Boolean, Double> overtimeHoursByType = new HashMap<>();
        overtimeHoursByType.put(true, 0.0);  // Rest day overtime
        overtimeHoursByType.put(false, 0.0); // Regular day overtime
        
        for (Attendance record : records) {
            totalRegularHours += calculateRegularHours(record.getTimeIn(), record.getTimeOut());
            
            double overtimeHours = calculateOvertimeHours(
                record.getDate(), 
                record.getTimeIn(), 
                record.getTimeOut()
            );
            
            totalLateMinutes += calculateLateMinutes(record.getTimeIn());
            
            // Store overtime hours by day type
            DayOfWeek dayOfWeek = record.getDate().getDayOfWeek();
            boolean isRestDay = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
            
            if (overtimeHours > 0) {
                overtimeByDay.put(record.getDate(), overtimeHours);
                overtimeHoursByType.put(isRestDay, 
                                       overtimeHoursByType.get(isRestDay) + overtimeHours);
            }
        }
        
        // Calculate total overtime hours
        totalOvertimeHours = overtimeHoursByType.get(true) + overtimeHoursByType.get(false);
        
        // Calculate payments
        double regularPay = totalRegularHours * employee.getHourlyRate();
        
        double regularDayOTPay = overtimeHoursByType.get(false) * 
                                employee.getHourlyRate() * 
                                REGULAR_DAY_OT_MULTIPLIER;
        
        double restDayOTPay = overtimeHoursByType.get(true) * 
                             employee.getHourlyRate() * 
                             REST_DAY_OT_MULTIPLIER;
        
        double totalOvertimePay = regularDayOTPay + restDayOTPay;
        
        double lateDeduction = calculateLateDeduction(totalLateMinutes, employee.getHourlyRate());
        
        // Calculate weekly benefits (monthly benefits / 4)
        double weeklyAllowances = (employee.getRiceSubsidy() + employee.getPhoneAllowance() +  employee.getClothingAllowance()) / 4;

        
        // Calculate weekly government deductions
        double weeklySSS = calculateSSS(employee.getBasicSalary()) / 4;
        double weeklyPhilHealth = calculatePhilHealth(employee.getBasicSalary()) / 4;
        double weeklyPagIBIG = calculatePagIBIG(employee.getBasicSalary()) / 4;
        double weeklyWithholdingTax = calculateWithholdingTax(
            employee.getBasicSalary(),
            calculateSSS(employee.getBasicSalary()),
            calculatePhilHealth(employee.getBasicSalary()),
            calculatePagIBIG(employee.getBasicSalary())
        ) / 4;
        
        double weeklyGovernmentDeductions = weeklySSS + weeklyPhilHealth + weeklyPagIBIG + weeklyWithholdingTax;
        
        // Final calculations
        double grossPay = regularPay + totalOvertimePay + weeklyAllowances;
        double totalDeductions = weeklyGovernmentDeductions + lateDeduction;
        double netPay = grossPay - totalDeductions;
        
        // Print report with header lines but simpler format
        System.out.println("\n-------------------------------");
        System.out.printf("Week %d (%s to %s)\n",
            weekNumber,
            records.get(0).getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
            records.get(records.size()-1).getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("-------------------------------");
        
        System.out.printf("Worked Hours: %.2f hours\n", totalRegularHours);
        System.out.printf("Late: %d minutes\n", totalLateMinutes);
        System.out.printf("Overtime: %.2f hours\n", totalOvertimeHours);
        System.out.printf("Allowances: PHP %,.2f\n", weeklyAllowances);
        System.out.printf("Government Deductions: PHP %,.2f\n", weeklyGovernmentDeductions);
        System.out.println("-------------------------------");
        System.out.printf("Gross Weekly Pay: PHP %,.2f\n", grossPay);
        System.out.printf("Total Deductions: PHP %,.2f\n", totalDeductions);
        System.out.printf("Net Weekly Pay: PHP %,.2f\n", netPay);
    }
    
    // Gets available months with attendance records for a specific employee
    public List<YearMonth> getAvailableMonths(String employeeId) {
        return fileHandler.getAllAttendanceRecords().stream()
                .filter(r -> r.getEmployeeId().equals(employeeId))
                .map(r -> YearMonth.from(r.getDate()))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // Gets all available months with attendance records for any employee
    public List<YearMonth> getAllAvailableMonths() {
        return fileHandler.getAllAttendanceRecords().stream()
                .map(r -> YearMonth.from(r.getDate()))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    //Calculates weekly payroll for a specific employee
    public void calculateWeeklyPayroll(String employeeId, YearMonth month, int weekNumber) {
        processPayroll(employeeId, month, weekNumber);
    }

    // Calculates weekly payroll for all employees
    public void calculateAllWeeklyPayroll(YearMonth month, int weekNumber) {
        List<Employee> employees = fileHandler.readEmployees();
        
        if (employees.isEmpty()) {
            System.out.println("No employees found!");
            return;
        }
        
        for (Employee employee : employees) {
            processPayroll(employee.getEmployeeId(), month, weekNumber);
        }
    }
}
