package swim.vehicle;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import swim.api.agent.AbstractAgent;
import swim.concurrent.AbstractTask;
import swim.concurrent.TaskRef;
import swim.json.Json;
import swim.structure.Value;


public class MongoDbPollingAgent extends AbstractAgent {

  private final TaskRef pollTask = asyncStage().task(new AbstractTask() {
    @Override
    public void runTask() {
      poll();
    }
    @Override
    public boolean taskWillBlock() {
      return true;
    }
  });

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
    this.pollTask.cue();
  }

}
