public class BasicPlane extends AbstractPlane {

  @SwimRoute("/unit/:id")
  AgentRoute<UnitAgent> unitAgentType;

  public static void main(String[] args) {
    final Kernel kernel = ServerLoader.loadServer();
    final ActorSpace space = (ActorSpace) kernel.getSpace("basic");

    kernel.start();
    System.out.println("Running Basic server...");
    kernel.run();

//    space.command("/unit/foo", "wakeup", Value.absent());
    // TODO: add project-relevant commands
  }
}
