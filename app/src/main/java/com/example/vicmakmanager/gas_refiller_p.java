package com.example.vicmakmanager;

public class gas_refiller_p {
    String gas_name, gas_weight, cylinder_number;

    public gas_refiller_p(String gas_name, String gas_weight, String cylinder_number) {
        this.gas_name = gas_name;
        this.gas_weight = gas_weight;
        this.cylinder_number = cylinder_number;
    }

    public String getGas_name() {
        return gas_name;
    }

    public void setGas_name(String gas_name) {
        this.gas_name = gas_name;
    }

    public String getGas_weight() {
        return gas_weight;
    }

    public void setGas_weight(String gas_weight) {
        this.gas_weight = gas_weight;
    }

    public String getCylinder_number() {
        return cylinder_number;
    }

    public void setCylinder_number(String cylinder_number) {
        this.cylinder_number = cylinder_number;
    }
}
