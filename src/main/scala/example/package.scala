package dev.xymox.caliban.playground

import example.ExampleService.Service

import zio.{Has, RIO, ZEnv}

package object example {
  type ExampleService = Has[Service]
  type ExampleTask[A] = RIO[ZEnv with ExampleService, A]
}
