# Ingress Bridges

Code corresponding to the [Ingress Bridges cookbook](https://swim.dev/tutorials/ingress-bridges/).

## Demo Motivation

Recall the high-level designs of ingress bridges:

1. A data source *pushes* data, possibly first through intermediary relay processes, to your Swim server through Swim client commands/downlinks

2. A data source *pushes* data, possibly first through intermediary relay processes, to your Swim server via websocket messages

3. Your data source is yet another Swim server, and your "main" server *pulls* data from it using downlinks

4. Your data source is reachable only through non-WARP protocols, so a secondary process *pulls* data from your data source, then relays this data to the Swim server, either via Swim client commands/downlinks or via websocket messages

The first two designs are relatively simple, especially with the relevant standalone cookbook snippets, and do not have corresponding examples here.

The last two are trickier, so we have built one example of each to facilitate your understanding. Our Swim server remains a `BasicPlane` with simple `UnitAgents` like before, but it is now populated via one of two ingress bridges instead of `CustomClient`.

## WARP Ingress Bridge

This demo corresponds to Design 3, and the relevant source can mostly be found in the `swim.basic.warp` package.

`swim.basic.warp.SourcePlane` is a very simple Swim application that populates 10 minimal `SourceAgents` with data.

`swim.basic.UnitAgents` must be aware of how data is stored in the `SourcePlane`, and each `UnitAgent` creates its downlink accordingly.

### Running This Demo

This demo requires running the `swim.basic.warp.SourcePlane` class and the `swim.basic.BasicPlane` class in that order. Review [`README.md` from the parent directory](../README.md) for instructions on how to do this. For those who pick Option 2, the `gradle` task corresponding to `swim.basic.warp.SourcePlane` is called `runWarp`.

Note that you can even choose to run these processes on multiple machines; as long as you change the `UnitAgent` downlink logic accordingly (just the `hostUri`) accordingly and the machines can see each other over a network, everything will work.

## MQTT Ingress Bridge

This demo corresponds to Design 4, and the relevant source can be found in the `swim.basic.mqtt` package.

In order to demonstrate MQTT integration, our data must be stored in an MQTT server. To obviate manually standing up an MQTT server, the code currently uses one of the [publicly available MQTT brokers](https://github.com/mqtt/mqtt.github.io/wiki/public_brokers); feel free to replace this with your own broker.

We use the [Eclipse Paho Java Client](https://github.com/eclipse/paho.mqtt.java) for MQTT client operations; note the corresponding dependency in `build.gradle`.

`swim.basic.mqtt.DataSourcePopulator` sends strings of the form `@msg{id:%d,val:%s}` over MQTT to the aforementioned broker under  the topic `swimSensors/all`.

`swim.basic.mqtt.IngressBridge` listens to this topic, then relays any arriving messages to the right destinations after some minor transformations using a Swim command. Equivalently, we can send a properly-structured websocket message instead; this is also our only option in a language outside Java and Javascript.

### Running This Demo

This demo requires running the `swim.basic.warp.SourcePlane`, `swim.basic.mqtt.DataSourcePopulator`, and `swim.basic.mqtt.IngressBridge` classes in that order. Review [`README.md` from the parent directory](../README.md) for instructions on how to do this. For those who pick Option 2, the `gradle` task corresponding to `swim.basic.mqtt.DataSourcePopulator` is called `runMqtt`, and the one corresponding to `swim.basic.mqtt.IngressBridge` is called `runBridge`.
