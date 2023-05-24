package com.salad.idlehero.resource

import com.fasterxml.jackson.databind.ObjectMapper

abstract class AbstractJsonResourceLoader {

  val BASE_PATH = "src/main/resources/com/salad/idlehero/"

  val objectMapper = new ObjectMapper()
}
