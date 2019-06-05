package com.etindahan;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserGetterSetter {

    private String uid,user_type,first_name,last_name,email,contact_number,address;
    private String shop_name,owner_id;
    private Float latitude,longitude;

    public UserGetterSetter() {

    }

    public UserGetterSetter(String shop_name, String address, String uid) {
        this.uid = uid;
        this.shop_name = shop_name;
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //GET SHOP FOR HOME/MAIN FRAGMENT

    public String getshop_name() { return shop_name; }

    public void setshop_mame(String shop_mame) { shop_mame = shop_mame; }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("shop_name",shop_name);

        return result;
    }

    //

    //MAP SETTER
    public Float getLatitude() { return latitude; }

    public void setLatitude(Float latitude) { this.latitude = latitude; }

    public Float getLongitude() { return longitude; }

    public void setLongitude(Float longitude) { this.longitude = longitude; }
    //


    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
}
