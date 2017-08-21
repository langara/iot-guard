package pl.elpassion.iotguard.robot

import pl.elpassion.iotguard.Logger
import pl.elpassion.iotguard.api.Event
import pl.elpassion.iotguard.api.Message
import pl.elpassion.iotguard.api.Server

class RobotImpl(private val server: Server, private val babbler: Babbler, private val logger: Logger) : Robot {

    override fun start(serverPort: Int) {
        server.events.subscribe { onEvent(it) }
        server.start(serverPort)
    }

    private fun onEvent(event: Event) {
        logger.log("onEvent($event)")
        when (event) {
            is Message -> onMessage(event.message)
            else -> logger.log("TODO: handle Robot.onEvent($event)")
        }
    }

    private fun onMessage(message: String) {

        when (message) {
            "move forward" -> moveForward()
            "move backward" -> moveBackward()
            "move left" -> moveLeft()
            "move right" -> moveRight()
            "stop" -> stop()
            else ->
                if (message.startsWith("say ")) {
                    say(message.substring(4))
                }
                else {
                    logger.log("TODO: handle Robot.onMessage($message)")
                }
        }
    }

    private fun moveForward() {}
    private fun moveBackward() {}
    private fun moveLeft() {}
    private fun moveRight() {}
    private fun stop() {}

    private fun say(speech: String) {
        babbler.say(speech)
    }
}