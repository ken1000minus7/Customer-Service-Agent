package com.ken.customerserviceagent.api.handler

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeHandler: JsonDeserializer<ZonedDateTime>, JsonSerializer<ZonedDateTime> {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ZonedDateTime {
        val dateString = json?.asString
        return ZonedDateTime.parse(dateString, formatter)
    }

    override fun serialize(
        zonedDateTime: ZonedDateTime?,
        typeOfT: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(zonedDateTime?.format(formatter))
    }
}