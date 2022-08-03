# 海鸥云资产管理系统

### 公司介绍
盒木科技专注于仓储物流和生产制造行业数字化系统的研发和实施。海鸥云是盒木科技自主研发的软件产品系列,目前已发布海鸥云仓库管理系统WMS，仓库控制系统WCS,生产执行系统MES，进销存ERP及资产管理系统。欢迎合作伙伴，代理商或者客户微信扫描下方客户经理二维码或电话18761717855体验咨询。
![微信](https://gitee.com/haioucloud/erp/raw/master/img/%E5%BE%AE%E4%BF%A1.png)

#### 介绍
开源资产管理系统，采用前后端分离技术，api使用restful协议，方便二次开发，后端使用Python，Django，DRF等技术，前端代码使用AntD进行构建，包含资产领用，退库，借用，归还，调拨，维修等流程。移动端使用Uniapp，包含RFID资产盘点等功能。
* Gitee地址: [Gitee](https://gitee.com/haioucloud/ams)
* Demo地址: [Demo](http://114.218.158.78:14448/) &nbsp;&nbsp;公司编号: admin  测试帐号：admin  密码：admin

### 使用前须知
* 软件开放源码(发行协议:GPL-3.0)，个人用户可免费学习使用，但禁止任何单位或个人修改软件后再次发行的行为。商业使用需得到我司授权，否则我们将通过法律途径解决侵权问题。
* 我们欢迎对开源技术感兴趣的朋友一起加入到我们项目中来完善系统功能并为客户提供服务。欢迎扫描下方二维码添加技术交流群，添加时请备注来意

   ![微信群](https://gitee.com/haioucloud/erp/raw/master/img/%E5%BE%AE%E4%BF%A1%E7%BE%A4.png)
   
### 硬件要求及开发环境
* 移动端RFID扫描功能需指定型号PDA，请联系作者购买
* Python版本为V3.9+
* Django版本为V3.2+
* Django-rest-framework版本为V3.12+
* Vue版本为2.6+
* PDA端使用Uniapp
* 数据库为MySQL
* 前端组件为AntD
* 其他Python包可参考requirements.txt文件

### 搭建运行环境
* pip install -r requirements.txt
* cd frontend  #进入frontend文件夹
* npm install -g @vue/cli  #安装vue脚手架
* npm install  #安装依赖包

### 本地运行
1. 启动后端服务
    python manage.py runserver
2. 启动前端服务
    npm run serve
3. 浏览器访问前端地址

### PDA界面截图
![PDA RFID盘点界面](https://gitee.com/haioucloud/ams/raw/master/img/RFID%E7%9B%98%E7%82%B9.JPG)

### PC界面截图
![基础数据](https://gitee.com/haioucloud/ams/raw/master/img/%E5%9F%BA%E7%A1%80%E6%95%B0%E6%8D%AE.JPG)
![资产管理](https://gitee.com/haioucloud/ams/raw/master/img/%E8%B5%84%E4%BA%A7%E7%AE%A1%E7%90%86.JPG)
![统计报表](https://gitee.com/haioucloud/ams/raw/master/img/%E7%BB%9F%E8%AE%A1%E6%8A%A5%E8%A1%A8.JPG)
![记录查询](https://gitee.com/haioucloud/ams/raw/master/img/%E8%AE%B0%E5%BD%95%E6%9F%A5%E8%AF%A2.JPG)

