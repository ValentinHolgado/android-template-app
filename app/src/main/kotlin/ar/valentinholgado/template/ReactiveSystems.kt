package ar.valentinholgado.template

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ReactiveSystems {

    companion object {

        fun initialize(application: Application) {
            application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

                override fun onActivityPaused(activity: Activity?) {}

                override fun onActivityResumed(activity: Activity?) {}

                override fun onActivityStarted(activity: Activity?) {

                    activity?.let {
                        when (it) {
                        }
                    }
                }

                override fun onActivityDestroyed(activity: Activity?) {}

                override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

                override fun onActivityStopped(activity: Activity?) {}

                override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
            })
        }
    }
}