package com.sbuljat.mailgunner.service

import akka.actor.ActorSystem
import spray.http._
import spray.client.pipelining._
import com.sbuljat.mailgunner.util.ApplicationConfig._
import scala.concurrent.Future

/**
  * Created by stipe on 20.1.2016..
  */

case class MailgunResponse(success:Boolean, msg:String)

trait MailgunApi {

  def send(to:String, subject:String, text:String):Future[MailgunResponse]

}

class MailgunService extends MailgunApi{
  private implicit val system = ActorSystem("mailgun-client")
  import system.dispatcher

  private val pipeline: HttpRequest => Future[HttpResponse] = ( addCredentials(BasicHttpCredentials("api", Mailgun.apiKey)) ~> sendReceive )

  def send(to:String, subject:String, text:String):Future[MailgunResponse] = {

    val payload = FormData(Seq("from" -> Mailgun.from, "to" -> to, "subject" -> subject, "text" -> text))

    pipeline(Post(Mailgun.endpoint, payload)).map{response =>
      MailgunResponse(response.status.isSuccess, response.entity.asString)
    }
  }
}
