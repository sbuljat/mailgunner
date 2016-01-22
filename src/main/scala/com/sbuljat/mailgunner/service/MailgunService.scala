package com.sbuljat.mailgunner.service

import akka.event.Logging
import com.sbuljat.mailgunner.model.{SendMessageResponse, SendMessageRequest}
import com.sbuljat.mailgunner.util.ApplicationConfig
import spray.http._
import spray.client.pipelining._
import com.sbuljat.mailgunner.util.ApplicationConfig._
import scala.concurrent.Future
import scala.util.Random

/**
  * Created by stipe on 20.1.2016.
  */

// API for interacting with Mailgun
trait MailgunApi {

  def send(payload:SendMessageRequest):Future[SendMessageResponse]

}

// Mailgun API implementation based on spray-client
class MailgunService(actorService:ActorService = new ActorService) extends MailgunApi{
  implicit val sys = actorService.system

  import sys.dispatcher
  val log = Logging(sys, getClass)

  private val pipeline: HttpRequest => Future[HttpResponse] = ( addCredentials(BasicHttpCredentials("api", Mailgun.apiKey)) ~> sendReceive )

  // async email sender
  def send(request:SendMessageRequest):Future[SendMessageResponse] = {

    // generate body from template or given body
    val body = request.template   match {
      case Some(template) =>
        ApplicationConfig.Template.template(template, request.vars).getOrElse(request.body)
      case _ =>
        request.body
    }

    val payload = FormData(Seq("from" -> Mailgun.from, "to" -> request.to, "subject" -> request.subject, "html" -> body))

    pipeline(Post(Mailgun.endpoint, payload)).map{response =>
      //log.info(s"Message send status to ${request.to} => ${response.status.toString()} ${response.entity.asString}")

      SendMessageResponse(response.status.isSuccess, response.entity.asString, request)
    }
  }

  // shutdowns ActorSystem used by the spray pipeline
  def shutdown = {
    actorService.shutdown()
  }

}
