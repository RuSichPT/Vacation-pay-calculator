package com.github.RuSichPT.Vacationpaycalculator.service;

public interface VacationPayService {
    double calculateVacationPay(double avgSalary, int numDays);
    double calculateVacationPay(double avgSalary, int numDays, String startDate);
}
