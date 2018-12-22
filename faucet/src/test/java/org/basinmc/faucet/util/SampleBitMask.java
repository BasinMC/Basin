package org.basinmc.faucet.util;

import java.util.Collection;
import java.util.List;

/**
 * Sample bitmask specification for testing purposes.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public final class SampleBitMask extends BitMask<SampleBitMask> {

  public static final SampleBitMask A = new SampleBitMask(1 << 0);
  public static final SampleBitMask B = new SampleBitMask(1 << 1);
  public static final SampleBitMask C = new SampleBitMask(1 << 2);
  public static final SampleBitMask D = new SampleBitMask(1 << 3);

  private SampleBitMask(int mask) {
    super(mask);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected SampleBitMask createInstance(int mask) {
    return new SampleBitMask(mask);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<SampleBitMask> values() {
    return List.of(A, B, C, D);
  }
}
