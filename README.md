# Basin ![State](https://img.shields.io/badge/state-prototype-orange.svg) [![Latest Tag](https://img.shields.io/github/release/basinmc/basin.svg)](https://github.com/BasinMC/Basin/releases)

A modular Minecraft API.

### Requirements

* Java 1.8 or newer
* Maven 3.3 or newer
* Git in the executing shell's PATH

## Building

1. Execute `git clone --recursive --remote https://github.com/BasinMC/Basin.git` from your shell
1. Execute `mvn clean install`

## Writing Patches

1. Execute the steps described in [Building](#Building)
1. Check out master in both sink and faucet using `git checkout master`
1. Apply your changes to the NMS classes in `src/minecraft/java`
1. Commit your changes in `src/minecraft/java` (**Note:** Commit by purpose - No single files!)
1. Run `mvn org.basinmc.maven.plugins:minecraft-maven-plugin:generate-patches` from the `sink` directory

## Need Help?

The [official documentation][wiki] has help articles and specifications on the implementation. If, however, you still
require assistance with the application, you are welcome to join our [IRC Channel](#contact) and ask veteran users and
developers. Make sure to include a detailed description of your problem when asking questions though:

1. Include a complete error message along with its stack trace when applicable.
2. Describe the expected result.
3. Describe the actual result when different from the expected result.

[wiki]: https://github.com/BasinMC/Basin/wiki

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for information on working on Beacon and submitting patches. You can also join
the [project's chat room](#contact) to discuss future improvements or to get your custom implementation listed.

## Contact

**IRC:** irc.basinmc.org (port 6667 or port +6697) in [#Basin](irc://irc.basinmc.org/Basin)<br />
**Website:** https://www.basinmc.org
