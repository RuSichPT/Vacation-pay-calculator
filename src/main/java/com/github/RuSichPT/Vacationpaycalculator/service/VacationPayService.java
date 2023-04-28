package com.github.RuSichPT.Vacationpaycalculator.service;

public interface VacationPayService {
    double calculateVacationPay(double meanSalary, int numDays);
    double calculateVacationPay(double meanSalary, int numDays, String startDate);
}
