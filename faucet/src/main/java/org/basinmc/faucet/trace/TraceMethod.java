/*
 * Copyright 2016 Hex <hex@hex.lc>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License&quot￼;
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
 *
 */
package org.basinmc.faucet.trace;

import java.lang.invoke.MethodHandle;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Represents a generic method that is used in a trace chain. If a more specific {@link TraceNode}
 * type is available, its use would be preferred for performance reasons.
 */
public interface TraceMethod extends TraceNode {

  /**
   * Get the class that triggered this action.
   *
   * @return a class descriptor
   */
  @NonNull
  Class<?> getStartingClass();

  /**
   * Get the name of the method. Constructors and static initialization blocks will return
   * <p>{@literal <init>}</p> and <p>{@literal <clinit>}</p> respectively.
   *
   * @return a method name
   */
  @NonNull
  String getMethodName();

  /**
   * Get this method's signature as defined by the Java specification. For example,
   * {@code public boolean test(int x, String y);} would be represented as
   * {@code test(ILjava/lang/String;)Z}. For more information,
   * <a href=http://www.rgagnon.com/javadetails/java-0286.html>this tutorial</a> provides
   * a useful introduction to Java method signatures.
   *
   * @return a signature string
   */
  @NonNull
  String getSignature();

  /**
   * Attempt to resolve the given method into a {@link MethodHandle}. Please note that this
   * result is not pre-computed, so the first call to this method will result in a blocking
   * operation which may take some time. However, the result will be cached and can be
   * retrieved later inexpensively.
   *
   * @return a cached or newly-looked-up method handle
   */
  @Nullable
  MethodHandle resolve();
}
