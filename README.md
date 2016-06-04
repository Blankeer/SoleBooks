# SoleBook读书交流APP

LeanCloud、MVP、RxJava

## 截图
![login](https://github.com/Blankeer/SoleBooks/blob/master/image/login.gif?raw=true)![details](https://github.com/Blankeer/SoleBooks/blob/master/image/details.gif?raw=true)![random](https://github.com/Blankeer/SoleBooks/blob/master/image/rondom.gif?raw=true)

![img](https://github.com/Blankeer/SoleBooks/blob/master/image/bangdan.png?raw=true)![img](https://github.com/Blankeer/SoleBooks/blob/master/image/book_1.png?raw=true)![img](https://github.com/Blankeer/SoleBooks/blob/master/image/book_2.png?raw=true)![img](https://github.com/Blankeer/SoleBooks/blob/master/image/nav.png?raw=true)![img](https://github.com/Blankeer/SoleBooks/blob/master/image/nav_2.png?raw=true)![img](https://github.com/Blankeer/SoleBooks/blob/master/image/nearmap.png?raw=true)![img](https://github.com/Blankeer/SoleBooks/blob/master/image/random.png?raw=true)![img](https://github.com/Blankeer/SoleBooks/blob/master/image/tags.png?raw=true)![img](https://github.com/Blankeer/SoleBooks/blob/master/image/userhome.png?raw=true)

## 项目描述
### 数据来源
数据来源于豆瓣，数据获取采用爬虫（豆瓣api有限制）
，包含书籍信息，榜单(热门榜、新书榜、TOP250)信息

### 数据存储
数据存储采用LeanCloud服务，使用了Node.js云引擎

### 用户管理
采用第三方（QQ/微博）登录服务，自己不做用户管理（注册/登录/找回密码等）
，降低用户使用成本，打开APP用第三方登录即可使用。

### 附近的人
使用高德地图服务，将距离当前位置最近的10个用户显示到地图上。

