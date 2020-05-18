package com.cegeptr.projetagectr.ui.connectionHeader;

import androidx.lifecycle.ViewModel;

public class ConnectionHeaderViewModel extends ViewModel {

    private boolean loginDialogDisplayed;
    private String newEmail;
    private String password;
    private boolean keepConnected;

    private boolean createAccountDialogDisplayed;
    private String firstName;
    private String lastName;
    private String email;
    private String newPassword;
    private String newPasswordConf;
    private String phoneNumber;
    private String matricule;
    private boolean condition;

    private boolean resetPasswordDialogDisplayed;
    private String emailForReset;

    public ConnectionHeaderViewModel(){
        resetLoginCache();
        resetCreatAcountCache();
        resetResetPasswordCache();
    }

    public void resetLoginCache(){
        email = "";
        password = "";
        keepConnected = false;
    }

    public void resetResetPasswordCache(){
        emailForReset = "";
    }

    public void resetCreatAcountCache(){
        firstName = "";
        lastName = "";
        newEmail = "";
        newPassword = "";
        newPasswordConf = "";
        phoneNumber = "";
        matricule = "";
        condition = false;
    }

    public boolean isLoginDialogDisplayed() {
        return loginDialogDisplayed;
    }

    public void setLoginDialogDisplayed(boolean loginDialogDisplayed) {
        this.loginDialogDisplayed = loginDialogDisplayed;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isKeepConnected() {
        return keepConnected;
    }

    public void setKeepConnected(boolean keepConnected) {
        this.keepConnected = keepConnected;
    }

    public boolean isCreateAccountDialogDisplayed() {
        return createAccountDialogDisplayed;
    }

    public void setCreateAccountDialogDisplayed(boolean createAccountDialogDisplayed) {
        this.createAccountDialogDisplayed = createAccountDialogDisplayed;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConf() {
        return newPasswordConf;
    }

    public void setNewPasswordConf(String newPasswordConf) {
        this.newPasswordConf = newPasswordConf;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public boolean isConditionChecked() {
        return condition;
    }

    public void setConditionChecked(boolean condition) {
        this.condition = condition;
    }

    public String getEmailForReset() {
        return emailForReset;
    }

    public void setEmailForReset(String emailForReset) {
        this.emailForReset = emailForReset;
    }

    public boolean isResetPasswordDialogDisplayed() {
        return resetPasswordDialogDisplayed;
    }

    public void setResetPasswordDialogDisplayed(boolean resetPasswordDialogDisplayed) {
        this.resetPasswordDialogDisplayed = resetPasswordDialogDisplayed;
    }
}
