open module swim.basic {
  requires transitive swim.loader;
  requires transitive swim.client;
  requires org.eclipse.paho.client.mqttv3;

  exports swim.basic;

  provides swim.api.plane.Plane with swim.basic.BasicPlane;
}
