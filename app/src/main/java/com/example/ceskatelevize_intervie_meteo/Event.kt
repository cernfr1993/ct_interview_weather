package com.example.ceskatelevize_intervie_meteo

open class Event<out T>(private val content: T) {

    private var hasBeenHandled = false
        set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event<*>

        if (content != other.content) return false
        if (hasBeenHandled != other.hasBeenHandled) return false // Consider if this should be part of equals

        return true
    }

    override fun hashCode(): Int {
        var result = content?.hashCode() ?: 0
        result = 31 * result + hasBeenHandled.hashCode() // Consider if this should be part of hashCode
        return result
    }
}
