package utils

import java.net.URL

object Resources {
    fun resolve(path: String): URL? {
        return javaClass.classLoader.getResource(path)
    }
}