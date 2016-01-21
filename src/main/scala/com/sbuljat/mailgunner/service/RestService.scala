package com.sbuljat.mailgunner.service

import akka.actor.Actor
import com.sbuljat.mailgunner.json.{SendMessageResponseJsonProtocol, SendMessageRequestJsonProtocol}
import com.sbuljat.mailgunner.model.SendMessageRequest
import com.sbuljat.mailgunner.util.ApplicationConfig
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
        // Header X-API-Token is mandatory, used for auth purposes.
        // Currently tokens are hardcoded in application configuration but could be saved in a isolated database and make APIs to manage it (add, remove,...)
        headerValueByName("X-API-Token"){ token =>
          validate(ApplicationConfig.apiTokens.contains(token), "Check your token"){
            entity(as[SendMessageRequest]) { request =>
              complete{
                mailgun.send(request)
              }
            }
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
