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
 * Annotates a method parameter to denote that it is to be filled by a command argument. This inferring
 * is done in the following manner. First, the parameter list will be evaluated without any parameters
 * annotated with this. These nameless parameters will be assigned, in order, from the beginning of the
 * command string, ignoring any words beginning with "-". Second, the remaining words will be determined to
 * be either long or short options. If this annotates a boolean, the parameter will be inferred to have no
 * arguments. Otherwise, if the succeeding parameter is an {@link java.util.Optional}, then the following
 * word is evaluated. If the following word is not an option (i.e. does not begin with "-") then the next
 * word is used as the parameter. If it does, then the optional will be passed an empty value. If it is not an
 * optional, then it will be passed the raw value if it exists; otherwise, an error message will be
 * displayed.That parameter may have spaces in it as long as they are surrounded with whichever
 * string is given by {@link #term()} and the {@link #multiword()} flag is set.
 *
 * If an {@link java.util.Optional} is used as a parameter then it will be considered optional. If it is not
 * present either by option name or by argument number, it will be passed an empty value.
 *
 * Short options and long options are used interchangeably.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Option {
    /**
     * Give a short description of this option.
     */
    String desc() default "";

    /**
     * Determine the character to be used for this short option. Invocations will be in the format
     * "/command -X [argument]" with 'X' being the short option character.
     */
    char shortOpt() default '\0';

    /**
     * Determine the string to be used for this long option. Invocations will be in the format
     * "/command --short-opt [argument]" with "short-opt" being the short option string.
     */
    String longOpt() default "";

    /**
     * Determines if this option accepts an argument that can have spaces in it.
     */
    boolean multiword() default false;

    /**
     * Determines the termination string for a multiword argument. The command string will be read
     * from the first word immediately following the option to the next occurrence of this string.
     */
    String term() default "\"";

    /**
     * Determines if this has an argument or not. If it annotates a boolean, the answer is no. This
     * should not normally be set.
     */
    boolean argument() default true;

    /**
     * Show the type to which the string option should be parsed to. This should only be used to
     * annotate {@link java.util.Optional} parameters due to type erasure and will be ignored
     * in other circumstances.
     */
    Class<?> type() default String.class;
}
