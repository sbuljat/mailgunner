package com.sbuljat.mailgunner.service

import akka.actor.Actor
import com.sbuljat.mailgunner.json.{SendMessageResponseJsonProtocol, SendMessageRequestJsonProtocol}
import com.sbuljat.mailgunner.model.SendMessageRequest
import spray.routing.HttpService

/**
  * Created by stipe on 22.1.2016..
  */
trait RestService extends HttpService with SendMessageRequestJsonProtocol with SendMessageResponseJsonProtocol{

  implicit def executionContext = actorRefFactory.dispatcher

  val mailgun:MailgunService

  // REST routes (create, delete)
  val route =
    path("send"){
      post{
        entity(as[SendMessageRequest]) { request =>
          complete{
            mailgun.send(request)
          }
        }
      }
    }
}

class RestServiceActor extends Actor with RestService{

  def actorRefFactory = context

  val mailgun = new MailgunService()

  def receive = runRoute(route)

}
