Contribution Guidelines
-----------------------

Hey there, and thanks for considering to contribute to Basin!

But before you get started with programming your modification and submitting it back to us, please
take a few minutes to read this guide closely to make sure you understood everything correctly. This
guide is one of the most important documents we currently maintain as it ensures our code quality
stays at a minimum acceptable level.

This document exists to protect your and everybody else's sanity!

Code Style
----------

The most important things first: The entire code in the Basin project is formatted based on
[Google's Style Guide][Google Java Style] and thus you should ensure you are aware of its specifics.

We generally recommend, that all contributors configure their IDEs to maintain the code style
automatically. A set of ready-to-use code styles can be found in Google's
[Java Styleguide Repository][Google Java Style Presets].

Another important aspect of the Basin code style: Always reference object local variables using
`this`. This guarantees no accidental shadowing occurs and clarifies the code further.

[Google Java Style]: https://google.github.io/styleguide/javaguide.html
[Google Java Style Presets]: https://github.com/google/styleguide

JavaDoc
-------

Documentation is probably the second most important thing to look out for. A good documentation
reduces the amount of arising questions and improves productivity.

Always make sure to give your methods and classes useful descriptions which make it easy to
understand their purpose and usage. Include a code example if relevant!

Also make sure to document **all exceptions** your method throws even if it is technically an heir
of `java.lang.RuntimeException`. People might want to catch those errors before they reach Basin
again!

And last but no less important: Do not state the obvious. We all have a common sense, right?

If you need a good example of the level we'd like to achieve, check Netty's [ByteBuf JavaDoc][ByteBuf Doc].

[ByteBuf Doc]: https://github.com/netty/netty/blob/4.1/buffer/src/main/java/io/netty/buffer/ByteBuf.java#L32

JSR 305
-------

Generally all methods and classes should indicate their thread safety, null-ness and signing. So
generally:

* Declare the nullness of method return types with `@Nullable` and `@NonNull`
* Document the nullness of all method parameters using `@Nullable` and `@NonNull`
* Annotate all string based fields which are bound to certain patterns with `@MatchesPattern`
* Declare the signing of all integer parameters and return values using `@Signed` and `@Nonnegative`
* Document all returned or passed closable resources with `@WillClose` or `@WillNotClose`
* Annotate all parameters or return values that utilize RegEx with `@RegEx`
* Document abstract classes which expect their children to call the super method with `@OverridingMethodsMustInvokeSuper`
* Annotate all thread-safe classes with `@ThreadSafe`
* Document all methods and fields guarded by a lock with `@GuardedBy`
* Mark immutable classes as `@Immutable`

This method ensures that any developer's IDE is aware of nullness and common pitfalls without the
need of checking the JavaDoc directly.

Annotations
-----------

Ensure all introduced annotations use the correct `RetentionPolicy` (mark them invisible unless you
expect them to become runtime visible in the future!). Also mark all annotations that should show up
in the JavaDoc with `@Documented` so their behavior can be understood by anybody who wishes to read
the documentation or the code.

Annotations should additionally be documented with their entire purpose (e.g. include information
on the transformations applied to annotated members).

Pull Requests
-------------

Think twice before writing and submitting a pull request! Make sure your changes do not break any
vanilla functionality. Also verify their documentation and code style.

Requests that do not comply to above's guidelines will not be accepted and closed if they fall
victim to inactivity (you will be asked to fix issues and given a few weeks time to do so before
your request is considered inactive and closed)!

Philosophy
----------

Breaking vanilla functionality is not ideal, but sometimes necessary in order to achieve a particular
goal, most often a performance patch. If your changes break vanilla functionality, while we would prefer
that it didn't, please mark it as such and extensively document exactly what it changes, no matter how
trivial. 

The Faucet API is in an extremely volatile state and, as such, we expect anyone using it to remain
up-to-date. We will refrain from backporting changes to outdated versions except in very extreme
situations. This being said, we would **prefer** to not break plugin-facing API without good reason.
Between update cycles, effort must be kept to keep plugins functional. If you have a change that would
break plugin functionality, mark it as such and it will be reviewed during the next update cycle.
