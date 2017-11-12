# How to generate the Java smart contract wrapper class file

### Prerequisites
1. Reference: https://docs.web3j.io/smart_contracts.html
2. Solidity compiler (`solc`)
3. `web3j` installed in `PATH`

### Compile with `solc` to generate `.abi` and `.bin` files
`$ solc erc20_coin.sol --bin --abi --optimize -o build/`

### Generate Java wrapper class file
1. `$ web3j solidity generate build/Token.bin build/Token.abi -o build -p net.kreatious.ethereum.upgradedtelegram.contract.generated`
2. Copy generated class file to appropriate package in `src/main/java`.
3. Delete the generated class file here in `generated` folder.