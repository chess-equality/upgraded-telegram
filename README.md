# ERC20 Token Smart Contract Testing

### Prerequisites
1. JDK 1.8+
2. Already Includes Maven wrapper (`mvnw`).
3. A synced and running Ethereum client with accounts setup.
4. Set parameters in `application-<profile>.properties`.

### Run
1. To run all tests (will run both test suites): `mvnw [-D"spring.profiles.active"=private|testnet] clean test`
2. To run a specific test suite: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=TestSuite_Erc20 test`
3. To run a specific test: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=PositiveTests_Erc20 test`
4. If profile is not given, defaults to `private`.
5. If on Linux, use `./mvnw`.
