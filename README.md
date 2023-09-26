# blockchain
This Java code represents a simplified simulation of a blockchain-based cryptocurrency system, similar to Bitcoin. It consists of several classes representing different components of the system, including transactions, nodes (users), miners, a blockchain, and malicious nodes. Below is an explanation of each class and its functionality

1. `Transaction` Class:
   - Represents a cryptocurrency transaction with a unique transaction ID, sender, receiver, and amount.
   - The constructor initializes the transaction ID using a random UUID, sender, receiver, and amount.

2. `Node` Class:
   - Represents a user in the cryptocurrency network with an address, balance, and a list of transactions.
   - Users can receive transactions from others and send transactions if they have a sufficient balance.
   - The `receiveTransaction` method adds incoming transactions to the user's transaction list and updates their balance.
   - The `sendTransaction` method allows a user to send cryptocurrency to another user, if they have a sufficient balance.
   - Users are identified by their unique address.

3. `MaliciousNode` Class (inherits from `Node`):
   - Extends the `Node` class to simulate malicious nodes.
   - Overrides the `sendTransaction` method to allow malicious nodes to perform malicious transactions up to a certain limit.
   - Malicious nodes can receive a malicious balance and use it to send malicious transactions.

4. `Miner` Class (inherits from `Node`):
   - Represents miners in the network who participate in mining new blocks.
   - Miners have a mining reward, mining difficulty, and can mine new blocks by adding them to the blockchain.
   - The `mine` method simulates the process of mining a new block by generating a new block hash.
   - Miners receive a mining reward for successfully mining a block.

5. `Blockchain` Class:
   - Represents the blockchain ledger containing a list of blocks.
   - Each block is represented as a list of block hashes.
   - The blockchain also keeps track of transactions using a `transactionMap`.
   - The `addBlock` method adds a new block to the blockchain.
   - The `validateTransaction` method ensures that transactions are unique.
   - The `getLongestBlockchain` method finds the longest blockchain in the network.
   - The `getPreviousBlockHash` method returns the hash of the most recent block.

6. `Bitcoin` Class (Main Class):
   - The main class for running the simulation.
   - Creates a blockchain and initializes various nodes, miners, and malicious nodes.
   - Simulates a series of transactions between nodes.
   - Simulates mining by miners, where they attempt to add blocks to the blockchain.
   - Finally, it prints the balances of all nodes in the network.

In this simulation, users (nodes) can send and receive cryptocurrency, miners can mine new blocks to add to the blockchain, and malicious nodes can perform malicious transactions. The blockchain maintains the ledger of transactions and blocks.
