package project.SPM.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import project.SPM.dto.PapagoSmsDto;


@Slf4j
@Component
public class PapagoSmsValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PapagoSmsDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        PapagoSmsDto papagoSmsDto = (PapagoSmsDto) target;

        if (!StringUtils.hasText(papagoSmsDto.getPhoneNm())) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phoneNm", "label");
        }

        if (!StringUtils.hasText(papagoSmsDto.getMessage())) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message", "label");
        }
    }
}
