package org.basinmc.faucet.event.system;

import org.basinmc.faucet.event.Event.EmptyState;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class StartupEvent extends SystemEvent<EmptyState> {

  public static class Pre extends StartupEvent {

  }

  public static class Post extends StartupEvent {

  }
}
