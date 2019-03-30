open module swim.basic {
  requires transitive swim.loader;
  requires transitive swim.client;

  exports swim.basic;

  provides swim.api.plane.Plane with swim.basic.BasicPlane;
}
