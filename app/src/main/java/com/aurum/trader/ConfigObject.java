package com.aurum.trader;

public class ConfigObject {
    private String configKey;
    private String configValue;

    public ConfigObject() {
    }

    public ConfigObject(String str, String str2) {
        this.configKey = str;
        this.configValue = str2;
    }

    public ConfigObject(String str, boolean z) {
        this.configKey = str;
        this.configValue = String.valueOf(z);
    }

    public String getConfigKey() {
        return this.configKey;
    }

    public void setConfigKey(String str) {
        this.configKey = str;
    }

    public String getConfigValue() {
        return this.configValue;
    }

    public boolean getConfigValueAsBool() {
        return Boolean.parseBoolean(this.configValue);
    }

    public void setConfigValue(String str) {
        this.configValue = str;
    }
}
