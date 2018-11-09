package br.com.stanzione.gigigotest.data;

import com.google.gson.JsonObject;

public class CardInformation {

    private String number;
    private String name;
    private String valid;
    private String cvv;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("card_number", number);
        jsonObject.addProperty("card_name", name);
        jsonObject.addProperty("card_valid", valid);
        jsonObject.addProperty("card_cvv", cvv);
        return jsonObject;
    }
}
