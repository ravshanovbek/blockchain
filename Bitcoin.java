import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

class Transaction {
    private String transactionId;
    private String sender;
    private String receiver;
    private double amount;

    public Transaction(String sender, String receiver, double amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }
}

class Node {
    private String address;
    private double balance;
    private List<Transaction> transactions;
    // When a new node is created, it initializes these fields as follows:
    public Node(String address, double balance) {
        this.address = address;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }
    // This method allows a node to receive a transaction from another user.
    public void receiveTransaction(Transaction transaction) {
        if (transaction.getReceiver().equals(address)) {
            transactions.add(transaction);
            balance += transaction.getAmount();
            System.out.println("Received transaction: " + transaction.getTransactionId());
        }
    }
    // This method allows a node to send cryptocurrency to another user (node).
    public boolean sendTransaction(Node receiver, double amount) {
        if (balance >= amount) {
            Transaction transaction = new Transaction(address, receiver.getAddress(), amount);
            receiver.receiveTransaction(transaction);
            transactions.add(transaction);
            balance -= amount;
            System.out.println("Sent transaction: " + transaction.getTransactionId());
            return true;
        } else {
            System.out.println("Insufficient balance to send transaction.");
        }
        return false;
    }

    public String getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

class MaliciousNode extends Node {
    private double maliciousBalance;

    public MaliciousNode(String address, double balance, double maliciousBalance) {
        super(address, balance);
        this.maliciousBalance = maliciousBalance;
    }

    @Override
    public boolean sendTransaction(Node receiver, double amount) {
        if (maliciousBalance > 0) {
            double maliciousAmount = Math.min(amount, maliciousBalance);
            maliciousBalance -= maliciousAmount;
            Transaction maliciousTransaction = new Transaction(getAddress(), receiver.getAddress(), maliciousAmount);
            receiver.receiveTransaction(maliciousTransaction);
            System.out.println("Malicious node " + getAddress() + " sent a malicious transaction: " + maliciousTransaction.getTransactionId());
            return true;
        } else {
            System.out.println("Malicious node " + getAddress() + " does not have enough malicious balance to perform a malicious transaction.");
        }
        return super.sendTransaction(receiver, amount);
    }

    public void addMaliciousBalance(double amount) {
        maliciousBalance += amount;
        System.out.println("Malicious node " + getAddress() + " received malicious balance: " + amount);
    }

    public double getMaliciousBalance() {
        return maliciousBalance;
    }
}



class Miner extends Node {
    private double miningReward;
    private double miningDifficulty;

    public Miner(String address, double balance, double miningReward, double miningDifficulty) {
        super(address, balance);
        this.miningReward = miningReward;
        this.miningDifficulty = miningDifficulty;
    }

    public void mine(Blockchain blockchain) {
        String previousBlockHash = blockchain.getPreviousBlockHash();
        String newBlockHash = generateNewBlockHash(previousBlockHash);
        double rewardAmount = miningReward;

        if (rewardAmount > 0) {
            Transaction rewardTransaction = new Transaction("System", getAddress(), rewardAmount);
            receiveTransaction(rewardTransaction);
        }

        blockchain.addBlock(newBlockHash, getAddress());
        System.out.println("Mined block: " + newBlockHash);
    }

    private String generateNewBlockHash(String previousBlockHash) {
        // Implement logic to generate a new block hash using mining algorithms
        return UUID.randomUUID().toString();
    }

    public double getMiningDifficulty() {
        return miningDifficulty;
    }
}

class Blockchain {
    public List<List<String>> blockchains;
    private Map<String, Transaction> transactionMap;

    public Blockchain() {
        this.blockchains = new ArrayList<>();
        this.transactionMap = new HashMap<>();
    }

    public void addBlock(String newBlockHash, String minerAddress) {
        List<String> newBlock = new ArrayList<>();
        newBlock.add(newBlockHash);
        blockchains.add(newBlock);
        System.out.println("Block added to the blockchain by miner: " + minerAddress);
    }

    public void validateTransaction(Transaction transaction) {
        String transactionId = transaction.getTransactionId();
        if (!transactionMap.containsKey(transactionId)) {
            transactionMap.put(transactionId, transaction);
        }
    }

    public List<String> getLongestBlockchain() {
        int maxLength = 0;
        List<String> longestBlockchain = null;

        for (List<String> blockchain : blockchains) {
            if (blockchain.size() > maxLength) {
                maxLength = blockchain.size();
                longestBlockchain = blockchain;
            }
        }

        return longestBlockchain;
    }

    public String getPreviousBlockHash() {
        List<String> longestBlockchain = getLongestBlockchain();
        if (longestBlockchain != null && longestBlockchain.size() > 0) {
            return longestBlockchain.get(longestBlockchain.size() - 1);
        }
        return "";
    }
}

public class Bitcoin {
    public static void main(String[] args) {
        // Create blockchain
        Blockchain blockchain = new Blockchain();

        // Create nodes
        Node node1 = new Node("Address1", 10.0);
        Node node2 = new Node("Address2", 10.0);
        Node node3 = new Node("Address3", 10.0);
        Node node4 = new Node("Address4", 10.0);

        // Create miners
        Miner miner1 = new Miner("Miner1", 0.0, 10.0, 2.0);
        Miner miner2 = new Miner("Miner2", 0.0, 10.0, 1.5);
        Miner miner3 = new Miner("Miner3", 0.0, 10.0, 1.0);

        // Create malicious nodes
        MaliciousNode maliciousNode1 = new MaliciousNode("MaliciousNode1", 0.0, 5.0);
        MaliciousNode maliciousNode2 = new MaliciousNode("MaliciousNode2", 0.0, 5.0);

        // Add miners to the network
        List<Miner> miners = new ArrayList<>();
        miners.add(miner1);
        miners.add(miner2);
        miners.add(miner3);

        // Add nodes to the network
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(maliciousNode1);
        nodes.add(maliciousNode2);

        // Perform transactions
        node1.sendTransaction(node2, 5.0);
        node2.sendTransaction(node3, 3.0);
        node3.sendTransaction(node4, 2.0);
        node4.sendTransaction(node1, 4.0);

        // Simulate mining and block validation
        for (Miner miner : miners) {
            mineBlocks(miner, blockchain);
        }

        // Print node balances
        for (Node node : nodes) {
            System.out.println("Node Balance (" + node.getAddress() + "): " + node.getBalance());
        }
    }

    private static void mineBlocks(Miner miner, Blockchain blockchain) {
        double miningDifficulty = miner.getMiningDifficulty();
        double blockThreshold = 1.0 / miningDifficulty;
        Random random = new Random();

        while (true) {
            double rand = random.nextDouble();
            if (rand <= blockThreshold) {
                miner.mine(blockchain);
                break;
            }
        }

        // Select the longest blockchain
        List<String> longestBlockchain = blockchain.getLongestBlockchain();
        if (longestBlockchain != null) {
            blockchain.blockchains.clear();
            blockchain.blockchains.add(longestBlockchain);
            System.out.println("New longest blockchain selected: " + longestBlockchain);
        }
    }
}
