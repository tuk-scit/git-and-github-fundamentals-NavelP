package com.example.vicmakmanager.Gas;

public class gas_cylinders_p {
    public String gas_name, gas_weight, total_gas_cylinder_number, gas_cylinder_empty, gas_cylinder_full, exchanged, total_commission, cylinder_commission;
    public int gas_image;

    public gas_cylinders_p(String gas_name, String gas_weight, String total_gas_cylinder_number, String gas_cylinder_empty,
                           String gas_cylinder_full, int gas_image, String exchanged, String total_commission, String cylinder_commission) {
        this.gas_name = gas_name;
        this.gas_weight = gas_weight;
        this.total_gas_cylinder_number = total_gas_cylinder_number;
        this.gas_cylinder_empty = gas_cylinder_empty;
        this.gas_cylinder_full = gas_cylinder_full;
        this.gas_image = gas_image;
        this.total_commission = total_commission;
        this.exchanged = exchanged;
        this.cylinder_commission = cylinder_commission;
    }

    public String getExchanged() {
        return exchanged;
    }

    public void setExchanged(String exchanged) {
        this.exchanged = exchanged;
    }

    public String getTotal_commission() {
        return total_commission;
    }

    public void setTotal_commission(String total_commission) {
        this.total_commission = total_commission;
    }

    public String getCylinder_commission() {
        return cylinder_commission;
    }

    public void setCylinder_commission(String cylinder_commission) {
        this.cylinder_commission = cylinder_commission;
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

    public String getTotal_gas_cylinder_number() {
        return total_gas_cylinder_number;
    }

    public void setTotal_gas_cylinder_number(String total_gas_cylinder_number) {
        this.total_gas_cylinder_number = total_gas_cylinder_number;
    }

    public String getGas_cylinder_empty() {
        return gas_cylinder_empty;
    }

    public void setGas_cylinder_empty(String gas_cylinder_empty) {
        this.gas_cylinder_empty = gas_cylinder_empty;
    }

    public String getGas_cylinder_full() {
        return gas_cylinder_full;
    }

    public void setGas_cylinder_full(String gas_cylinder_full) {
        this.gas_cylinder_full = gas_cylinder_full;
    }

    public int getGas_image() {
        return gas_image;
    }

    public void setGas_image(int gas_image) {
        this.gas_image = gas_image;
    }
}
