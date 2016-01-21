package com.sbuljat.mailgunner.json

import com.sbuljat.mailgunner.model.{SendMessageResponse, SendMessageRequest}
import spray.json._

/**
  * Created by stipe on 22.1.2016..
  */
trait SendMessageResponseJsonProtocol {

  implicit object SendMessageResponseJsonFormat extends RootJsonFormat[SendMessageResponse] {

    def write(res:SendMessageResponse) = JsObject(
      "success" -> JsBoolean(res.success),
      "message" -> JsString(res.msg)
    )

    def read(value:JsValue):SendMessageResponse = {
      value.asJsObject.getFields("success", "message") match {
        case Seq(JsBoolean(success), JsString(msg)) =>
          SendMessageResponse(success, msg)
        case _ =>
          throw new DeserializationException("Bad JSON! Expected { success:Boolean, msg:String }")
      }
    }

  }

}
