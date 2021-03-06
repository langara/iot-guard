package pl.mareklangiewicz.kws.robot

import android.app.Application
import android.util.Log
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins

class RobotApp : Application() {

    companion object {
        init {
//            WebSocketImpl.DEBUG = true
            setupRxJavaErrorHandler()
        }

        fun setupRxJavaErrorHandler() {
            RxJavaPlugins.setErrorHandler { throwable ->
                if (throwable is UndeliverableException) {
                    Log.v("RxJavaPlugins", "error handler got: $throwable")
                } else throw throwable
            }
        }
    }
}
