package com.example.service;


import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable{
    private int id;
    private String name;
    private int age;
    private String phone;

    public Person(){

    }

    protected Person(Parcel in) {
        id = in.readInt();
        name = in.readString();
        age = in.readInt();
        phone = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(phone);
    }

    public void readFromParcel(Parcel dest) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        id = dest.readInt();
        name = dest.readString();
        age = dest.readInt();
        phone = dest.readString();
    }

    @Override
    public String toString() {
        return "\"id=" + id + ",name=" + name + ",age=" + age + ",phone=" + phone + "\"";
    }
}
