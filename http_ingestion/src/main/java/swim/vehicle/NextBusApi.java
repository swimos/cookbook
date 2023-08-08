package swim.vehicle;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.zip.GZIPInputStream;
import swim.codec.Utf8;
import swim.xml.Xml;
import swim.structure.Value;

public final class NextBusApi {

  private NextBusApi() {
  }

  private static final String ENDPOINT_FMT = "https://retro.umoiq.com/service/publicXMLFeed?command=vehicleLocations&a=%s&t=%d";

  private static String endpointForAgency(String agency, long since) {
    return String.format(ENDPOINT_FMT, agency, since);
  }

  private static HttpRequest requestForEndpoint(String endpoint) {
    return HttpRequest.newBuilder(URI.create(endpoint))
        .GET()
        .headers("Accept-Encoding", "gzip")
        .build();
  }

  public static Value getVehiclesForAgency(HttpClient executor, String agency, long since) {
    final HttpRequest request = requestForEndpoint(endpointForAgency(agency, since));
    try {
      final HttpResponse<InputStream> response = executor.send(request, HttpResponse.BodyHandlers.ofInputStream());
      return Utf8.read(new GZIPInputStream(response.body()), Xml.structureParser().documentParser());
      // Alternatively: convert GZIPInputStream to String, then invoke the more familiar Xml.parse()
    } catch (Exception e) {
      e.printStackTrace();
      return Value.absent();
    }
  }

}
