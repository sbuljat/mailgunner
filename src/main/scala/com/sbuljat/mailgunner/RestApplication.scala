package com.sbuljat.mailgunner

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import akka.util.Timeout
import com.sbuljat.mailgunner.service.{RestServiceActor, RestService}
import com.sbuljat.mailgunner.util.ApplicationConfig
import spray.can.Http
import akka.pattern.ask

/**
  * Created by stipe on 22.1.2016..
  */
object RestApplication extends App{

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("dns")

  implicit val timeout = Timeout(ApplicationConfig.timeout)
  // start a new HTTP server on port {..} with our service actor as the handler
  IO(Http) ? Http.Bind(system.actorOf(Props[RestServiceActor], "rest-service"), interface = ApplicationConfig.httpInterface, port = ApplicationConfig.httpPort)

  // shutdown actor system on system shutdown
  sys.addShutdownHook{
    system.terminate()
  }

}
