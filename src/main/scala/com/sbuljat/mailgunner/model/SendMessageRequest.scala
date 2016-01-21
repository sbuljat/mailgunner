package com.sbuljat.mailgunner.model

/**
  * Created by stipe on 21.1.2016.
  *
  */
case class SendMessageRequest(to:String, subject:String, body:String, template:Option[String]=None, vars:Map[String,String]=Map.empty)
