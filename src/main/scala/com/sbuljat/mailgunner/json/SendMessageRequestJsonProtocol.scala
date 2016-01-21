package com.sbuljat.mailgunner.json

import com.sbuljat.mailgunner.model.SendMessageRequest
import spray.httpx.SprayJsonSupport
import spray.json._

/**
  * Created by stipe on 21.1.2016.
  *
  * JSON protocol, used mainly for parsing JSON from a String input.
  *
  */
trait SendMessageRequestJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport{

  implicit object SendMessageJsonFormat extends RootJsonFormat[SendMessageRequest] {

    def write(req:SendMessageRequest) = JsObject(
      "to" -> JsString(req.to),
      "subject" -> JsString(req.subject),
      "body" -> JsString(req.body)
    )

    def read(value:JsValue):SendMessageRequest = {
        value.asJsObject.getFields("to", "subject", "body") match {
          case Seq(JsString(to), JsString(subject), JsString(body)) =>
            SendMessageRequest(to, subject, body)
          case _ =>
            throw new DeserializationException("Bad JSON! Expected { to:String, subject:String, body:String }")
        }
    }

  }

}
