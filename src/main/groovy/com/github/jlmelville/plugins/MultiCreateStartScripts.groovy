
/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jlmelville.plugins

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Optional
import org.gradle.util.GUtil
/**
 * <p>A {@link org.gradle.api.Task} for creating OS dependent start scripts.</p>
 */
public class MultiCreateStartScripts extends ConventionTask {

    /**
     * The directory to write the scripts into.
     */
    @OutputDirectory
    File outputDir

    /**
     * The application's main class.
     */
    @Input
    Iterable<String> mainClassNames

    /**
     * The application's default JVM options.
     */
    @Input
    @Optional
    Iterable<String> defaultJvmOpts = []

    /**
     * The application's name.
     */
    @Input
    String applicationName

    @Input
    ExeNameTransform exeNameTransform = "None"

    String optsEnvironmentVar

    String exitEnvironmentVar

    /**
     * The class path for the application.
     */
    @InputFiles
    FileCollection classpath

    /**
     * Returns the name of the application's OPTS environment variable.
     */
    @Input
    String getOptsEnvironmentVar() {
        if (optsEnvironmentVar) {
            return optsEnvironmentVar
        }
        if (!getApplicationName()) {
            return null
        }
        return "${GUtil.toConstant(getApplicationName())}_OPTS"
    }

    @Input
    String getExitEnvironmentVar() {
        if (exitEnvironmentVar) {
            return exitEnvironmentVar
        }
        if (!getApplicationName()) {
            return null
        }
        return "${GUtil.toConstant(getApplicationName())}_EXIT_CONSOLE"
    }

    File getUnixScript(exeName) {
        return new File(getOutputDir(), exeName)
    }

    File getWindowsScript(exeName) {
        return new File(getOutputDir(), "${exeName}.bat")
    }

    String getExeName(className) {
        className.split(/\./)[-1]
    }

    @TaskAction
    void generate() {
        getMainClassNames().each { mainClassName ->
            def generator = new ShortClasspathStartScriptGenerator()
            def exeName = getExeName(mainClassName)
            ExeNameTransform transform = getExeNameTransform()
            if (transform == ExeNameTransform.SnakeCase) {
                exeName = { str -> str.replaceAll(/\B[A-Z][a-z]/) { '_' + it }.toLowerCase() }(exeName)
            }
            else if (transform == ExeNameTransform.LowerCase) {
                exeName = exeName.toLowerCase()
            }

            generator.applicationName = exeName
            generator.mainClassName = mainClassName
            generator.defaultJvmOpts = getDefaultJvmOpts()
            generator.optsEnvironmentVar = getOptsEnvironmentVar()
            generator.exitEnvironmentVar = getExitEnvironmentVar()
            generator.classpath = getClasspath().collect { "lib/${it.name}" }
            generator.scriptRelPath = "bin/${getUnixScript(exeName).name}"
            generator.generateUnixScript(getUnixScript(exeName))
            generator.generateWindowsScript(getWindowsScript(exeName))
        }
    }

}
