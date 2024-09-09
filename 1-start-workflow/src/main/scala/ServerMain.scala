import zio.*
import zio.temporal.*
import zio.temporal.worker.*
import zio.temporal.workflow.*

@workflowInterface trait HelloWorld:
  @workflowMethod def apply(name: String): String

class HelloWorldImpl extends HelloWorld:
  override def apply(name: String) = s"Hello $name!"

object ServerMain extends ZIOAppDefault:

  val program =
    for
      worker <- ZWorkerFactory.newWorker("my-task-queue")
                  @@ ZWorker.addWorkflow[HelloWorldImpl].fromClass
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
