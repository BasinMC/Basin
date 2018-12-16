package org.basinmc.faucet.internal.warn;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the annotated element as a stable API (e.g. safe to use and covered by the project's
 * deprecation policy).
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Stable {

}
