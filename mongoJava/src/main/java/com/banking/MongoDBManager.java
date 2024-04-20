package com.banking;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBManager {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> accountsCollection;
    private static final String COLLECTION_NAME = "accounts";

    public MongoDBManager() {
        // Connect to MongoDB
        mongoClient = MongoClients.create();
        database = mongoClient.getDatabase("bank");
        this.accountsCollection = database.getCollection(COLLECTION_NAME);
    }

    public void closeConnection() {
        mongoClient.close();
    }

    public void insertAccount(BankAccount account) {
        Document document = new Document()
                .append("accountNumber", account.getAccountNumber())
                .append("accountHolder", account.getAccountHolder())
                .append("balance", account.getBalance());
        accountsCollection.insertOne(document);
    }

    public Document findAccountByAccountNumber(String accountNumber) {
        return accountsCollection.find(new Document("accountNumber", accountNumber)).first();
    }

    public void updateAccount(BankAccount account) {
        accountsCollection.updateOne(new Document("accountNumber", account.getAccountNumber()),
                new Document("$set", new Document("balance", account.getBalance())));
    }
    
    public void createAccount(String accountNumber, String accountHolder, double balance) {
        // Create a new document representing the account
        Document accountDocument = new Document()
                .append("accountNumber", accountNumber)
                .append("accountHolder", accountHolder)
                .append("balance", balance);

        // Insert the document into the collection
        accountsCollection.insertOne(accountDocument);

        System.out.println("Account created successfully.");
    }
}

