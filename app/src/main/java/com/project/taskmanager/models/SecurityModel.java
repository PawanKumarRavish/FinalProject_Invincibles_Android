package com.project.taskmanager.models;

/**
 * Created by Gaganjot Singh on 16/08/2019.
 */

public class SecurityModel {

    String securityQuestions;

    public SecurityModel(String securityQuestions) {
        this.securityQuestions = securityQuestions;
    }

    public String getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(String securityQuestions) {
        this.securityQuestions = securityQuestions;
    }
}
