# CCP_CarSwipeCard
长春化工车载NFC蓝牙刷卡App

# 10.30
准备完善出门部分的逻辑。


# 11.1
* 完成竖屏展示
* MQTT断线重连
* 修补更新的bug
* 使用签名过后的release 版本
* 先从本地缓存读取数据。如果没有再去请求。


# 11.2
* 将员工数据库和每天的数据库分离
* 增加语音提示
* 增加五秒不允许重刷 提示。

# 11.6
* 定位的代码重复了，删掉重复的了。

# 11.7
* 增加上传历史记录到阿里云OSS上

# 11.8
* 数据在每次司机点击结束行程的时候会上传数据。

# 11.9
* 蓝牙绑定
* 本地的列表
* 发布1.0.4版本


# 11.10
* 1.0.5
* 优化MQTT断线重连。
* 结束时上传版本号

# 11.11
* 蓝牙设备断线重连
* 解决token失效问题，随时用随时获取。
* 使用Toasty 作为toast
* 解决上传报表的问题。什么时候就上传不了

# 11.12
* 解决OSS token 超时上传文件不了的问题。
* 重新用paho mqtt 重写了MQTT 他的重连机制。
* 发布release 1.0.6 版本。


* 11.13
* 解决OSS 上传时间，文件名不正确的问题。

# TODO
