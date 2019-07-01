// Copyright 2015-2019 SWIM.AI inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package swim.grade.db;

import java.sql.SQLException;
import org.h2.tools.Server;

public class Database {

  private static Server server;
  private static String port;

  public static void start(int port) throws SQLException {
    System.out.println("[DEBUG] will start database on port " + port);
    if (server != null) {
      stop();
    }
    final String portStr = String.valueOf(port);
    Database.port = portStr;
    server = Server.createTcpServer("-tcpPort", portStr, "-tcpAllowOthers", "-ifNotExists");
    server.start();
    System.out.println("[DEBUG] did start database on port " + port);
  }

  public static void stop() {
    if (server != null) {
      System.out.println("[DEBUG] will stop database");
      server.stop();
      server = null;
      port = null;
      System.out.println("[DEBUG] did stop database");
    }
  }

  public static void main(String[] args) throws SQLException {

    // Ensure Database shutdown before exit
    Runtime.getRuntime()
        .addShutdownHook(new Thread(Database::stop));

    Database.start(9002);

    // For demo purposes, use driver to seed database
    try {
      BlockingCustomDriver.start("tcp://localhost:9002", "~/test", "sa", "");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      BlockingCustomDriver.stop();
    }
  }
}
