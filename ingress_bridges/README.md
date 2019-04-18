# Ingress Bridges

Code corresponding to the [ingress bridges cookbook](https://swim.dev/tutorials/ingress-bridges/).

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

To run this demo:

1. Run `swim.basic.warp.SourcePlane`

2. Run `swim.basic.BasicPlane`

You can even choose to run these processes on multiple machines; as long as you change the `UnitAgent` downlink logic accordingly (just the `hostUri`) accordingly and the machines can see each other over a network, everything will work.

## MQTT Ingress Bridge

This demo corresponds to Design 4, and the relevant source can be found in the `swim.basic.mqtt` package.

In order to demonstrate MQTT integration, our data must be stored in an MQTT server. To obviate manually standing up an MQTT server, the code currently uses one of the [publicly available MQTT brokers](https://github.com/mqtt/mqtt.github.io/wiki/public_brokers); feel free to replace this with your own broker.

`swim.basic.mqtt.DataSourcePopulator` sends strings of the form `@msg{id:%d,val:%s}` over MQTT to the aforementioned broker under  the topic `swimSensors/all`.

`swim.basic.mqtt.IngressBridge` listens to this topic, then relays any arriving messages to the right destinations after some minor transformations using a Swim command. Equivalently, we can send a properly-structured websocket message instead; this is also our only option in a language outside Java and Javascript.

To run this demo:

1. Run `swim.basic.BasicPlane`

2. Run `swim.basic.mqtt.DataSourcePopulator`

3. Run `swim.basic.mqtt.IngressBridge`

## Running A Java Class

This demo requires running multiple Java classes.

### Option 1: Build And Run With IDE

Simply navigate to your class, and run it through your IDE.

### Option 2: Build And Run With Gradle

0. Ensure that the `mainClassName` variable in your `build.gradle` file is correct.

1. In a shell, navigate to this directory (`/some/path/cookbook/ingress_bridges/`)

2. Run one of the following:

  - `./gradlew run`, on most non-Windows machines
  
  - `.\gradlew.bat run`, on Windows machines

  - `gradle run` (regardless of machine) if you have a local Gradle distribution and prefer to use that; note that we do not support anything prior to 5.2.

### Option 3: Build With Gradle, Run With Java

0. Ensure that the `mainClassName` variable in your `build.gradle` file is correct.

1. In a shell, navigate to this directory (`/some/path/cookbook/ingress_bridges/`)

2. Run one of the following:

  - `./gradlew build`, on most non-Windows machines
  
  - `.\gradlew.bat build`, on Windows machines

  - `gradle build` (regardless of machine) if you have a local Gradle distribution and prefer to use that; note that we do not support anything prior to 5.2.

3. Unfoo the newly-created `build/distributions/map-lanes-3.9.2.foo`, where "foo" is either "tar" or "zip".

4. Run the `bin/ingress-bridges` script (`bin\ingress-bridges.bat` on Windows).

Follow the "Running A Java Class" instructions twice: once with `mainClassName=BasicPlane`, and once with `mainClassName=CustomClient`.
