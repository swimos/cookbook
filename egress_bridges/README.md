# Egress Bridges

Code corresponding to the [egress bridges cookbook](https://swimos.org/tutorials/egress-bridges/).

*Read this in other languages: [简体中文](README.zh-cn.md)*

## Demo Motivation

Recall the two designs of push-type egress bridges to non-Swim sinks:

1. A *singleton* Web Agent writes to the sink through a *non-blocking* driver

2. *Any* Web Agent writes to the sink through a *thread-safe, non-blocking* driver

Our sink in this example is a relational H2 database. Oracle has
an [Asynchronous Database Acesss](https://blogs.oracle.com/java/jdbc-next:-a-new-asynchronous-api-for-connecting-to-a-database) (
ADBA) API, of which [ADBA over JDBC](https://github.com/oracle/oracle-db-examples/tree/master/java/AoJ) implements a
subset. Everything in the `com.oracle.adbaoverjdbc` package in this repository has been pulled straight from there; the
only modifications were to make the directory structures match what Gradle expects.

We scrape together the working pieces to create our non-blocking `CustomDriver`. In order to keep the implementation
simple, `CustomDriver` is not thread-safe. We therefore only demonstrate Option 2 in this example.

## Running This Demo

This demo requires running the `swim.grade.db.Database`, `swim.grade.GradePlane`, and `swim.grade.Sim` classes in that
order. Review [`README.md` from the parent directory](../README.md) for instructions on how to do this. For those who
pick Option 2, the `gradle` task corresponding to `swim.grade.db.Database` is called `runDb`, and the one corresponding
to `swim.grade.Sim` is called `runSim`.

## Stopping This Demo

Close aforementioned three processes in the opposite order to which they were run. Immediately before `GradePlane`
exits, the process issues a read of each student in the database; you should confirm that these match the final log for
each `StudentAgent`.
