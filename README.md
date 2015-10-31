# Multiplication
[![GitHub license](https://img.shields.io/github/license/jlmelville/multiplication.svg)](https://github.com/jlmelville/multiplication/blob/master/LICENSE)

Multiplication is an alternative to the
[application Gradle plugin](https://docs.gradle.org/current/userguide/application_plugin.html)
that allows a project to have multiple command-line executable JVM
applications associated with it.

## Usage
Install the plugin and configure the `buildscript` section like you
would with any other custom plugin
(see the [Installation](#installation) section).
Then just pass an array of all the class names you want to have as
executables:
```Gradle
apply plugin: 'multiplication'
mainClassNames = [ 'foo.bar.AnExeClassName',
                   'foo.bar.AnotherExeClassName',
                   'foo.bar.YetAnotherOne' ]
// if you want the executables to be called e.g. 'an_exe_class_name'
// instead of 'AnExeClassName' you can specify a transformation:
exeNameTransform = 'SnakeCase'
```
When you run the `installApps` task:
```bash
./gradlew installApps
```

you'll find all the scripts in `build/install/<project name>/bin`
just like you would find the single executable using the application plugin.

## Installation
Build this project from source, and then use a Maven artifact or the jar
directly in your own Gradle project.

### Creating a Maven artifact
The plugin build script uses the
[maven-publish plugin](https://docs.gradle.org/current/userguide/publishing_maven.html),
so you can publish the plugin as a POM to a Maven repo. Using the `publishToMavenLocal` task:

```Shell
gradlew publishToMavenLocal
```
will publish it to your maven local repo (which is normally in `$HOME/.m2`).

Then, to use the plugin in your own project, add this to your `build.gradle`

```Gradle
buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath 'com.github.jlmelville.plugins:multiplication:1.0'
    }
}
```

### Creating a jar
The plugin doesn't have any transitive dependencies, so you can also
just add the jar as a dependency directly. First, create the jar from
the plugin's build directory:

```Shell
gradlew jar
```

This will put the plugin jar file in the `build/libs` directory.

Then, to use the plugin in your own project, add this to your `build.gradle`

```Gradle
buildscript {
    dependencies {
        classpath fileTree(dir: '/path/to/the/jar',
            includes: ['multiplication-1.0.jar'])
    }
}
```

with `/path/to/the/jar` replaced with the actual location of
the multiplication plugin jar. But you knew that already.

## Special Bonus Features
### Filename Casing Options
If, like me, you are not a fan of camel case in executable names, then you
can specify the following in your build script:

```exeNameTransform = "SnakeCase"```

and the executables in the Usage example would have been created as
`an_exe_class_name`, `another_exe_class_name` and `yet_another_one`.

If even underscores offend your sense of aesthetics, then use

```exeNameTransform = "LowerCase"```

and the executables are now `anexeclassname`, `anotherexeclassname` and
`yetanotherone`.

### Windows Batch Script Tweak
For the Windows batch script only, the classpath is specified as
`*.jar`, rather that specifying all the jar files in the ``lib`` directory
one at a time. This massively reduces the risk of the command line
invocation exceeding the maximum length allowed.

## Why Do This?
I like Gradle, and I especially like the application plugin, because it
makes it nice and easy to create a command-line JVM executable
(notwithstanding its unfortunate tendency to create command lines that are
[too long for Windows to handle](https://gist.github.com/jlmelville/2bfe9277e9e2c0ff79b6)).
What I don't like about it is that you can only create one executable per
project. One solution is to create one sub-project per command line
program, but this is incredibly tedious and heavyweight.

So here's the plugin. I looked at the source to the application plugin to
see if there was a good way to subclass or otherwise hijack the existing
implementation into using an array of strings instead of a single
string. Sadly, that was beyond my meagre Gradling skills.

As a result, this code is mainly a copy and paste of the original application
plugin with some minor modifications to support an array. I'm not proud of
myself.

The only thing I'm less proud of is the horrific pun I chose for the name of
the plugin.

## Known Issues
* On Windows, because the classpath is specified with the `*.jar`  syntax,
  you must be using Java 6 or above.

* From at least Gradle 2.8, using the application plugin gives a
  deprecation warning that it's going away in Gradle 3.0. So there's a
  good chance that the multiplication plugin might break with Gradle 3.0.
  Let's worry about that when it happens, though.

## License
[The Apache License, version 2.0.](http://www.apache.org/licenses/LICENSE-2.0)