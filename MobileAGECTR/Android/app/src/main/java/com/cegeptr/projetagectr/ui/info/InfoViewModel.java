package com.cegeptr.projetagectr.ui.info;

import androidx.lifecycle.ViewModel;

import com.cegeptr.projetagectr.logic.DataSingleton;

public class InfoViewModel extends ViewModel{

    private DataSingleton data = DataSingleton.getInstance();

    private boolean passwordDialogDisplayed;
    private boolean infoDialogDisplayed;
    private boolean avatarDialogDisplayed;

    private int avatar;
    private String firstName;
    private String lastName;
    private String mail;
    private String phoneNumber;
    private String matricule;
    private String oldPassword;
    private String newPassword;
    private String newPasswordConf;

    public InfoViewModel(){
        resetInfoCache();
        restPasswordCache();
    }

    public void resetInfoCache(){
        avatar = data.getConnectedUser().getAvatar();
        firstName = data.getConnectedUser().getFirstName();
        lastName = data.getConnectedUser().getLastName();
        mail = data.getConnectedUser().getEmail();
        String phoneChache = data.getConnectedUser().getPhoneNumber();

        if (phoneChache!=null && phoneChache.length() >= 10) {
            phoneNumber = "(" + phoneChache.substring(0, 3) + ")-" + phoneChache.substring(3, 6) + " " + phoneChache.substring(6);
        }
        else{
            phoneNumber= "";
        }
        matricule = data.getConnectedUser().getMatricule();
    }

    public void restPasswordCache(){
        oldPassword = "";
        newPassword = "";
        newPasswordConf = "";
    }

    public boolean isPasswordDialogDisplayed() {
        return passwordDialogDisplayed;
    }

    public void setPasswordDialogDisplayed(boolean passwordDialogDisplayed) {
        this.passwordDialogDisplayed = passwordDialogDisplayed;
    }

    public boolean isInfoDialogDisplayed() {
        return infoDialogDisplayed;
    }

    public void setInfoDialogDisplayed(boolean infoDialogDisplayed) {
        this.infoDialogDisplayed = infoDialogDisplayed;
    }

    public boolean isAvatarDialogDisplayed() {
        return avatarDialogDisplayed;
    }

    public void setAvatarDialogDisplayed(boolean avatarDialogDisplayed) {
        this.avatarDialogDisplayed = avatarDialogDisplayed;
    }

    public DataSingleton getData() {
        return data;
    }

    public void setData(DataSingleton data) {
        this.data = data;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}