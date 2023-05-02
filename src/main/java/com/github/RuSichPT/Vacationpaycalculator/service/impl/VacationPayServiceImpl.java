package com.github.RuSichPT.Vacationpaycalculator.service.impl;

import com.github.RuSichPT.Vacationpaycalculator.client.IsDayOffClient;
import com.github.RuSichPT.Vacationpaycalculator.service.VacationPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class VacationPayServiceImpl implements VacationPayService {
    private final IsDayOffClient isDayOffClient;
    public static final double AVG_NUM_DAYS_MONTH = 29.3;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${date1}")
    private String rescheduledWeekend1;

    @Value("${date2}")
    private String rescheduledWeekend2;

    public VacationPayServiceImpl(IsDayOffClient isDayOffClient) {
        this.isDayOffClient = isDayOffClient;
    }

    @Override
    public double calculateVacationPay(double avgSalary, int numDays) {
        if (numDays < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "numDays must be >= 0");
        }
        return Math.round(avgSalary / AVG_NUM_DAYS_MONTH * numDays);
    }

    @Override
    public double calculateVacationPay(double avgSalary, int numDays, String startDate) {
        LocalDate start;
        try {
            start = LocalDate.parse(startDate, FORMATTER);
        } catch (DateTimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is not correctly. Pattern mus be yyyy-MM-dd", ex);
        }
        int resultDays = numDays;
        for (int i = 0; i < numDays; i++) {
            if (isOnlyHoliday(start)) {
                resultDays--;
            }
            start = start.plusDays(1);
        }

        log.info("Calculation days: " + resultDays);
        return calculateVacationPay(avgSalary, resultDays);
    }

    private boolean isOnlyHoliday(LocalDate date) {
        if (isAlwaysHoliday(date)) {
            log.info(date + " " + date.getDayOfWeek() + " is always holiday");
            return true;
        } else if (isRescheduledWeekend(date)) {
            log.info(date + " " + date.getDayOfWeek() + " is rescheduled weekend");
            return false;
        }

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);

        return isDayOffClient.isDayOff(date) && !isWeekend;
    }

    private boolean isAlwaysHoliday(LocalDate date) {
        int currentYear = LocalDate.now().getYear();
        LocalDate exceptionDaysStart = LocalDate.of(currentYear, 1, 1);
        LocalDate exceptionDaysEnd = LocalDate.of(currentYear, 1, 8);

        return date.compareTo(exceptionDaysStart) >= 0 && date.compareTo(exceptionDaysEnd) <= 0;
    }

    private boolean isRescheduledWeekend(LocalDate date) {
        try {
            LocalDate day1 = LocalDate.parse(rescheduledWeekend1, FORMATTER);
            LocalDate day2 = LocalDate.parse(rescheduledWeekend2, FORMATTER);
            return date.isEqual(day1) || date.isEqual(day2);
        } catch (DateTimeException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Date is not correctly. Pattern mus be yyyy-MM-dd", ex);
        }
    }
}
