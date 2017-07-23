// RemoteAidlInterface.aidl
package com.example.service;

import com.example.service.ClientCallback;
import com.example.service.Person;

interface RemoteInterface {
    Person getPersonById(int id);

    void addPerson(in Person person);

    void registClientCallback(ClientCallback callback);

    void unRegistClientCallback(ClientCallback callback);
}
