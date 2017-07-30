// RemoteAidlInterface.aidl
package com.example.service;

//即使在同一个包下，也需要手动导入类
import com.example.service.ClientCallback;
import com.example.service.Person;

//编译后生成的Java文件在app/build/generated/source/aidl/dubug/<packagename>目录下
//服务端创建，客户端调用
interface RemoteInterface {

//    根据用户ID获取用户信息
    Person getPersonById(int id);

//    添加用户
    void addPerson(in Person person);

//    向服务端注册监听
    void registClientCallback(ClientCallback callback);

//    取消注册
    void unRegistClientCallback(ClientCallback callback);
}
