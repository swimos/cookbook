package swim.vehicle;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import swim.api.agent.AbstractAgent;
import swim.json.Json;
import swim.structure.Value;

public class MongoDbPollingAgent extends AbstractAgent {

  private FindIterable<Document> find() {
    return Assets.mongoClient().getDatabase("myDatabase")
        .getCollection("myCollection")
        .find();
  }

  private void poll() {
    try (MongoCursor<Document> cursor = find().cursor()) {
      while (cursor.hasNext()) {
        processDocument(cursor.next());
      }
    }
  }

  private void processDocument(final Document document) {
    final Value body = Json.parse(document.toJson());
    final String nodeUri = "/vehicle/" + body.get("id").longValue();
    command(nodeUri, "addMessage", body);
  }

  @Override
  public void didStart() {
    asyncStage().task(this::poll).cue();
  }

}
