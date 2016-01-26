package com.webtrends.harness.component.spray

import akka.actor.{Actor, ActorRef}
import spray.can.server.ServerSettings

trait SprayExternalHttpServer { this: Actor =>

  var externalHttpServer:Option[ActorRef] = None


  def startExternalHttpServer(port: Int, settings:Option[ServerSettings]=None) : ActorRef = {
    externalHttpServer = Some(context.actorOf(CoreSprayServer.props(port, {x:String => x.contains("SiftReportExtract")}, settings), SprayExternalServer.Name))
    externalHttpServer.get ! BindServer
    externalHttpServer.get
  }

  def stopExternalHttpServer = {
    externalHttpServer match {
      case Some(s) => s ! ShutdownServer
      case None => //ignore
    }
  }
}

object SprayExternalServer {
  val Name = "external-http-server"
}