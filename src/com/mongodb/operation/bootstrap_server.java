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

public class bootstrap_server {
		
		public Document find(String endpoint_client_name,BasicDBObject fields){


			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

			MongoDatabase database = mongoClient.getDatabase("bootstrap");
			MongoCollection<Document> collection = database.getCollection("device_list");
			
			Document doc = collection.find(new Document("endpoint_client_name",endpoint_client_name)).projection(fields).first();
			
			mongoClient.close();
			return doc;
		}
		
		public void update(BootstrapRsp btresult){
			

			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

			MongoDatabase database = mongoClient.getDatabase("bootstrap");
			MongoCollection<Document> collection = database.getCollection("device_list");
			
			collection.updateOne(new Document("endpoint_client_name",btresult.getEndpoint_client_name()), 
					new Document("$set",new Document("bootstrap_time_stamp",btresult.getBootstrap_time_stamp())));
			
			
			mongoClient.close();
		}
	
	
}
