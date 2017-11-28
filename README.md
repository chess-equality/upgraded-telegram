# ERC20 Token Smart Contract Testing

### Prerequisites:
1. JDK 1.8+
2. This project already Includes the Maven wrapper (`mvnw`).
3. A synced and running Ethereum client with accounts setup.
4. Set parameters in `application-<profile>.properties`. Leave the smart contract address for now; it will be set in the next section.
5. If using the testnet, note that `web3j` (https://web3j.io/), with which this project is using to connect to Ethereum, supports both Geth and Parity, however has had some issues dealing with asynchronous processing with Parity, at least with version 3.1.1 of `web3j` used in this project.
6. The issues above were not experienced with Parity in Geth compatibility mode (`--geth`) and most probably also with a pure Geth client.
7. Make sure the test accounts have sufficient Ether.

### Deploy smart contract:
1. Run the smart contract deployer: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=SmartContractDeployer test`. If on Linux, use `./mvnw`.
2. Take note of the smart contract address printed in the console.
3. Set the smart contract address in `application-<profile>.properties`.
4. If you need to update the smart contract code, please refer to `src/main/resources/contract/README.md`.

### Run the tests:
1. To run all tests (will run both test suites) in logical order: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=TestSuite_All test`
2. To run a specific test suite: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=TestSuite_Erc20 test`
3. To run a specific test and all its methods: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=PurchaseAndSellTest test`
4. To run a specific test method: `mvnw [-D"spring.profiles.active"=private|testnet] clean -Dtest=PurchaseAndSellTest#test1Purchase test`
5. If profile is not given, defaults to `private`.
