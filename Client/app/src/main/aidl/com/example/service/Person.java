package com.example.service;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 实现Parcelable接口的类，可以用于进程间通信传递数据，
 * 如果放在aidl文件夹下，需要在app/build.gradle的android标签中添加
 * sourceSets {
      main {
        java.srcDirs = ['src/main/java', 'src/main/aidl']
      }
    }
 需要创建同名的aidl文件（不是interface开头，不会类冲突）
 实现Parcelable接口时在报错位置alt + enter可以自动创建相应代码
 */
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

    /**
     * 实现Parcelable接口时不会自动创建此方法，
     * 但如果aidl文件中Person类添加了out或inout标签时必须手动实现此方法
     */
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
}
