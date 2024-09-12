import zio.*

/** An extremely simple example of a runnable ZIO program */
object MyApp extends ZIOAppDefault:

  override val run = for
    time <- Clock.currentDateTime
    _    <- Console.printLine(s"the current date and time is $time")
  yield "this return value will be ignored"
