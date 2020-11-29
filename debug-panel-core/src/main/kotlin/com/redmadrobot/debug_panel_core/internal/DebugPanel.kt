package com.redmadrobot.debug_panel_core.internal

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.redmadrobot.debug_panel_core.DebugPanelInstance
import com.redmadrobot.debug_panel_core.inapp.DebugBottomSheet
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.util.ActivityLifecycleHandler
import timber.log.Timber

object DebugPanel {

    private var instance: DebugPanelInstance? = null

    fun initialize(application: Application, plugins: List<Plugin>) {
        createDebugPanelInstance(application, plugins)
        ActivityLifecycleHandler(application).start()
        initTimber()
    }

    fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit) {
        instance?.getEventLiveData()?.observe(lifecycleOwner, Observer { onEvent.invoke(it) })
    }

    fun showPanel(fragmentManager: FragmentManager) {
        if (isInitialized()) {
            DebugBottomSheet.show(fragmentManager)
        }
    }

    fun isInitialized() = instance != null

    private fun createDebugPanelInstance(application: Application, plugins: List<Plugin>) {
        instance = DebugPanelInstance(application, plugins)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}