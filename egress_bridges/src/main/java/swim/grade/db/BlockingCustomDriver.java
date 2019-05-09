package swim.grade.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class BlockingCustomDriver {

  private Connection conn;
  private String url;

  private BlockingCustomDriver(Connection conn, String url) {
    this.conn = conn;
    this.url = url;
  }

  private static BlockingCustomDriver driver;

  static void start(String host, String path, String usr, String pw)
      throws ClassNotFoundException, SQLException {
    // `start` becomes `restart` if `driver` already exists
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
    driver = new BlockingCustomDriver(DriverManager.getConnection(url, usr, pw), url);
    System.out.println("[INFO] Connection Established: "
        + driver.conn.getMetaData().getDatabaseProductName() + "/"
        + driver.conn.getCatalog());

    // Initialize table
    createStudentsTable();
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

  // throws NPE if called before start()
  static void createStudentsTable() throws SQLException {
    ResultSet res = driver.conn.getMetaData()
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

  static void stop() throws SQLException {
    if (driver != null) {
      System.out.println("[DEBUG] Will stop driver");
      driver.conn.close();
      driver = null;
      System.out.println("[DEBUG] Did stop driver");
    }
  }

  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    start("tcp://localhost:9002", "~/test", "sa", "");
    stop();
  }
}
