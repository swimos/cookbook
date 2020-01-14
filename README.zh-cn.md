# Cookbook（烹饪书）

所有的[cookbook](https://swim.dev/tutorials/) 代码片段，组合成了可运行的Gradle项目。

*其他语言版本：[English](README.md), [简体中文](README.zh-cn.md)*

## 运行Java类

运行任何cookbook要求运行至少一个Java类

### 方法1：使用IDE创建且运行

只需要浏览到需要运行的类（集），然后通过IDE运行它（们）

### 方法2：使用Gradle创建且运行

每一个cookbook代表了一个Swim 伺服器（Server）。为一个已知的cookbook运行伺服器，可简单地用命令行来发送 `gradle $COOKBOOK-NAME:run` （或者是它的其他变异之一，这在接下来的部分中会概括）来执行。例如：`gradle web-agents:run` 运行cookbook网络代理 (Web Agent)。

Cookbook的展示中会需要运行多个Java类，用户将会发现每个Java类都会有相对应的不同Gradle run task。在所有的cookbook的子目录中都可以找到专门的README，里面描述了如何运行子目录Task。例如：要使得完全运行Command Lanes cookbook，用户必须在该目录中发送 `gradle command-lanes:run` 和 `gradle command-lanes:runClient`。

#### `gradle` 的变异

用户只有在安装了Gradle的机器上才能够使用 `gradle` 命令。补充：cookbook只支持Gradle 5.2 版本及以上

若用户并不希望安装或者更新当地Gradle分配，用户可以使用以下的命令来代替 `gradle`：

- `./gradlew` 适用于大部分非Windows环境

- `.\gradlew.bat` 适用于Windows

### 方法3：使用Gradle创建，但用Java运行

创建和包装一个cookbook，只需要运行 `gradle $COOKBOOK-NAME:build`（或者是之前叙述过的 `gradle` 变异命令之一）

该命令将会在 `$COOKBOOK_NAME/build/distributions/` 目录下创建 `.tar` 和 `.zip` 文件。解压其中任意一个文件使脚本文件显示在`bin`目录下

在默认情况下，引发的脚本将会只运行指定的cookbook的主要Swim伺服器。对于那些需要运行多个Java类的cookbook，最简单地方法是在新文件中精确地复制黏贴脚本，然后将默认主函类换成目标主类。

举例来说，[出口网桥cookbook](/egress_bridges) 需要按顺序运行 `swim.grade.db.Database`, `swim.grade.GradePlane`, 和 `swim.grade.Sim` 类。在接下来的步骤中将会介绍如何配置环境使得机器可运行以上类（该教程假设基于*nix环境，但相对应的Windows指令不言自明）：

1. `gradle egress-bridges:build`（或者其他之前叙述过的 `gradle` 变异）

2. `cd egress_bridges/build/distributions/`

3. `unzip egress-bridges-3.10.0.zip`

4. `cd egress-bridges-3.10.0/bin`（若cookbook只需要运行一个Java类，则到此已完成运行）

5. `cp egress-bridges database` （在Windows环境中使用 `.bat` 文件）

6. 编辑 `database(.bat)` 文件，将 `swim.grade.GradePlane` 替换成 `swim.grade.db.Database`

7. `cp egress-bridges sim`（在Windows环境中使用 `.bat` 文件）

8. 编辑 `sim(.bat)`, 将 `swim.grade.GradePlane` 替换成 `swim.grade.Sim`

此时运行cookbook只需要在之前提到的 `bin/` 目录下按照顺序运行 `./database`，`./egress-bridges`，以及 `./sim` 即可。