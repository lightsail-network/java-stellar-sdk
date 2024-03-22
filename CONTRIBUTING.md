# How to contribute

👍🎉 First off, thanks for taking the time to contribute! 🎉👍

Check out the [Stellar Contribution Guide](https://github.com/stellar/.github/blob/master/CONTRIBUTING.md) that apply to
all Stellar projects.

## What software do I need to install

- Java 11 or higher
- Docker

## How to Test

Run the command `./gradlew check` to run all tests.

## How to Build

Run the command `./gradlew build` to build it. All jar files will be located in the `build/libs` directory, and the
documentation can be found in the `javadoc` folder.

## How to Update XDR Related Files

- Please check the [Makefile](Makefile) file. In general, you only need to update the value of `XDRNEXT`.
- Run the command `make xdr-update` to automatically download the XDR files and generate the corresponding Java
  files. All generated Java files will be located in the `org.stellar.sdk.xdr` package.

## How to Install Git Hook

Run the command `./gradlew updateGitHook` to automatically install the Git Hook. Every time you commit your code, it
  will automatically check if the code is formatted. If not, it will automatically format the code.

## How to Publish

- First, you need to modify the [build.gradle.kts](build.gradle.kts) file and change the value of `version` to the
  version number you want to publish. Then, submit it to GitHub.
- Create a new Release on GitHub with the same version number as the previous step's `version`. CI will automatically
  build and publish it to [jitpack.io](https://jitpack.io/#stellar/java-stellar-sdk) and the GitHub Release page.

## Code Style

- Use camel case style.
- Avoid using wildcard imports.
- If possible, use Lombok.

### Pull Requests

- PRs must update the [CHANGELOG.md](CHANGELOG.md) with a small description of the change
- PRs are merged into master or release branch using squash merge
- Keep PR scope narrow.
- Explicitly differentiate refactoring PRs and feature PRs. Refactoring PRs don’t change functionality. They usually
  touch a lot more code, and are reviewed in less detail. Avoid refactoring in feature PRs.