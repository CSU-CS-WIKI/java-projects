# JAVA聊天室

> 写了两个版本的聊天室。
>
> `PP`:
>
> - 用户间的聊天会在弹窗中进行
> - 原始界面，无色彩
> - Mac，windows均流畅运行
>
> `PP-无弹窗`：
>
> - 后来实验老师改了要求，说聊天室就是聊天室，不能有弹窗（？？）因此这个是最终提交给老师检查的版本
> - 用户间的聊天都在总的界面中进行
> - 进行了一定的美化
> - 界面在Mac电脑中显示有点问题
>
> **使用说明**：先运行`Server.java`，然后运行`Client.java`（若第一次登录，则密码自定义就行）
>
> **开发环境**：JDK11，IntelliJ IDEA

## PP

### 功能

1. 双击`用户名`称出现弹窗，进行私聊

2. 双击`创建群聊`，可选择用户进行群聊


3. 服务器管理员可与用户私聊信息

4. 服务器可强制下线用户

5. 用户上线、下线时所有在线用户会受到消息

   

## PP-无弹窗

1. 登录界面：考虑密码错误、注册时已有重复用户
2. 客户端：
   - 用户上线广播
   - 聊天室内广播消息
   - 私聊用户
   - 创建群聊

3. 服务器端：
   - 管理员向用户广播消息
   - 管理员私聊用户
   - 管理员强制下线用户