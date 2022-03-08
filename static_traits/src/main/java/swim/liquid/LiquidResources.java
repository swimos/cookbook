package swim.liquid;

import swim.codec.ParserException;
import swim.codec.Utf8;
import swim.json.Json;
import swim.recon.Recon;
import swim.structure.Value;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utilities for loading and parsing configuration resources.
 */
public final class LiquidResources {
  private LiquidResources() {
    // static
  }

  public static Value loadJsonResource(String jsonResource) {
    Value jsonValue = null;
    InputStream jsonInput = null;
    try {
      jsonInput = LiquidPlane.class.getClassLoader().getResourceAsStream(jsonResource);
      if (jsonInput != null) {
        jsonValue = Utf8.read(Json.structureParser().valueParser(), jsonInput);
      }
    } catch (IOException cause) {
      throw new ParserException(cause);
    } finally {
      try {
        if (jsonInput != null) {
          jsonInput.close();
        }
      } catch (IOException swallow) {
      }
    }
    return jsonValue;
  }

  public static Value loadReconResource(String reconResource) {
    Value reconValue = null;
    InputStream reconInput = null;
    try {
      reconInput = LiquidPlane.class.getClassLoader().getResourceAsStream(reconResource);
      if (reconInput != null) {
        reconValue = Utf8.read(Recon.structureParser().blockParser(), reconInput);
      }
    } catch (IOException cause) {
      throw new ParserException(cause);
    } finally {
      try {
        if (reconInput != null) {
          reconInput.close();
        }
      } catch (IOException swallow) {
      }
    }
    return reconValue;
  }
}
