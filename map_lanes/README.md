# Map Lanes

## Running A Java Class

This demo requires running the `BasicPlane` and `CustomClient` Java classes.

### Option 1: Build And Run With IDE

Simply navigate to your class, and run it through your IDE.

### Option 2: Build And Run With Gradle

0. Ensure that the `mainClassName` variable in your `build.gradle` file is correct.

1. In a shell, navigate to this directory (`/some/path/cookbook-code/map_lanes/`)

2. Run one of the following:

  - `./gradlew run`, on most non-Windows machines
  
  - `.\gradlew.bat run`, on Windows machines

  - `gradle run` (regardless of machine) if you have a local Gradle distribution and prefer to use that; note that we do not support anything prior to 5.2.

### Option 3: Build With Gradle, Run With Java

0. Ensure that the `mainClassName` variable in your `build.gradle` file is correct.

1. In a shell, navigate to this directory (`/some/path/cookbook-code/map_lanes/`)

2. Run one of the following:

  - `./gradlew build`, on most non-Windows machines
  
  - `.\gradlew.bat build`, on Windows machines

  - `gradle build` (regardless of machine) if you have a local Gradle distribution and prefer to use that; note that we do not support anything prior to 5.2.

3. Unfoo the newly-created `build/distributions/map-lanes-3.9.2.foo`, where "foo" is either "tar" or "zip".

4. Run the `bin/map-lanes` script (`bin\map-lanes.bat` on Windows).

## Running This Demo

Follow the "Running A Java Class" instructions twice: once with `mainClassName=BasicPlane`, and once with `mainClassName=CustomClient`.
