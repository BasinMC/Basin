package org.basinmc.faucet.event.system;

import org.basinmc.faucet.event.Event;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class SystemEvent<S extends Enum<S>> implements Event<S> {

  public void test() {
  }
}
