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

package swim.grade;

import jdk.incubator.sql2.DataSource;
import jdk.incubator.sql2.DataSourceFactory;
import jdk.incubator.sql2.Session;
import jdk.incubator.sql2.SqlException;
import jdk.incubator.sql2.TransactionCompletion;
import java.util.stream.Collector;

class CustomDriver {

  private static final DataSource.Builder DATA_SOURCE_BUILDER = DataSourceFactory
      .newFactory("com.oracle.adbaoverjdbc.DataSourceFactory")
      .builder();
  private static CustomDriver driver;
  private Session conn;
  private String url;

  private CustomDriver(Session conn, String url) {
    this.conn = conn;
    this.url = url;
  }

  public static void start(String host, String path, String usr, String pw) {
    // `start` becomes `restart` if `driver` already exists
    if (driver != null) {
      stop();
    }

    final String url = "jdbc:h2:"
        + host
        + ((!host.endsWith("/") && !path.startsWith("/")) ? "/" : "")
        + path;
    System.out.println("[DEBUG] Will start driver at " + url);

    driver = new CustomDriver(null, url);
    driver.conn = DATA_SOURCE_BUILDER
        .url(url).username(usr).password(pw)
        .build()
        .getSession(t -> {
          System.out.println("[ERROR] " + t.getMessage());
          stop();
          System.exit(-1);
        });
    System.out.println("[DEBUG] Did start driver at " + url);
  }

  static void stop() {
    if (driver != null) {
      System.out.println("[DEBUG] Will stop driver");
      driver.stopDriver();
      driver = null;
      System.out.println("[DEBUG] Did stop driver");
    }
  }

  static void checkGrade(final int id) {
    final String sql = String.format("SELECT points_earned, points_possible FROM STUDENTS WHERE id = %d;", id);
    driver.conn.<Integer>rowOperation(sql)
        .collect(
            Collector.of(
                () -> null,
                (a, r) -> {
                  System.out.printf("%d: %s / %s\n", id, r.at(1).get(Integer.class), r.at(2).get(Integer.class));
                },
                (l, r) -> null,
                a -> {
                  System.out.println("end");
                  return null;
                }
            )
        )
        .onError(ex -> {
          ex.printStackTrace();
        })
        .submit();
  }

  static void updateGrade(int id, int earned, int possible) {
    TransactionCompletion trans = driver.conn.transactionCompletion();

    final String baseSql = String.format(
        "UPDATE STUDENTS "
            + "SET points_earned = %d, points_possible = %d "
            + "WHERE id = %d;", earned, possible, id);
    driver.conn.<Long>rowCountOperation(baseSql)
        .apply(c -> {
          if (c.getCount() != 1L) {
            trans.setRollbackOnly();
            throw new SqlException("updated wrong number of rows", null, null, -1, null, -1);
          }
          return c.getCount();
        })
        .onError(t -> t.printStackTrace())
        .submit();
    driver.conn.catchErrors();
    driver.conn.commitMaybeRollback(trans);
  }

  private void stopDriver() {
    if (this.conn != null && !this.conn.getSessionLifecycle().isClosed()) {
      this.conn.close();
    }
    this.conn = null;
    this.url = null;
  }
}
