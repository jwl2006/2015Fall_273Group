package com.mongodb.operation;

import java.util.Arrays;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.spring.Entity.BootstrapRsp;


public class register_server {
	
	public void update(String epn, Document upd){
		
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		MongoDatabase database = mongoClient.getDatabase("register");
		MongoCollection<Document> collection = database.getCollection("device_status");
		collection.updateOne(new Document("endpoint_client_name",epn), new Document("$set",upd));
		//collection.replaceOne(new Document("endpoint_client_name",myDoc.get("endpoint_client_name")), myDoc);
		mongoClient.close();
	}
	
	public void singleupdate(String epn, String srcpath, String srcval){
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		MongoDatabase database = mongoClient.getDatabase("register");
		MongoCollection<Document> collection = database.getCollection("device_status");
		
		collection.updateOne(new Document("endpoint_client_name",epn), 
				new Document("$set", new Document(srcpath,srcval)));
		mongoClient.close();
	}
	
	public void dereg(Document upd){
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		MongoDatabase database = mongoClient.getDatabase("register");
		MongoCollection<Document> collection = database.getCollection("device_status");
		
		collection.deleteOne(upd);
		mongoClient.close();
	}
	
	public void insert(String rsgmsg){
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		MongoDatabase database = mongoClient.getDatabase("register");
		MongoCollection<Document> collection = database.getCollection("device_status");
		
		ObjectMapper mapper = new ObjectMapper();

		Document myDoc = Document.parse(rsgmsg);
		
		collection.deleteOne(new Document("endpoint_client_name",myDoc.get("endpoint_client_name")));
		collection.insertOne(myDoc);
		mongoClient.close();
		//collection.replaceOne(new Document("endpoint_client_name",myDoc.get("endpoint_client_name")), myDoc);
	}
	
	public Document find(String endpoint_client_name,BasicDBObject fields){
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		MongoDatabase database = mongoClient.getDatabase("register");
		MongoCollection<Document> collection = database.getCollection("device_status");

		Document doc = collection.find(new Document("endpoint_client_name",endpoint_client_name)).projection(fields).first();
		mongoClient.close();
		return doc;
	}
	
}
