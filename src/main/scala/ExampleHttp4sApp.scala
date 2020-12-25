package dev.xymox.caliban.playground

import example._
import example.ExampleData._

import caliban.Http4sAdapter
import cats.data.Kleisli
import cats.effect.Blocker
import org.http4s.StaticFile
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import zio._
import zio.blocking.Blocking
import zio.interop.catz._

import scala.concurrent.ExecutionContext

object ExampleHttp4sApp extends App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    println("Starting ExampleHttp4sApp... (http://localhost:8088/api/graphql)")
    ZIO
      .runtime[ZEnv with ExampleService]
      .flatMap(implicit runtime =>
        for {
          blocker     <- ZIO.access[Blocking](_.get.blockingExecutor.asEC).map(Blocker.liftExecutionContext)
          interpreter <- ExampleApi.api.interpreter
          _           <- BlazeServerBuilder[ExampleTask](ExecutionContext.global)
            .bindHttp(8088, "localhost")
            .withHttpApp(
              Router[ExampleTask](
                "/api/graphql" -> CORS(Http4sAdapter.makeHttpService(interpreter)),
                "/ws/graphql"  -> CORS(Http4sAdapter.makeWebSocketService(interpreter)),
                "/graphiql"    -> Kleisli.liftF(StaticFile.fromResource("/graphiql.html", blocker, None))
              ).orNotFound
            )
            .resource
            .toManaged
            .useForever
        } yield ()
      )
      .provideCustomLayer(ExampleService.make(sampleCharacters))
      .exitCode
  }
}
