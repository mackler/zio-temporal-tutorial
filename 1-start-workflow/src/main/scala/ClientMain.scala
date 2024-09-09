import zio.*
import zio.temporal.*
import zio.temporal.worker.*
import zio.temporal.workflow.*

object ClientMain extends ZIOAppDefault:

  val invokeWorkflow = (name: String) =>
    ZIO.serviceWithZIO[ZWorkflowClient]: client =>
      for
        time       <- Clock.currentDateTime
        workflowID      = s"hello-$time"
        workflowOptions = ZWorkflowOptions
          .withWorkflowId(workflowID)
          .withTaskQueue("my-task-queue")
//         .withWorkflowRunTimeout(1.hour)
        helloWorld <- client.newWorkflowStub[HelloWorld](workflowOptions)
        result     <- ZWorkflowStub.execute(helloWorld(name))
      yield result

  val program = for
    name           <- getArgs.map(_(0))
    result <- invokeWorkflow(name)
    _              <- Console.printLine(result)
  yield ()

  override def run = program.provideSome[ZIOAppArgs & Scope](
    ZWorkflowClientOptions.make,
    ZWorkflowClient.make,
    ZWorkflowServiceStubsOptions.make,
    ZWorkflowServiceStubs.make,
  )
