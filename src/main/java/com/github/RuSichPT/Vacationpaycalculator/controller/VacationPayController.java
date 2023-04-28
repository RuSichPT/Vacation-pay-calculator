package com.github.RuSichPT.Vacationpaycalculator.controller;

import com.github.RuSichPT.Vacationpaycalculator.service.VacationPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/")
public class VacationPayController {

    private final VacationPayService vacationPayService;

    public VacationPayController(VacationPayService vacationPayService) {
        this.vacationPayService = vacationPayService;
    }

    @GetMapping({"calculate/{meanSalary}/{numDays}", "calculate/{meanSalary}/{numDays}/{startDate}"})
    public double getVacationPay(@PathVariable double meanSalary, @PathVariable int numDays,
                                 @PathVariable(required = false) String startDate) {
        if (startDate != null) {
            return vacationPayService.calculateVacationPay(meanSalary, numDays, startDate);
        }
        return vacationPayService.calculateVacationPay(meanSalary, numDays);
    }
}
