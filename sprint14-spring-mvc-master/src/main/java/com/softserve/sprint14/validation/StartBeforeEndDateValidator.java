package com.softserve.sprint14.validation;

import com.softserve.sprint14.entity.Sprint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class StartBeforeEndDateValidator implements ConstraintValidator<StartBeforeEndDateValidation, Sprint> {

    @Override
    public void initialize(StartBeforeEndDateValidation annotation) {
    }

    @Override
    public boolean isValid(Sprint bean, ConstraintValidatorContext context) {
        final LocalDate startDate = bean.getStartDate();
        final LocalDate endDate = bean.getFinishDate();

        return !startDate.isAfter(endDate);
    }
}
