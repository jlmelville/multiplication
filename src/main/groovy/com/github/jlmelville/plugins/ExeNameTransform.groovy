/*
* Copyright 2015 the original author or authors.
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
/**
 * An enum representing three possible transformations of the class name
 * that will become an executable.
 *
 * <ul>
 * <li>None: class name will be used as-is, e.g. the class
 * <pre>baz.FooBar</pre> becomes the command line exe
 * <pre>FooBar</pre> and <pre>FooBar.bat</pre></li>
 * <li>SnakeCase: class name is transformed to snake case: e.g. <pre>foo_bar</pre></li>
 * <li>LowerCase: class name is transformed to lower case: e.g. <pre>foobar</pre></li>
 * </ul>
 */
package com.github.jlmelville.plugins
enum ExeNameTransform {
    None, SnakeCase, LowerCase;
}
