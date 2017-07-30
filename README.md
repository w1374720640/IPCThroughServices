本项目介绍了Android系统中通过Service进行进程间通信的两种方式，即Messenger和AIDL，配套博客见

> http://www.jianshu.com/p/bd0532bd08aa

项目包含两个工程，分别为服务端和客户端，提供服务的称为服务端，获取服务的称为客户端。先编译运行服务端代码（即Service工程），再编译运行客户端代码（即Client工程），通过客户端执行进程间通信操作。