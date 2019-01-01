/*
 * Copyright 2019 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.sink.plugin.manifest;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.netty.buffer.ByteBuf;
import java.util.Objects;
import org.basinmc.faucet.extension.error.ExtensionManifestException;
import org.basinmc.faucet.extension.manifest.ExtensionManifest;
import org.basinmc.sink.util.BufferUtil;

/**
 * Represents the container header (e.g. the area which identifies how much metadata and actual
 * executable code is contained within this container).
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class ExtensionHeader {

  /**
   * Defines the total length of the extension header area.
   */
  public static final int LENGTH = 30;

  private final long manifestLength;
  private final long signatureLength;
  private final long contentLength;

  public ExtensionHeader(@NonNull ByteBuf in) throws ExtensionManifestException {
    BufferUtil.checkMagicValue(in, ExtensionManifest.MAGIC_NUMBER,
        () -> new ExtensionManifestException("Illegal extension header"));

    in.skipBytes(2); // TODO: Flags
    this.manifestLength = in.readLong();
    this.signatureLength = in.readLong();
    this.contentLength = in.readLong();
  }

  public ExtensionHeader(int manifestLength, int signatureLength, int contentLength) {
    this.manifestLength = manifestLength;
    this.signatureLength = signatureLength;
    this.contentLength = contentLength;
  }

  public long getManifestLength() {
    return this.manifestLength;
  }

  public long getSignatureLength() {
    return this.signatureLength;
  }

  public long getContentOffset() {
    return LENGTH + this.signatureLength + this.manifestLength;
  }

  public long getContentLength() {
    return this.contentLength;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExtensionHeader)) {
      return false;
    }
    ExtensionHeader that = (ExtensionHeader) o;
    return this.manifestLength == that.manifestLength &&
        this.signatureLength == that.signatureLength &&
        this.contentLength == that.contentLength;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.manifestLength, this.signatureLength, this.contentLength);
  }
}
