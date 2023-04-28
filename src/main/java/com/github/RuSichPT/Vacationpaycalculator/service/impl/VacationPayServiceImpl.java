package com.github.RuSichPT.Vacationpaycalculator.service.impl;

import com.github.RuSichPT.Vacationpaycalculator.client.IsDayOffClient;
import com.github.RuSichPT.Vacationpaycalculator.service.VacationPayService;
import lombok.extern.slf4j.Slf4j;
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
    private final double AVG_NUM_DAYS_MONTH = 29.3;

    public VacationPayServiceImpl(IsDayOffClient isDayOffClient) {
        this.isDayOffClient = isDayOffClient;
    }

    @Override
    public double calculateVacationPay(double avgSalary, int numDays) {
        return Math.round(avgSalary / AVG_NUM_DAYS_MONTH * numDays);
    }

    @Override
    public double calculateVacationPay(double avgSalary, int numDays, String startDate) {
        LocalDate start;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            start = LocalDate.parse(startDate, formatter);
        } catch (DateTimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is not correctly. Pattern dd-MM-yyyy", ex);
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
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;

        return isDayOffClient.isDayOff(date) && !isWeekend;
    }
}
