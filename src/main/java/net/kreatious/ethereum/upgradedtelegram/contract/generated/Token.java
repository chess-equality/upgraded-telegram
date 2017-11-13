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
 * <p>Generated with web3j version 3.0.2.
 */
public final class Token extends Contract {
    private static final String BINARY = "60606040526a7c13bc4b2c133c560000006000556b019d971e4fe8401e74000000600155341561002e57600080fd5b60028054600160a060020a03338116600160a060020a031990921691909117918290556001549116600090815260036020526040902055610a88806100746000396000f3006060604052600436106100f05763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde0381146100f5578063095ea7b31461017f57806316c38b3c146101b557806318160ddd146101cf57806323b872dd146101f45780632d6e71b61461021c5780632e1a7d4d14610232578063313ce5671461024857806341c0e1b51461027157806364edfbf01461028457806370a082311461028c57806395d89b41146102ab578063a0712d68146102be578063a9059cbb146102d4578063d0e30db0146102f6578063dd62ed3e146102fe578063e4849b3214610323575b600080fd5b341561010057600080fd5b610108610339565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561014457808201518382015260200161012c565b50505050905090810190601f1680156101715780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561018a57600080fd5b6101a1600160a060020a0360043516602435610370565b604051901515815260200160405180910390f35b34156101c057600080fd5b6101cd60043515156103ff565b005b34156101da57600080fd5b6101e2610449565b60405190815260200160405180910390f35b34156101ff57600080fd5b6101a1600160a060020a036004358116906024351660443561047b565b341561022757600080fd5b6101a160043561059b565b341561023d57600080fd5b6101a16004356105d9565b341561025357600080fd5b61025b61064a565b60405160ff909116815260200160405180910390f35b341561027c57600080fd5b6101cd61064f565b6101cd610678565b341561029757600080fd5b6101e2600160a060020a036004351661075a565b34156102b657600080fd5b610108610775565b34156102c957600080fd5b6101a16004356107ac565b34156102df57600080fd5b6101a1600160a060020a0360043516602435610831565b6101a16108f3565b341561030957600080fd5b6101e2600160a060020a0360043581169060243516610917565b341561032e57600080fd5b6101a1600435610942565b60408051908101604052600e81527f4d7920466972737420546f6b656e000000000000000000000000000000000000602082015281565b600060023660441461037e57fe5b60025460a060020a900460ff161561039557600080fd5b600160a060020a03338116600081815260046020908152604080832094891680845294909152908190208690557f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259086905190815260200160405180910390a35060019392505050565b60025433600160a060020a0390811691161461041a57600080fd5b6002805491151560a060020a0274ff000000000000000000000000000000000000000019909216919091179055565b6000805260036020527f3617319a054d772f909f7c479a2cebe5066e836a939412e32403c99029b92eff546001540390565b600060033660641461048957fe5b60025460a060020a900460ff16156104a057600080fd5b600160a060020a038416600090815260036020526040902054838101116104c657600080fd5b600160a060020a038516600090815260036020526040902054839010156104ec57600080fd5b600160a060020a03808616600090815260046020908152604080832033909416835292905220548390101561052057600080fd5b600160a060020a03808616600081815260036020818152604080842080548a90039055600482528084203387168552825280842080548a900390559489168084529190529083902080548701905591600080516020610a3d8339815191529086905190815260200160405180910390a3506001949350505050565b60025460009060a060020a900460ff16156105b557600080fd5b60025433600160a060020a039081169116146105d057600080fd5b50600055600190565b60025460009033600160a060020a039081169116146105f757600080fd5b600160a060020a033016318290101561060f57600080fd5b600254600160a060020a031682156108fc0283604051600060405180830381858888f19350505050151561064257600080fd5b506001919050565b601281565b60025433600160a060020a0390811691161461066a57600080fd5b600254600160a060020a0316ff5b60025460009060a060020a900460ff161561069257600080fd5b6000341161069f57600080fd5b60008054600254600160a060020a03168252600360205260409091205410156106c757600080fd5b5060008054600254600160a060020a031682526003602052604090912054346107d002919003819010156106fa57600080fd5b60028054600160a060020a03908116600090815260036020526040808220805486900390553383168083529181902080548601905592549092911690600080516020610a3d8339815191529084905190815260200160405180910390a350565b600160a060020a031660009081526003602052604090205490565b60408051908101604052600381527f4b43540000000000000000000000000000000000000000000000000000000000602082015281565b60025460009060a060020a900460ff16156107c657600080fd5b60025433600160a060020a039081169116146107e157600080fd5b600160a060020a0333166000818152600360205260408082208054860190556001805486019055600080516020610a3d8339815191529085905190815260200160405180910390a3506001919050565b60025460009060a060020a900460ff161561084b57600080fd5b600160a060020a0333166000908152600360205260409020548290101561087157600080fd5b600160a060020a0383166000908152600360205260409020548281011161089757600080fd5b600160a060020a03338116600081815260036020526040808220805487900390559286168082529083902080548601905591600080516020610a3d8339815191529085905190815260200160405180910390a350600192915050565b60025460009033600160a060020a0390811691161461091157600080fd5b50600190565b600160a060020a03918216600090815260046020908152604080832093909416825291909152205490565b60025460009060a060020a900460ff161561095c57600080fd5b600160a060020a0333166000908152600360205260409020548290101561098257600080fd5b6107d08204600160a060020a03301631101561099d57600080fd5b6107d08206156109ac57600080fd5b600160a060020a0333811660008181526003602052604080822080548790039055600280548516835291819020805487019055905490921691600080516020610a3d8339815191529085905190815260200160405180910390a333600160a060020a03166107d0830480156108fc0290604051600060405180830381858888f19350505050151561064257600080fd00ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3efa165627a7a723058206b9929e8153a2927c57b76b0cf40cc68408c0e0a5ecc6e04f70d59f6857f97460029";

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
