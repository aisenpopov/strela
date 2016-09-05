package ru.strela.service;

import ru.strela.model.Athlete;
import ru.strela.util.validate.IValidateResult;

public interface AthleteService {

    public Athlete save(Athlete athlete);

    public boolean validate(Athlete athlete, IValidateResult validateResult);

    public void initNew(Athlete athlete);

}
