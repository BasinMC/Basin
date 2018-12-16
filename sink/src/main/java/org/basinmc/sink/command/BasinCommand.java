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
package org.basinmc.sink.command;

import java.util.Optional;
import org.basinmc.faucet.command.Color;
import org.basinmc.faucet.command.Communicable;
import org.basinmc.faucet.command.annotation.Alias;
import org.basinmc.faucet.command.annotation.Command;
import org.basinmc.faucet.command.annotation.DateFormat;
import org.basinmc.faucet.command.annotation.Description;
import org.basinmc.faucet.command.annotation.Option;
import org.basinmc.faucet.command.annotation.Subcommand;
import org.basinmc.faucet.command.annotation.Supercommand;

// TODO - this class does nothing currently but serve as an example command implementation.
@Command("basinctl") // all hail our systemd overlords
@Alias("b")
@Description("Provides userspace access and control to server-specific functions.")
public class BasinCommand {

  @Supercommand
  public void printServerState(Communicable sender) {
    sender.sendMessage(Color.GOLD + "Basin Sink");
  }

  @Subcommand("plugins")
  @Alias("pl")
  @Description("List plugins.")
  public void plugins(Communicable sender,
      @Option(desc = "Nicely format output in a table", shortOpt = 't', longOpt = "table") boolean table,
      @Option(desc = "Hide initialization states", shortOpt = 'H', longOpt = "hide-init") boolean hideinit) {

  }

  @Subcommand("restart")
  @Alias("reload")
  @Description("Restart a plugin.")
  public void restart(Communicable sender,
      @Option(desc = "Bundle to restart, defaults to the server") Optional<String> bundleName,
      @Option(desc = "Delay before the restart occurs", shortOpt = 'd', longOpt = "delay") @DateFormat long delay,
      @Option(desc = "Notify online players", shortOpt = 'n', longOpt = "notify") boolean notify) {

  }
}
