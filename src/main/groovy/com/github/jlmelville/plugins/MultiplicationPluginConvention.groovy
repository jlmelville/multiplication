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

import org.gradle.api.Project
import org.gradle.api.file.CopySpec

/**
 * <p>A {@link org.gradle.api.plugins.Convention} used for the MultiplicationPlugin.</p>
 */
class MultiplicationPluginConvention {

    /**
     * The name of the application.
     */
    String applicationName

    /**
     * The fully qualified names of the application's main classes.
     */
    Iterable<String> mainClassNames

    /**
     * Array of string arguments to pass to the JVM when running the application
     */
    Iterable<String> applicationDefaultJvmArgs = []

    /**
     * Closure to apply to executable name. By default is the simple class name, but could
     * be converted to lower case, snake_case etc.
     */
    ExeNameTransform exeNameTransform = "None"

    /**
     * <p>The specification of the contents of the distribution.</p>
     * <p>
     * Use this {@link org.gradle.api.file.CopySpec} to include extra files/resource in the application distribution.
     * <pre autoTested=''>
     * apply plugin: 'application'
     *
     * applicationDistribution.from("some/dir") {
     *   include "*.txt"
     * }
     * </pre>
     * <p>
     * Note that the application plugin pre configures this spec to; include the contents of "{@code src/dist}",
     * copy the application start scripts into the "{@code bin}" directory, and copy the built jar and its dependencies
     * into the "{@code lib}" directory.
     */
    CopySpec applicationDistribution

    final Project project

    MultiplicationPluginConvention(Project project) {
        this.project = project
        applicationDistribution = project.copySpec {}
    }

}
