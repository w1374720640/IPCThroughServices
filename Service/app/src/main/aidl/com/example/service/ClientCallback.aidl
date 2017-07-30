// ClientAidlCallback.aidl
package com.example.service;

//编译后生成的Java文件在app/build/generated/source/aidl/dubug/<packagename>目录下
//客户端向服务端注册，客户端创建，服务端调用
interface ClientCallback {

//    启动客户端
    void start();

//    停止客户端
    void stop();
}
