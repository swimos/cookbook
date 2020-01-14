# Egress Bridges（出口网桥）

代码可在 [egress bridges cookbook](https://swim.dev/tutorials/egress-bridges/) 中找到。

*其他语言版本：[English](README.md), [简体中文](README.zh-cn.md)*

## 展示动机

回忆两种 push-type egress bridge（出口网桥）到非Swim sink的设计：

1. *独立*网络代理通过 *non-blocking* 驱动写入sink

2. *任意*网络代理通过 *thread-safe, non-blocking* 驱动写入sink

在此举例中sink指代的是 relational H2 database。Oracle 提供了 [Asynchronous Database Acesss](https://blogs.oracle.com/java/jdbc-next:-a-new-asynchronous-api-for-connecting-to-a-database) (ADBA) API，完成了 [ADBA over JDBC](https://github.com/oracle/oracle-db-examples/tree/master/java/AoJ)的一部分。在此repository中的 `com.oracle.adbaoverjdbc` 包都是直接从API中读取的；只在目录构建中做了改动以匹配Gradle需求。

我们拼凑了需要的部分制作了我们自己的 non-blocking `CustomDriver`。为了保证实现的简单化，`CustomDriver` 不是 thread-safe。因此在此举例中我们只演示了方法2。

## 运行 egress bridge 展示

该展示要求按照顺序依次运行 `swim.grade.db.Database`，`swim.grade.GradePlane`，和 `swim.grade.Sim` 类。回顾[`父目录中的README.zh-cn.md`](../README.zh-cn.md)教程可知如何进行运行。对于选择了选项2的用户，`gradle` task中对应 `swim.grade.db.Database` 的名称为 `runDb`，`swim.grade.Sim` 对应的名称是 `runSim`。

## 停止运行 egress bridge 展示

按照倒序关闭之前提到的三个运行程序。在 `GradePlane` 即将退出之前，程序会从数据库中读取每个学生的信息；用户可以确认到读取的信息匹对每个 `StudentAgent` 的最终log。