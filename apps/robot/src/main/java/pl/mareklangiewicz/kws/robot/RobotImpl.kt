package pl.mareklangiewicz.kws.robot

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.STREAM_MUSIC
import io.reactivex.disposables.Disposable
import pl.mareklangiewicz.kws.Close
import pl.mareklangiewicz.kws.Event
import pl.mareklangiewicz.kws.Message
import pl.mareklangiewicz.kws.Open
import pl.mareklangiewicz.kws.Server
import pl.mareklangiewicz.kws.loggers.Logger

class RobotImpl(private val server: Server, private val babbler: Babbler, private val context: Context, private val logger: Logger) : Robot {

    companion object {
        const val VOLUME_COMMAND_PREFIX = "volume "
        const val SAY_COMMAND_PREFIX = "say "
    }
    private var disposable: Disposable? = null

    override fun start() {
        disposable = server.eventS.subscribe { onEvent(it) }
        server.start()
        logger.log("Waiting for commands on port 9999")
    }

    private fun onEvent(event: Event) {
        logger.log("onEvent($event)")
        when (event) {
            is Message -> onMessage(event.message)
            is Open -> say("connected")
            is Close -> say("disconnected")
            else -> logger.log("TODO: handle Robot.onEvent($event)")
        }
    }

    private fun onMessage(message: String) {
        when (message) {
            "move forward" -> say("pretending to move forward. mmmmmmmmm. $randomReadyConfirmation!")
            "move backward" -> say("pretending to move backward. rrrrrrrrr. $randomReadyConfirmation!")
            "move left" -> say("pretending to move left. $randomReadyConfirmation!")
            "move right" -> say("pretending to move right. $randomReadyConfirmation!")
            "look up" -> say("pretending to look up. $randomReadyConfirmation!")
            "look down" -> say("pretending to look down. $randomReadyConfirmation!")
            "look ahead" -> say("pretending to look ahead. $randomReadyConfirmation!")
            "stop" -> say("pretending to stop. $randomReadyConfirmation!")
            else -> when {
                message.startsWith(SAY_COMMAND_PREFIX) -> say(message.substring(SAY_COMMAND_PREFIX.length))
                message.startsWith(VOLUME_COMMAND_PREFIX) -> changeVolume(message.substring(VOLUME_COMMAND_PREFIX.length).toInt())
                else -> logger.log("TODO: handle Robot.onMessage($message)")
            }
        }
    }

    override fun turnOff() {
        server.close()
        disposable?.dispose()
    }

    private fun say(speech: String) {
        when (speech) {
            in listOf("something funny", "something stupid") -> babbler.say(randomFunnySentence)
            in listOf("something smart", "something intelligent") -> babbler.say(randomSmartSentence)
            else -> babbler.say(speech)
        }
    }

    private fun changeVolume(delta: Int) {
        val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = manager.getStreamVolume(STREAM_MUSIC)
        val newVolume = (currentVolume + delta).coerceIn(0, manager.getStreamMaxVolume(STREAM_MUSIC))
        manager.setStreamVolume(STREAM_MUSIC, newVolume, 0)
        babbler.say(randomReadyConfirmation)
    }

    private val randomFunnySentence
        get() = FUNNY_QUOTES.random().sentence

    private val randomSmartSentence
        get() = SMART_QUOTES.random().let { (sentence, author) -> "$sentence\n$author" }

    private val randomReadyConfirmation
        get() = READY_CONFIRMATIONS.random()
}
