package net.kreatious.ethereum.upgradedtelegram.contract.generated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.1.1.
 */
public final class Token extends Contract {
    private static final String BINARY = "60606040526a7c13bc4b2c133c560000006000556b019d971e4fe8401e74000000600155341561002e57600080fd5b60028054600160a060020a03338116600160a060020a031990921691909117918290556001549116600090815260036020526040902055610a9a806100746000396000f3006060604052600436106100f05763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde038114610111578063095ea7b31461019b57806316c38b3c146101d157806318160ddd146101e957806323b872dd1461020e5780632d6e71b6146102365780632e1a7d4d1461024c578063313ce5671461026257806341c0e1b51461028b57806364edfbf01461010757806370a082311461029e57806395d89b41146102bd578063a0712d68146102d0578063a9059cbb146102e6578063d0e30db014610308578063dd62ed3e14610310578063e4849b3214610335575b60025460a060020a900460ff161561010757600080fd5b61010f61034b565b005b341561011c57600080fd5b61012461042d565b60405160208082528190810183818151815260200191508051906020019080838360005b83811015610160578082015183820152602001610148565b50505050905090810190601f16801561018d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101a657600080fd5b6101bd600160a060020a0360043516602435610464565b604051901515815260200160405180910390f35b34156101dc57600080fd5b61010f60043515156104f3565b34156101f457600080fd5b6101fc61053d565b60405190815260200160405180910390f35b341561021957600080fd5b6101bd600160a060020a036004358116906024351660443561056f565b341561024157600080fd5b6101bd60043561068f565b341561025757600080fd5b6101bd6004356106cd565b341561026d57600080fd5b61027561073e565b60405160ff909116815260200160405180910390f35b341561029657600080fd5b61010f610743565b34156102a957600080fd5b6101fc600160a060020a036004351661076c565b34156102c857600080fd5b610124610787565b34156102db57600080fd5b6101bd6004356107be565b34156102f157600080fd5b6101bd600160a060020a0360043516602435610843565b6101bd610905565b341561031b57600080fd5b6101fc600160a060020a0360043581169060243516610929565b341561034057600080fd5b6101bd600435610954565b60025460009060a060020a900460ff161561036557600080fd5b6000341161037257600080fd5b60008054600254600160a060020a031682526003602052604090912054101561039a57600080fd5b5060008054600254600160a060020a031682526003602052604090912054346107d002919003819010156103cd57600080fd5b60028054600160a060020a03908116600090815260036020526040808220805486900390553383168083529181902080548601905592549092911690600080516020610a4f8339815191529084905190815260200160405180910390a350565b60408051908101604052600e81527f4d7920466972737420546f6b656e000000000000000000000000000000000000602082015281565b600060023660441461047257fe5b60025460a060020a900460ff161561048957600080fd5b600160a060020a03338116600081815260046020908152604080832094891680845294909152908190208690557f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259086905190815260200160405180910390a35060019392505050565b60025433600160a060020a0390811691161461050e57600080fd5b6002805491151560a060020a0274ff000000000000000000000000000000000000000019909216919091179055565b6000805260036020527f3617319a054d772f909f7c479a2cebe5066e836a939412e32403c99029b92eff546001540390565b600060033660641461057d57fe5b60025460a060020a900460ff161561059457600080fd5b600160a060020a038416600090815260036020526040902054838101116105ba57600080fd5b600160a060020a038516600090815260036020526040902054839010156105e057600080fd5b600160a060020a03808616600090815260046020908152604080832033909416835292905220548390101561061457600080fd5b600160a060020a03808616600081815260036020818152604080842080548a90039055600482528084203387168552825280842080548a900390559489168084529190529083902080548701905591600080516020610a4f8339815191529086905190815260200160405180910390a3506001949350505050565b60025460009060a060020a900460ff16156106a957600080fd5b60025433600160a060020a039081169116146106c457600080fd5b50600055600190565b60025460009033600160a060020a039081169116146106eb57600080fd5b600160a060020a033016318290101561070357600080fd5b600254600160a060020a031682156108fc0283604051600060405180830381858888f19350505050151561073657600080fd5b506001919050565b601281565b60025433600160a060020a0390811691161461075e57600080fd5b600254600160a060020a0316ff5b600160a060020a031660009081526003602052604090205490565b60408051908101604052600381527f4b43540000000000000000000000000000000000000000000000000000000000602082015281565b60025460009060a060020a900460ff16156107d857600080fd5b60025433600160a060020a039081169116146107f357600080fd5b600160a060020a0333166000818152600360205260408082208054860190556001805486019055600080516020610a4f8339815191529085905190815260200160405180910390a3506001919050565b60025460009060a060020a900460ff161561085d57600080fd5b600160a060020a0333166000908152600360205260409020548290101561088357600080fd5b600160a060020a038316600090815260036020526040902054828101116108a957600080fd5b600160a060020a03338116600081815260036020526040808220805487900390559286168082529083902080548601905591600080516020610a4f8339815191529085905190815260200160405180910390a350600192915050565b60025460009033600160a060020a0390811691161461092357600080fd5b50600190565b600160a060020a03918216600090815260046020908152604080832093909416825291909152205490565b60025460009060a060020a900460ff161561096e57600080fd5b600160a060020a0333166000908152600360205260409020548290101561099457600080fd5b6107d08204600160a060020a0330163110156109af57600080fd5b6107d08206156109be57600080fd5b600160a060020a0333811660008181526003602052604080822080548790039055600280548516835291819020805487019055905490921691600080516020610a4f8339815191529085905190815260200160405180910390a333600160a060020a03166107d0830480156108fc0290604051600060405180830381858888f19350505050151561073657600080fd00ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3efa165627a7a723058208e08d4ee1d5241a611a94d495f76530846209ddfc0a50c21ea76b37580ef9df00029";

    private Token(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Token(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Approval", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Approval", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> name() {
        Function function = new Function("name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _amount) {
        Function function = new Function(
                "approve", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_spender), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setPaused(Boolean _value) {
        Function function = new Function(
                "setPaused", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Bool(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        Function function = new Function("totalSupply", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _amount) {
        Function function = new Function(
                "transferFrom", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from), 
                new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setReserved(BigInteger _amount) {
        Function function = new Function(
                "setReserved", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> withdraw(BigInteger _value) {
        Function function = new Function(
                "withdraw", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        Function function = new Function("decimals", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> kill() {
        Function function = new Function(
                "kill", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> purchase(BigInteger weiValue) {
        Function function = new Function(
                "purchase", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        Function function = new Function("balanceOf", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> symbol() {
        Function function = new Function("symbol", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> mint(BigInteger _amount) {
        Function function = new Function(
                "mint", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _amount) {
        Function function = new Function(
                "transfer", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> deposit(BigInteger weiValue) {
        Function function = new Function(
                "deposit", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        Function function = new Function("allowance", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.Address(_spender)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> sell(BigInteger _amount) {
        Function function = new Function(
                "sell", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Token> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Token.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Token> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Token.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Token load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Token(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Token load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Token(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TransferEventResponse {
        public String _from;

        public String _to;

        public BigInteger _value;
    }

    public static class ApprovalEventResponse {
        public String _owner;

        public String _spender;

        public BigInteger _value;
    }
}
