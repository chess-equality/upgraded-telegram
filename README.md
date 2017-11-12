# ERC20 Token Smart Contract Testing

### Prerequisites
1. JDK 1.8+
2. Already Includes Maven wrapper (`mvnw`).
3. A synced and running Ethereum client with accounts setup.
4. Set parameters in `application-<profile>.properties`.

### Run
1. `./mvnw [-D"spring.profiles.active"=private|testnet] clean test`
2. If profile is not given, defaults to `private`.