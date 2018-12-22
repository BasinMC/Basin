package org.basinmc.sink.event;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.basinmc.faucet.event.ExecutionContext;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class SinkExecutionContext<S extends Enum<S>> implements ExecutionContext<S> {

  private final S defaultState;
  private S state;

  SinkExecutionContext(S defaultState) {
    this.defaultState = defaultState;
    this.state = defaultState;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public S getDefaultState() {
    return this.defaultState;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public S getState() {
    return this.state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setState(@NonNull S s) {
    this.state = s;
  }
}
