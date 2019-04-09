package org.basinmc.faucet.internal.warn

/**
 * Marks the annotated element as a stable API (e.g. safe to use and covered by the project's
 * deprecation policy).
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR)
annotation class Stable
