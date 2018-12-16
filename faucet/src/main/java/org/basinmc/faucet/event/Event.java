package org.basinmc.faucet.event;

import javax.annotation.Nullable;

/**
 * Marks the annotated type as an event which may be passed to the local event bus.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface Event<STATE extends Enum<STATE>> {

  /**
   * Retrieves the default state for this event's execution context.
   *
   * @return an arbitrary state.
   */
  @Nullable
  default STATE getDefaultState() {
    return null;
  }
}
