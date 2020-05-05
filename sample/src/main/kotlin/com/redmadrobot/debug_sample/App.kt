package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.data.PreInstalledData
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleChangeListener
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.internal.DebugPanelConfig

class App : Application(), Authenticator, FeatureToggleChangeListener {
    override fun onCreate() {
        super.onCreate()

        val debugPanelConfig = DebugPanelConfig(
            //TODO Временная реализация. Здесь это не должно делаться.
            authenticator = this,
            preInstalledServers = PreInstalledData(getPreinstalledServers()),
            preInstalledAccounts = PreInstalledData(getPreInstalledAccounts()),
            featureTogglesConfig = FeatureTogglesConfig(
                FeatureToggleWrapperImpl.toggleNames,
                FeatureToggleWrapperImpl(),
                this
            )
        )

        DebugPanel.initialize(this, debugPanelConfig)
    }

    override fun authenticate(account: DebugAccount) {
        println("Login - ${account.login}, Password - ${account.password}")
    }

    override fun onFeatureToggleChange(name: String, newValue: Boolean) {
        // Feature toggle was changed. You need
        println("New value for key \"$name\" = $newValue")
    }

    private fun getPreinstalledServers(): List<DebugServer> {
        return listOf(
            DebugServer(url = "https://testserver1.com")
        )
    }

    private fun getPreInstalledAccounts(): List<DebugAccount> {
        return listOf(
            DebugAccount(
                "7882340482",
                "Qq!11111"
            ),
            DebugAccount(
                "2777248041",
                "Qq!11111"
            ),
            DebugAccount(
                "4183730054",
                "Ww!11111"
            ),
            DebugAccount(
                "1944647499",
                "Qq!11111"
            )
        )
    }
}
