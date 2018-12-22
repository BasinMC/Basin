package org.basinmc.faucet.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class BitMaskTest {

  @Test
  public void testHas() {
    var mask = SampleBitMask.A
        .set(SampleBitMask.B)
        .set(SampleBitMask.C);

    assertTrue(mask.has(SampleBitMask.A));
    assertTrue(mask.has(SampleBitMask.B));
    assertTrue(mask.has(SampleBitMask.C));
    assertFalse(mask.has(SampleBitMask.D));

    assertTrue(mask.has(SampleBitMask.A.set(SampleBitMask.B)));
    assertTrue(mask.has(SampleBitMask.B.set(SampleBitMask.C)));
  }
}
