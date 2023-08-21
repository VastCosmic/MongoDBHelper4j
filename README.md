# MongoDBHelper4j

A MongoDBHelper with morphia for java.

## 简介

该项目提供了一个简单的MongoDB辅助工具，并提供了包括且不限于多线程存储与读取的优化方法。

### 性能测试

一次存储50000条（5万）假数据，时间约为4ms左右；

一次存储10000000条（1千万条）假数据，时间约为180ms左右；

读取仍在计划开发中。

#### 测试机器
（该性能可以通过自行设定线程数与batchSize进行额外可能的优化）

本地MongoDB

CPU: 12th_Gen_Intel(R)_Core(TM)_i5-12400

内存：16*2 GB ddr4 3200Mhz

**************************************

VastCosmic

Site：https://site.vastcosmic.cn

Email：lhyu7677@gmail.com

**************************************
