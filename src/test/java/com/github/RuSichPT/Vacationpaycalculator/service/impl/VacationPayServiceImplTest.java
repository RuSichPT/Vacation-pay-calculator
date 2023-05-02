package com.github.RuSichPT.Vacationpaycalculator.service.impl;

import com.github.RuSichPT.Vacationpaycalculator.client.IsDayOffClient;
import com.github.RuSichPT.Vacationpaycalculator.client.impl.IsDayOffClientImpl;
import com.github.RuSichPT.Vacationpaycalculator.service.VacationPayService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import static com.github.RuSichPT.Vacationpaycalculator.service.impl.VacationPayServiceImpl.AVG_NUM_DAYS_MONTH;


class VacationPayServiceImplTest {

    private final IsDayOffClient client = new IsDayOffClientImpl();
    private final VacationPayService vacationPayService = new VacationPayServiceImpl(client);

    @Test
    void shouldProperlyCalculateVacationPay() {
        // given
        double avgSalary = 70000;
        int numDays = 7;
        double actual = Math.round(avgSalary / AVG_NUM_DAYS_MONTH * numDays);

        // when
        double expected = vacationPayService.calculateVacationPay(avgSalary, numDays);

        // then
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest()
    @CsvSource({
        "2023-05-07, 7, 1",
        "2023-01-01, 10, 8"
    })
    void shouldProperlyCalculateVacationPayWithStartDate(String start, int numDays, int holidays){
        // given
        double avgSalary = 70000;
        ReflectionTestUtils.setField(vacationPayService, "rescheduledWeekend1", "2023-02-24");
        ReflectionTestUtils.setField(vacationPayService, "rescheduledWeekend2", "2023-05-08");
        double expected = vacationPayService.calculateVacationPay(avgSalary, numDays, start);

        // when
        double actual = vacationPayService.calculateVacationPay(avgSalary, numDays - holidays);

        // then
        Assertions.assertEquals(expected, actual);
    }

}