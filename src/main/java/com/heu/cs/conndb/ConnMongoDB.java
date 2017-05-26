package com.heu.cs.conndb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


/**
 * Created by memgq on 2017/5/14.
 */
public class ConnMongoDB {
    MongoClient mongoClient = new MongoClient("localhost",27017);

    public ConnMongoDB( ){ }

    public ConnMongoDB(String host, int port){
        this.mongoClient=new MongoClient(host,port);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoCollection getCollection(String databaseName, String collectionName){
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        return collection;
    }
}
