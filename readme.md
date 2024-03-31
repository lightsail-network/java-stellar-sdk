# java-stellar-sdk

[![Test and Deploy](https://github.com/lightsail-network/java-stellar-sdk/actions/workflows/test-deploy.yml/badge.svg?branch=master)](https://github.com/lightsail-network/java-stellar-sdk/actions/workflows/test-deploy.yml)
[![Maven Central Version](https://img.shields.io/maven-central/v/network.lightsail/stellar-sdk)](https://central.sonatype.com/artifact/network.lightsail/stellar-sdk)
[![javadoc](https://javadoc.io/badge2/network.lightsail/stellar-sdk/javadoc.svg)](https://javadoc.io/doc/network.lightsail/stellar-sdk)

The Java Stellar SDK library provides APIs to build transactions and connect to [Horizon](https://github.com/lightsail-network/go/tree/master/services/horizon) and [Soroban-RPC Server](https://soroban.stellar.org/docs/reference/rpc).

## Installation

### Apache Maven

```xml
<dependency>
    <groupId>network.lightsail</groupId>
    <artifactId>stellar-sdk</artifactId>
    <version>0.43.1</version>
</dependency>
```

### Gradle
```groovy
implementation 'network.lightsail:stellar-sdk:0.43.1'
```

You can find instructions on how to install this dependency using alternative package managers [here](https://central.sonatype.com/artifact/network.lightsail/stellar-sdk).

### JAR

Download the latest jar from the GitHub repo's [releases tab](https://github.com/lightsail-network/java-stellar-sdk/releases). Add the `jar` package to your project according to how your environment is set up.

## Basic Usage
For some examples on how to use this library, take a look at the [Get Started docs in the developers site](https://developers.stellar.org/docs/tutorials/create-account/).

## Documentation
Javadoc is available at https://javadoc.io/doc/network.lightsail/stellar-sdk

## Integrate into Android project
If you want to integrate this SDK on Android platforms with API level 26 and above, you don't need any additional configuration. 
However, if you need to include it on lower platforms, you may also need to add the [Java Stellar SDK Android SPI](https://github.com/lightsail-network/java-stellar-sdk-android-spi).

## Contributing
For information on how to contribute, please refer to our [contribution guide](https://github.com/lightsail-network/java-stellar-sdk/blob/master/CONTRIBUTING.md).

## License
java-stellar-sdk is licensed under an Apache-2.0 license. See the [LICENSE](https://github.com/lightsail-network/java-stellar-sdk/blob/master/LICENSE) file for details.