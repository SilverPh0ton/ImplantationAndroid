package com.cegeptr.projetagectr.logic.Entity;

public class ServerResponse {

    private Boolean succes;
    private String message;

    public ServerResponse(Boolean succes, String message) {
        this.succes = succes;
        this.message = message;
    }


    public Boolean getSucces() {
        return succes;
    }

    public void setSucces(Boolean succes) {
        this.succes = succes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
