package pl.elpassion.iotguard.api

import io.reactivex.Observable

interface Socket : AutoCloseable {
    fun send(message: String)
}

sealed class Event(open val source: Socket? = null)

data class Open(override val source: Socket? = null) : Event(source)

data class Close(val code: Int, override val source: Socket? = null) : Event(source)

data class Message(val message: String, override val source: Socket? = null) : Event(source)

data class Error(val exception: Exception, override val source: Socket? = null) : Event(source)

object Start : Event()

interface Endpoint : AutoCloseable {
    val connections: List<Socket> // client will have at most one. server can have many
    val events: Observable<Event>
    fun send(message: String) = connections.forEach { it.send(message) }
}

val Endpoint.messages: Observable<String> get() = events.ofType(Message::class.java).map { it.message }

interface Client : Endpoint {
    fun connect(serverURI: String)
}

interface Server : Endpoint {
    fun start(socketPort: Int = 80)
}
