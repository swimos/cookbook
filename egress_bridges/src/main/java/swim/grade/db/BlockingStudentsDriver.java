// Copyright 2015-2022 SWIM.AI inc.
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class BlockingStudentsDriver {

  private final Connection conn;
  private final String url;

  // singleton pattern
  private static BlockingStudentsDriver driver;

  private BlockingStudentsDriver(Connection conn, String url) {
    this.conn = conn;
    this.url = url;
  }

  public static void start(String host, String path, String usr, String pw)
          throws ClassNotFoundException, SQLException {
    // start() becomes restart() if BlockingDriver.driver already exists
    if (driver != null) {
      stop();
    }

    // Set up connection
    Class.forName("org.h2.Driver");
    final String url = "jdbc:h2:"
            + host
            + ((!host.endsWith("/") && !path.startsWith("/")) ? "/" : "")
            + path;
    System.out.println("[DEBUG] will start driver at " + url);
    driver = new BlockingStudentsDriver(DriverManager.getConnection(url, usr, pw), url);
    System.out.println("[INFO] Connection Established: "
            + driver.conn.getMetaData().getDatabaseProductName() + "/"
            + driver.conn.getCatalog());

    // Initialize table
    createStudentsTable();
  }

  private static void createStudentsTable() throws SQLException {
    final ResultSet res = driver.conn.getMetaData()
        .getTables(null, null, "STUDENTS", new String[]{"TABLE"});
    if (res.next()) {
      System.out.println("[WARN] Skipping STUDENTS table creation, as it already exists");
      return;
    }
    // Create STUDENTS table
    final String createMsg =
            "CREATE TABLE Students (" // database uppercases table name by default
                    + "id INT, "
                    + "f_name VARCHAR(20), "
                    + "l_name VARCHAR(20), "
                    + "points_earned INT, "
                    + "points_possible INT, " // to handle dropped assignments
                    + "PRIMARY KEY (id)"
                    + ");";
    final Statement createStmnt = driver.conn.createStatement();
    createStmnt.execute(createMsg);
    createStmnt.close();

    // Seed STUDENTS with values
    final PreparedStatement seed = driver.conn.prepareStatement("INSERT INTO STUDENTS VALUES (?, ?, ?, ?, ?)");
    seed(seed, 1, "Eilert", "Bronislav");
    seed(seed, 2, "Karolina", "Ilyas");
    seed(seed, 3, "Mitsuko", "Maryam");
    seed(seed, 4, "Drahomir", "Zebedaios");
    seed(seed, 5, "Mauritius", "Ettie");
    seed.close();
  }

  private static void seed(PreparedStatement seed,
                           int id, String f, String l) throws SQLException {
    seed.setInt(1, id);
    seed.setString(2, f);
    seed.setString(3, l);
    seed.setInt(4, 0);
    seed.setInt(5, 0);
    seed.execute();
  }

  public static void updateGrade(int id, int earned, int possible) {
    final String baseSql = String.format(
            "UPDATE STUDENTS "
                    + "SET points_earned = %d, points_possible = %d "
                    + "WHERE id = %d;", earned, possible, id);
    try (Statement stmt = driver.conn.createStatement()) {
      stmt.executeUpdate(baseSql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void logGrade(final int id) {
    final String sql = String.format("SELECT points_earned, points_possible FROM STUDENTS WHERE id = %d;", id);
    try (Statement stmt = driver.conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        System.out.println(rs.getInt("points_earned"));
        System.out.println(rs.getInt("points_possible"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  static void stop() throws SQLException {
    if (driver != null) {
      System.out.println("[DEBUG] Will stop driver");
      driver.conn.close();
      driver = null;
      System.out.println("[DEBUG] Did stop driver");
    }
  }

}
