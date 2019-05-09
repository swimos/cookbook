# Egress Bridges

Code corresponding to the [egress bridges cookbook](https://swim.dev/tutorials/egress-bridges/).

## Demo Motivation

Recall the two designs of push-type egress bridges to non-Swim sinks:

1. A *singleton* Web Agent writes to the sink through a *non-blocking* driver

2. *Any* Web Agent writes to the sink through a *thread-safe, non-blocking* driver

Our sink in this example is a relational H2 database. Oracle has an [Asynchronous Database Acesss](https://blogs.oracle.com/java/jdbc-next:-a-new-asynchronous-api-for-connecting-to-a-database) (ADBA) API, of which [ADBA over JDBC](https://github.com/oracle/oracle-db-examples/tree/master/java/AoJ) implements a subset. Everything in the `com.oracle.adbaoverjdbc` package in this repository has been pulled straight from there; the only modifications were to make the directory structures match what Gradle expects.

We scrape together the working pieces to create our non-blocking `CustomDriver`. In order to keep the implementation simple, `CustomDriver` is not thread-safe. We therefore only demonstrate Option 2 in this example.

## Run

1. Run `swim.grade.db.Database` to start an H2 database.

2. Run `swim.grade.GradePlane` to run the Swim server and the non-blocking connector to the database.

3. Run `swim.grade.Sim` to simulate data being sent to the Swim server.

## Stop

Close aforementioned three processes in the opposite order to which they were run. Immediately before `GradePlane` exits, the process issues a read of each student in the Database; you should confirm that these match the final log for each `StudentAgent`.

## Running A Java Class

This demo requires running multiple Java classes.

### Option 1: Build And Run With IDE

Simply navigate to your class, and run it through your IDE.

### Option 2: Build And Run With Gradle

0. Ensure that the `mainClassName` variable in your `build.gradle` file is correct.

1. In a shell, navigate to this directory (`/some/path/cookbook/egress_bridges/`)

2. Run one of the following:

  - `./gradlew run`, on most non-Windows machines
  
  - `.\gradlew.bat run`, on Windows machines

  - `gradle run` (regardless of machine) if you have a local Gradle distribution and prefer to use that; note that we do not support anything prior to 5.2.

### Option 3: Build With Gradle, Run With Java

0. Ensure that the `mainClassName` variable in your `build.gradle` file is correct.

1. In a shell, navigate to this directory (`/some/path/cookbook/egress_bridges/`)

2. Run one of the following:

  - `./gradlew build`, on most non-Windows machines
  
  - `.\gradlew.bat build`, on Windows machines

  - `gradle build` (regardless of machine) if you have a local Gradle distribution and prefer to use that; note that we do not support anything prior to 5.2.

3. Unfoo the newly-created `build/distributions/egress-bridges-3.9.2.foo`, where "foo" is either "tar" or "zip".

4. Run the `bin/egress-bridges` script (`bin\egress-bridges.bat` on Windows).
