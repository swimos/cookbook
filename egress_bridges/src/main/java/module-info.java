import swim.grade.GradePlane;

open module swim.grade {
  requires transitive jdk.incubator.adba;
  requires transitive swim.loader;
  requires transitive swim.client;
  requires com.h2database;
  requires java.sql;

  exports swim.grade;

  provides swim.api.plane.Plane with GradePlane;
  provides jdk.incubator.sql2.DataSourceFactory with com.oracle.adbaoverjdbc.DataSourceFactory;
}
