pragma solidity ^0.4.8;

// Implements https://github.com/ethereum/EIPs/blob/master/EIPS/eip-20-token-standard.md
contract Token {
    string public constant name = "My First Token";
    string public constant symbol = "KCT";
    uint8 public constant decimals = 18;
    
    uint256 private constant tokensPerWei = 2000;
    uint256 private reserved = 150000000 ether;
    uint256 private supply = 500000000 ether;
    
    address private owner;
    bool private paused;
    mapping(address => uint256) private balances;
    mapping(address => mapping (address => uint256)) private allowed;
    
    // Protection against the ERC20 short address attack
    modifier args(uint8 count) {
        assert(msg.data.length == 32 * count + 4);
        _;
    }
    
    // Modifies the function so that only the contract owner can execute it
    modifier ownerOnly() {
        require(msg.sender == owner);
        _;
    }
    
    // Modifies the function so that it only works when unpaused
    modifier unpaused() {
        require(!paused);
        _;
    }
 
    // Instantiates a new Token
    function Token() public {
        owner = msg.sender;
        balances[owner] = supply;
    }
   
    // Gets the balance of the specified account
    function balanceOf(address _owner) public constant returns (uint256 balance) {
        return balances[_owner];
    }
    
    // Gets the total supply of tokens
    function totalSupply() public constant returns (uint256 total) {
        return supply - balances[0];
    }
    
    // Transfers some of the caller's tokens to the specified address
    function transfer(address _to, uint256 _amount) public unpaused returns (bool success) {
        require(balances[msg.sender] >= _amount);
        require(balances[_to] + _amount > balances[_to]);
    
        balances[msg.sender] -= _amount;
        balances[_to] += _amount;
        
        Transfer(msg.sender, _to, _amount);
        return true;
    }
    
    // Transfers tokens between addresses, if authorization was given
    function transferFrom(address _from, address _to, uint256 _amount) public args(3) unpaused returns (bool success) {
        require(balances[_to] + _amount > balances[_to]);
        require(balances[_from] >= _amount);
        require(allowed[_from][msg.sender] >= _amount);
        
        balances[_from] -= _amount;
        allowed[_from][msg.sender] -= _amount;
        balances[_to] += _amount;
        
        Transfer(_from, _to, _amount);
        return true;
    }
    
    // Allows the specified account to withdraw tokens from the caller's account
    function approve(address _spender, uint256 _amount) public args(2) unpaused returns (bool success) {
        allowed[msg.sender][_spender] = _amount;
        Approval(msg.sender, _spender, _amount);
        return true;
    }
    
    // Gets the number of tokens that are authorized to be transferred between the specified accounts
    function allowance(address _owner, address _spender) public constant returns (uint256 remaining) {
        return allowed[_owner][_spender];
    }
    
    // Triggered on any successful call to a transfer method
    event Transfer(address indexed _from, address indexed _to, uint256 _value);
    
    // Triggered on any successful call to the approve method
    event Approval(address indexed _owner, address indexed _spender, uint256 _value);
    
    // Purchases tokens in exchange for wei
    function purchase() public payable unpaused {
        require(msg.value > 0);
        require(balances[owner] >= reserved);
        
        var tokens = msg.value * tokensPerWei;
        require(balances[owner] - reserved >= tokens);
        balances[owner] -= tokens;
        balances[msg.sender] += tokens;
        
        Transfer(owner, msg.sender, tokens);
    }
    
    // Sells tokens in exchange for wei
    function sell(uint256 _amount) public unpaused returns (bool success) {
        require(balances[msg.sender] >= _amount);
        require(this.balance >= _amount / tokensPerWei);
        require(_amount % tokensPerWei == 0);
        
        balances[msg.sender] -= _amount;
        balances[owner] += _amount;
        Transfer(msg.sender, owner, _amount);
        
        msg.sender.transfer(_amount / tokensPerWei);
        return true;
    }
    
    // Mints new tokens
    function mint(uint256 _amount) public unpaused ownerOnly returns (bool success) {
        balances[msg.sender] += _amount;
        supply += _amount;
        
        Transfer(0, msg.sender, _amount);
        return true;
    }
    
    // Deposits some ETH into the contract
    function deposit() public payable ownerOnly returns (bool success) {
        return true;
    }
    
    // Withdraws the current balance of ETH tokens
    function withdraw(uint256 _value) public ownerOnly returns (bool success) {
        require(this.balance >= _value);
        owner.transfer(_value);
        return true;
    }
    
    // Pauses the contract
    function setPaused(bool _value) public ownerOnly {
        paused = _value;
    }
    
    // Changes the amount of tokens being held in reserve
    function setReserved(uint256 _amount) public unpaused ownerOnly returns (bool success) {
        reserved = _amount;
        return true;
    }
    
    // Terminates the contract.
    function kill() public ownerOnly {
        selfdestruct(owner);
    }
}