package ru.strela.mail;


public interface SendMailHelper {

    void sendRecoveryMail(String name, String email, String recoveryCode);
}
