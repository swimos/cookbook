# Ingress Bridges （入口网桥）

代码可在 [Ingress Bridges cookbook](https://swimos.org/tutorials/ingress-bridges/) 中找到。

*其他语言版本：[English](README.md), [简体中文](README.zh-cn.md)*

## 展示动机

回忆Ingress Bridge的高层设计：

1. 数据源*推送*数据，可能先经过中介传递程序，通过 Swim client command/downlinks 发送到 Swim server。

2. 数据源*推送*数据，可能先经过中介传递程序，通过 websocket messages 发送到 Swim server。

3. 客户数据源也是另一个Swim server，以及客户的“主”server从正在使用中的downlinks中*获取*数据。

4. 客户数据源只能通过非WARP protocols才能读取，所以一个第二程序是从客户数据源中*获取*数据，然后通过 Swim client commands/downlinks 或者 websocket meesage 将数据传输到Swim server。

前两个设计相对来说简单一些，特别是他们有单独的相关cookbook snippets，因此在此没有列举相关的例子。

后两个设计会比较复杂一些，所以为了能够促进理解，我们建了一个例子。我们的 Swim server 还像之前一样保留 `BasicPlane` 和简单的 `UnitAgents`，但现在用两个 ingress bridge 中的一个来填充数据而不是 `CustomClient` 了。

## WARP Ingress Bridge

此展示相对应设计3，并且与之相关的源大部分可以在 `swim.basic.warp` 包中找到。

`swim.basic.warp.SourcePlane` 是一个非常简单的 Swim 应用，该应用用数据打开了最少10个 `SourceAgents`。

`swim.basic.UnitAgents` 必须意识到数据是如何存储在 `SoucePlane` 中的，并且每一个 `UnitAgent` 都创造了相对应的 downlink。

### 运行 WARP Ingress Bridge 展示

此展示要求依次运行 `swim.basic.warp.SourcePlane` 类和 `swim.basic.BasicPlane` 类。回顾[`父目录中的README.zh-cn.md`](../README.zh-cn.md)教程可知如何进行运行。对于选择了选项2的用户，`gradle` task中对应 `swim.basic.warp.SourcePlane` 的名称为 `runWarp`。

注意：客户可以选择在不同的机器上运行这些程序，只需要将 `UnitAgent` 上的 downlink 逻辑（`hostUri` 部分）按照机器在互相可见的网络中的IP进行改动即可。

## MQTT Ingress Bridge

此展示相对应设计4，并且与之相关的源大部分可以在 `swim.basic.mqtt` 包中找到。

为了展示 MQTT 的整合，我们的数据必须存储在 MQTT server上。为避免手动建了 MQTT server，cookbook 代码使用了 [公开可用的 MQTT brokers](https://github.com/mqtt/mqtt.github.io/wiki/public_brokers) 其中的一个 broker。该 broker 可被替换成客户想用的 broker。

我们使用了 Eclipse Paho Java Client](https://github.com/eclipse/paho.mqtt.java) 来作为 MQTT client 运算；相对应的 dependency 记录在了 `build.gradle`。

`swim.basic.mqtt.DataSourcePopulator` 用 MQTT 发送了以`swimSensors/all`为topic，格式为 `@msg{id:%d,val:%s}` 的字符串到之前提到的broker。

`swim.basic.mqtt.IngressBridge` 接收该 topic，然后在使用 Swim command 进行少量形变后转送所有收到的信息到正确的目的地。同样地，我们也可以用正确格式的 websocket message 来发送；这是唯一的在使用除 Java 和 JavaScript 之外的语言的时候的方法。

### 运行 MQTT Ingress Bridge 展示

该演示需要按照顺序依次运行 `swim.basic.warp.SourcePlane`，`swim.basic.mqtt.DataSourcePopulator`，和 `swim.basic.mqtt.IngressBridge` 类。回顾[`父目录中的README.zh-cn.md`](../README.zh-cn.md)教程可知如何进行运行。对于选择了选项2的用户，`gradle` task中对应 `swim.basic.mqtt.DataSourcePopulator` 的名称为 `runMqtt`，`swim.basic.mqtt.IngressBridge` 相对应的名称是 `runBridge`。