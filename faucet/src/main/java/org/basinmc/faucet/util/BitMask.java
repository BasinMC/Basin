package org.basinmc.faucet.util;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * Represents an arbitrary bit mask which expresses the state of one or more predefined flags.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class BitMask<M extends BitMask<M>> implements Iterable<M> {

  private final int mask;

  protected BitMask(int mask) {
    this.mask = mask;
  }

  /**
   * Retrieves the total amount of set flags within this mask.
   *
   * @return an amount of flags.
   */
  public int count() {
    return Integer.bitCount(this.mask);
  }

  /**
   * Evaluates whether this mask contains the indicated flag.
   *
   * @param state an arbitrary flag.
   * @return true if set, false otherwise.
   */
  public boolean has(@NonNull M state) {
    if (this.getClass() != state.getClass()) { // TODO: Does this seem sensible?
      return false;
    }

    return (this.mask & ((BitMask) state).mask) == ((BitMask) state).mask;
  }

  /**
   * Sets one or more flags within this mask.
   *
   * @param state an arbitrary flag.
   */
  public M set(@NonNull M state) {
    if (this.getClass() != state.getClass()) {
      throw new IllegalArgumentException(
          "Expected state flag to be of type " + this.getClass().getName() + " but got " + state
              .getClass().getName());
    }

    return this.createInstance(this.mask ^ ((BitMask) state).mask);
  }

  /**
   * Un-Sets one or more flags within this mask.
   *
   * @param state an arbitrary flag.
   */
  public M unset(@NonNull M state) {
    if (this.getClass() != state.getClass()) {
      throw new IllegalArgumentException(
          "Expected state flag to be of type " + this.getClass().getName() + " but got " + state
              .getClass().getName());
    }

    return this.createInstance(this.mask & ~((BitMask) state).mask);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Iterator<M> iterator() {
    return this.values().stream()
        .filter(this::has)
        .iterator();
  }

  /**
   * Constructs a mutated bitmask instance for this particular type.
   *
   * @param mask an arbitrary bitmask.
   * @return a mutated instance.
   */
  protected abstract M createInstance(int mask);

  /**
   * Retrieves a list of all permitted values within this mask type.
   *
   * @return a list of permitted values.
   */
  public abstract Collection<M> values();

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BitMask)) {
      return false;
    }
    BitMask<?> bitMask = (BitMask<?>) o;
    return this.mask == bitMask.mask;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.mask);
  }
}
