# How to generate the Java smart contract wrapper class file

### Prerequisites
1. Reference: https://docs.web3j.io/smart_contracts.html
2. Solidity compiler (`solc`)
3. `web3j` installed in `PATH`

### Compile with `solc` to generate `.abi` and `.bin` files
1. Open console then change directory to token folder: `cd token`
2. `$ solc erc20_coin.sol --bin --abi --optimize -o build/`

### Generate Java wrapper class file
1. While in token folder, run: `$ web3j solidity generate build/Token.bin build/Token.abi -o generated -p net.kreatious.ethereum.upgradedtelegram.contract.generated`
2. Copy generated class file to appropriate package in `src/main/java`. <b>NOTE</b>: If you are meaning to "kill" the old contract first before deploying the new, please run the `SmartContractKiller` test first before updating the generated class file. 
3. Delete the generated class file here in `generated` folder.
