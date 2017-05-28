/*
 * Copyright 2017 Hex <hex@hex.lc>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.basinmc.faucet.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a method that handles subcommand executions for its enclosing {@link Command} type. The
 * subcommand's name will be chosen for a particular execution based on the first argument to the
 * command. Spaces can be used in the subcommand's name field to denote a nested subcommand (e.g. a
 * subcommand of a subcommand). Multiple executors can be mapped to one subcommand. If this
 * annotation is applied to an inner class, it is presumed to be a subcommand handler for an outer
 * class. In this case, subcommand handlers within the inner class would be mapped to subcommands of
 * the subcommand of the main command superhandler.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Subcommand {

  String value();
}
