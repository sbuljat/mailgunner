package com.sbuljat.mailgunner.model

import java.util.UUID

/**
  * Created by stipe on 21.1.2016.
  *
  */
case class SendMessageRequest(to:String, subject:String, body:String, template:Option[String]=None, vars:Map[String,String]=Map.empty, id:String=UUID.randomUUID().toString)
