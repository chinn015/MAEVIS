package com.user.maevis.models;

public class DeviceModel {
    public String deviceToken;
    public String deviceUser;

    public DeviceModel(String deviceToken, String deviceUser) {
        this.deviceToken = deviceToken;
        this.deviceUser = deviceUser;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceUser() {
        return deviceUser;
    }

    public void setDeviceUser(String deviceUser) {
        this.deviceUser = deviceUser;
    }
}
