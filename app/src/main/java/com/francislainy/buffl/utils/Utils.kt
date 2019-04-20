package com.francislainy.buffl.utils

import timber.log.Timber

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
