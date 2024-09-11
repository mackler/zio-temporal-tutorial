import zio.*
import zio.json.*
import zio.temporal.*
import zio.temporal.activity.*
import zio.temporal.worker.*
import zio.temporal.workflow.*

case class Question(name: String, text: String)

case class YesOrNo(answer: String, forced: Boolean, image: String)
object YesOrNo:
  given JsonDecoder[YesOrNo] = DeriveJsonDecoder.gen[YesOrNo]

object Main extends ZIOAppDefault:
  val program =
    for
      _ <- ZWorkerFactory.newWorker("question-queue")
             @@ ZWorker.addWorkflow[AnswerQuestionImpl].fromClass
             @@ ZWorker.addActivityImplementation(new QuestionActivityImpl)
      _ <- ZWorkerFactory.setup
      _ <- ZIO.sleep(Duration.Infinity)
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
