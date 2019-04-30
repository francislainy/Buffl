package com.francislainy.buffl.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import timber.log.Timber
import java.lang.reflect.Type

fun objectToStringJson(o: Any): String { //todo: have it as extension function instead

    val gson = GsonBuilder().setPrettyPrinting().create()
    return gson.toJson(o)
}

fun <T> objectFromJsonString(s: String?, classOfT:Class<T>) : T {
    val gson = Gson()
    return gson.fromJson(s, classOfT as Type)
}

class Utils {

    class MyDebugTree : Timber.DebugTree() {

        // https://stackoverflow.com/a/49216400/6654475
        override fun createStackElementTag(element: StackTraceElement): String? {
            return String.format(
                "#%s(%s:%s)",
                element.methodName,
                element.fileName,
                element.lineNumber,

                super.createStackElementTag(element)
            )
        }

    }

}
