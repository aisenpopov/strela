package ru.strela.util.validate;

public interface IValidateResult {

    public void addError(String fieldName, String message);

    public boolean hasErrors();

}
