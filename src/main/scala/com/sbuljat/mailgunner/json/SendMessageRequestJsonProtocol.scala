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

  def getOptionStringField(name:String, obj:JsObject): Option[String] = {
    obj.getFields(name) match {
      case Seq(JsString(x)) => Some(x)
      case _ => None
    }
  }

  implicit object SendMessageRequestJsonFormat extends RootJsonFormat[SendMessageRequest] {

    def write(req:SendMessageRequest) = JsObject(
      "id" -> JsString(req.id),
      "to" -> JsString(req.to),
      "subject" -> JsString(req.subject),
      "body" -> JsString(req.body),
      "template" -> req.template.map(e => JsString(e)).getOrElse(JsNull)
    )

    def read(value:JsValue):SendMessageRequest = {
        value.asJsObject.getFields("to", "subject", "body") match {
          case Seq(JsString(to), JsString(subject), JsString(body)) =>

            val vars:Map[String,String] = value.asJsObject.getFields("vars") match {
              case Seq(vars) =>
                vars.asJsObject.fields.collect{ case (name:String,value:JsString) => name -> value}.map(e => e._1 -> e._2.value)
              case _ =>
                Map.empty
            }

            SendMessageRequest(to, subject, body, getOptionStringField("template", value.asJsObject), vars)
          case _ =>
            throw new DeserializationException("Bad JSON! Expected { to:String, subject:String, body:String, template:String? }")
        }
    }

  }

}
