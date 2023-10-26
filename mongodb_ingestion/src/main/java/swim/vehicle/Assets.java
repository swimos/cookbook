package swim.vehicle;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public final class Assets {

  private Assets() {
  }

  private static MongoClient mongoClient;

  public static MongoClient mongoClient() {
    return Assets.mongoClient;
  }

  private static MongoClient loadMongoClient() {
    // Here we can configure the MongoClient with additional settings - perhaps loaded from a properties file
    return MongoClients.create("mongodb://myConnectionString");
  }

  public static void init() {
    Assets.mongoClient = loadMongoClient();
  }

}
