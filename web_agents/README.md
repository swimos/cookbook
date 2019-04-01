# Web Agents

## Run

This demo requires running the `BasicPlane` Java class.

## Running a Java Class

Simply navigate to the `BasicPlane` class, and run it through your IDE.

### Option 2: Build And Run With Gradle

1. In a shell, navigate to this directory (`/some/path/cookbook-code/web_agents/`)

2. Run one of the following:

  - `./gradlew run`, on most non-Windows machines
  
  - `.\gradlew.bat run`, on Windows machines

  - `gradle run` (regardless of machine) if you have a local Gradle distribution and prefer to use that; note that we do not support anything prior to 5.2.

### Option 3: Build With Gradle, Run With Java

1. In a shell, navigate to this directory (`/some/path/cookbook-code/web_agents/`)

2. Run one of the following:

  - `./gradlew build`, on most non-Windows machines
  
  - `.\gradlew.bat build`, on Windows machines

  - `gradle build` (regardless of machine) if you have a local Gradle distribution and prefer to use that; note that we do not support anything prior to 5.2.

3. Unfoo the newly-created `build/distributions/web-agents-3.9.2.foo`, where "foo" is either "tar" or "zip".

4. Run the `bin/web-agents` script (`bin\web-agents.bat` on Windows).
