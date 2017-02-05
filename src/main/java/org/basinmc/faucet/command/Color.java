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
package org.basinmc.faucet.command;

public enum Color {
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_CYAN('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GREY('7'),
    DARK_GREY('8'),
    BLUE('9'),
    GREEN('a'),
    CYAN('b'),
    RED('c'),
    PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),

    OBSCURE('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINE('n'),
    ITALICS('o'),
    RESET('r');

    private char controlCode;

    public static final char PREFIX = '§';

    Color(char controlCode) {
        this.controlCode = controlCode;
    }

    public char getControlCode() {
        return controlCode;
    }

    @Override
    public String toString() {
        return String.valueOf(PREFIX) + String.valueOf(controlCode);
    }
}
