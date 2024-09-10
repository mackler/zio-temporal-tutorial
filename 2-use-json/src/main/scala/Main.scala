import zio.*
import zio.temporal.*
import zio.temporal.worker.*
import zio.temporal.workflow.*

@workflowInterface trait GiveFavorite:
  @workflowMethod def apply(person: Person): String

class GiveFavoriteImpl extends GiveFavorite:
  override def apply(person: Person) =
    s"Hello ${person.name}. Have some ${person.favorite}"

case class Person(name: String, favorite: String)

object Main extends ZIOAppDefault:
  val program =
    for
      worker <- ZWorkerFactory.newWorker("favorites-queue")
                  @@ ZWorker.addWorkflow[GiveFavoriteImpl].fromClass
      _      <- ZWorkerFactory.setup
      _      <- ZIO.sleep(Duration.Infinity)
    yield ()

  override val run =
    program.provideSome[Scope](
      ZWorkflowClientOptions.make,
      ZWorkflowClient.make,
      ZWorkerFactoryOptions.make,
      ZWorkerFactory.make,
      ZWorkflowServiceStubsOptions.make,
      ZWorkflowServiceStubs.make,
    )
