## Test Coverage

### I. ERC20:

#### `transfer`:
1. Transfer by contract owner
2. Transfer by non-owner
3. Transfer by non-owner to himself
4. Transfer by non-owner who has not bought any tokens

#### `approve` and `transferFrom`:
1. Approve limit
2. Transfer within approved limit
3. Several transfers until approved limit reached 
4. Transfer above approved limit
5. Transfer 0 tokens

### II. Extensions:

#### `purchase` and `sell`:
1. Purchase tokens
2. Sell the purchased tokens
3. Sell without purchasing
4. Purchase then oversell
5. Sell tokens not divisible with rate
6. Purchase with 0 Ether

#### `mint`:
1. Minting by contract owner
2. Minting by non-owner

#### `deposit` and `withdraw`:
1. Deposit and withdraw by contract owner
2. Deposit and withdraw by non-owner

#### `pause`:
1. Pause contract then try to transfer

#### `set reserved`:
1. Reserve new number of tokens

### III. Not included in main tests:
1. Deploy contract
2. Kill contract then try to transfer
