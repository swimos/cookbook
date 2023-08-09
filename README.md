# Cookbook

All [cookbook](https://swimos.org/tutorials/) code snippets, gathered into runnable Gradle projects.

*Read this in other languages: [简体中文](README.zh-cn.md)*

## Running a Java Class

Running any cookbook requires running at least one Java class.

### Option 1: Build and Run With IDE

Simply navigate to your desired class(es), and run them through your IDE.

### Option 2: Build and Run With Gradle

Every cookbook stands up a Swim server. To run this server for a given cookbook, simply issue `./gradlew $COOKBOOK-NAME:run` (or `.\gradlew.bat $COOKBOOK-NAME:run on Windows`) from your command line.

- For example, `./gradlew web-agents:run` runs the Web Agents cookbook.

For a cookbook demonstration that requires running multiple classes, you will find a separate Gradle run task corresponding to each of these classes. All cookbook-specific READMEs, found in their appropriate child directories, outline how to run these tasks.

- For example, to fully run the Command Lanes cookbook, you must issue both a `./gradlew command-lanes:run` and a `./gradlew command-lanes:runClient` from this directory.

### Option 3: Build With Gradle, Run With Shell

To build and package a cookbook, simply run `./gradlew $COOKBOOK-NAME:build`.

This will generate both a `.tar` and a `.zip` file in the `$COOKBOOK_NAME/build/distributions/` directory. Unpackaging either one of these files exposes scripts in the `bin` directory.

By default, the generated script will only run the primary Swim server of the given cookbook. For cookbooks that require multiple classes, the easiest thing to do is to copy the script exactly in a new file, and swap out this default main class for the desired one.

For example recall that the [Egress bridges cookbook](/egress_bridges) requires running the `swim.grade.db.Database`, `swim.grade.GradePlane`, and `swim.grade.Sim` classes in that order. The following steps will set up your environment to enable running these classes; the instructions assume a *nix environment, but the Windows equivalents should be self-explanatory:

1. `./gradlew egress-bridges:build`

2. `cd egress_bridges/build/distributions/`

3. `unzip egress-bridges-3.10.0.zip`

4. `cd egress-bridges-3.10.0/bin` ; note that if the cookbook only required running one Java class, you would be done already

5. `cp egress-bridges database` (use `.bat` files instead in a Windows environment)

6. Edit `database(.bat)` by replacing `swim.grade.GradePlane` with `swim.grade.db.Database`

7. `cp egress-bridges sim` (use `.bat` files instead in a Windows environment)

8. Edit `sim(.bat)` by replacing `swim.grade.GradePlane` with `swim.grade.Sim`

Running the cookbook now simply requires running `./database`, `./egress-bridges`, and `./sim` in that order from the aforementioned `bin/` directory.
