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

import org.basinmc.faucet.util.Priority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows one to specify the order in which command handlers mapped to the same command (or between
 * a parent and a child subcommand handler) are executed. By default, all handlers mapped the same
 * command or command family are executed in a non-deterministic order. By this, command handlers
 * for each command are ordered based on priority and executed sequentially.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DelegatePriority {
    Priority value();

    /**
     * Set to false to halt execution of the command after this executor exits.
     */
    boolean serial() default true;
}
