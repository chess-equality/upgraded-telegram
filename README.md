# ERC20 Token Smart Contract Testing

### Prerequisites:
1. JDK 1.8+
2. This project already Includes the Maven wrapper (`mvnw`).
3. A synced and running Ethereum client with accounts setup.
4. Set parameters in `application-<profile>.properties`. Leave the smart contract address for now; it will be set in the next section.

### Deploy smart contract:
1. Run the smart contract deployer: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=SmartContractDeployer test`. If on Linux, use `./mvnw`.
2. Take note of the smart contract address printed in the console.
3. Set the smart contract address in `application-<profile>.properties`.
4. If you need to update the smart contract code, please refer to `src/main/resources/contract/README.md`.

### Run the tests:
1. To run all tests (will run both test suites): `mvnw [-D"spring.profiles.active"=private|testnet] clean test`
2. To run a specific test suite: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=TestSuite_Erc20 test`
3. To run a specific test and all its methods: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=TransferTest test`
4. To run a specific test method: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=SellTest#testSell test`
5. If profile is not given, defaults to `private`.
