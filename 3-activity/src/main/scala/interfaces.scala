import zio.*
import zio.json.*
import zio.temporal.*
import zio.temporal.activity.*
import zio.temporal.workflow.*
import scala.io.Source

@workflowInterface trait AnswerQuestion:
  @workflowMethod def apply(question: Question): String

class AnswerQuestionImpl extends AnswerQuestion:

  val activity = ZWorkflow.newActivityStub[QuestionActivity]:
      ZActivityOptions.withStartToCloseTimeout(60.seconds)

  override def apply(question: Question) =
    val answer = ZActivityStub.execute(activity.getAnswer())
    s"Hello ${question.name}. You asked, “${question.text}” The answer is $answer."

@activityInterface trait QuestionActivity:
  def getAnswer(): String

class QuestionActivityImpl extends QuestionActivity:
  override def getAnswer(): String =
    Source.fromURL("https://yesno.wtf/api").mkString.fromJson[YesOrNo] match
      case Right(YesOrNo(answer, _, _)) => answer
      case Left(_)                      => "unavailable"
