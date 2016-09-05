package ru.strela.util.validate;

import org.springframework.validation.BindingResult;

public class BindingResultValidateAdapter implements IValidateResult {

    private BindingResult result;

    public BindingResultValidateAdapter(BindingResult result) {
        this.result = result;
    }

    @Override
    public void addError(String fieldName, String message) {
        if (result != null) {
            result.rejectValue(fieldName, "field.required", message);
        }
    }

    @Override
    public boolean hasErrors() {
        if (result != null) {
            return result.hasErrors();
        }

        return false;
    }

}
