/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oracle.adbaoverjdbc;

import jdk.incubator.sql2.DataSource;
import jdk.incubator.sql2.DataSourceFactory;
import jdk.incubator.sql2.ParameterizedRowCountOperation;
import jdk.incubator.sql2.Result;
import jdk.incubator.sql2.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import static com.oracle.adbaoverjdbc.TestConfig.getDataSourceFactoryName;
import static com.oracle.adbaoverjdbc.TestConfig.getPassword;
import static com.oracle.adbaoverjdbc.TestConfig.getTimeout;
import static com.oracle.adbaoverjdbc.TestConfig.getUrl;
import static com.oracle.adbaoverjdbc.TestConfig.getUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This is a quick and dirty test to check if anything at all is working.
 */
public class RowCountOperationTestWithDMLReturning {

  // Define these three constants with the appropriate values for the database
  // and JDBC driver you want to use. Should work with ANY reasonably standard
  // JDBC driver. These values are passed to DriverManager.getSession.
  public static final String URL = TestConfig.getUrl();
  public static final String USER = TestConfig.getUser();
  public static final String PASSWORD = TestConfig.getPassword();
  public static final String FACTORY_NAME =
      TestConfig.getDataSourceFactoryName();


// CREATE TABLE DML_RET_TABLE1 (C11 int, C12 int GENERATED ALWAYS AS IDENTITY)  
  private static final String TEST_TABLE = "AOJ_DML_RET_TEST";

  @BeforeClass
  public static void setUpClass() throws Exception {
    DataSource ds = TestConfig.getDataSource();

    try (Session se = ds.getSession()) {
      se.operation("CREATE TABLE " + TEST_TABLE + "(C11 int, C12 int GENERATED ALWAYS AS IDENTITY)")
          .submit();
      se.endTransactionOperation(se.transactionCompletion())
          .timeout(getTimeout())
          .onError(err -> err.printStackTrace())
          .submit()
          .getCompletionStage()
          .toCompletableFuture()
          .get();
    }
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    DataSource ds = DataSourceFactory.newFactory(getDataSourceFactoryName())
        .builder()
        .url(getUrl())
        .username(getUser())
        .password(getPassword())
        .build();

    try (Session se = ds.getSession()) {
      se.operation("DROP TABLE " + TEST_TABLE).submit();
      se.endTransactionOperation(se.transactionCompletion())
          .timeout(getTimeout())
          .onError(err -> err.printStackTrace())
          .submit()
          .getCompletionStage()
          .toCompletableFuture()
          .get();
    }
  }

  /**
   * Do something that approximates real work. Do a transaction. Uses
   * TransactionCompletion, CompletionStage args, and catch Operation.
   */
  @Test
  public void transaction() throws Exception {
    DataSourceFactory factory = DataSourceFactory.newFactory(FACTORY_NAME);
    try (DataSource ds = factory.builder()
        .url(URL)
        .username(USER)
        .password(PASSWORD)
        .build();
         Session session = ds.getSession(t -> System.out.println("ERROR: " + t.toString()))) {

      ParameterizedRowCountOperation<Long> countOpReturning =
          session.<Long>rowCountOperation(
              "insert into " + TEST_TABLE + "(C11) values (1)");
      countOpReturning.returning(new String[] {"C12"})
          .collect(Collector.of(
              () -> null,
              (a, r) -> assertEquals(1, r.at(1)
                  .get(Integer.class)
                  .intValue()),
              (l, r) -> null))
          .onError(t -> fail(t.getMessage()))
          .submit();

      ParameterizedRowCountOperation<Long> countOpApplying =
          session.<Long>rowCountOperation(
              "insert into " + TEST_TABLE + "(C11) values (1)");
      countOpApplying.apply(Result.RowCount::getCount)
          .onError(t -> fail(t.getMessage()))
          .submit()
          .getCompletionStage()
          .thenAccept(c -> assertEquals(1L, c.longValue()));
      session.catchErrors();
      session.rollback()
          .toCompletableFuture()
          .get(TestConfig.getTimeout().toMillis(), TimeUnit.MILLISECONDS);
    }
  }
}
