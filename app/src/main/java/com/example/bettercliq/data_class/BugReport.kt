package com.example.bettercliq.data_class

import java.sql.ClientInfoStatus
import java.sql.Date

data class BugReport(
    val title:String?=null,
    val storeFront:String?=null,
    val platform:String?=null,
    val desc:String?=null,
    val reproduce:String?=null,
    val solution:String?=null,
    val date:String?=null,
    var status: String?=null

)
