package com.redmadrobot.servers_plugin.data

import com.redmadrobot.debug_panel_core.extension.subscribeOnIo
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.data.storage.DebugServersDao
import io.reactivex.Completable
import io.reactivex.Single

internal class LocalDebugServerRepository(
    private val debugServersDao: DebugServersDao,
    private val preInstalledServers: List<DebugServer>
) : DebugServerRepository {

    override fun addServer(server: DebugServer): Completable {
        return debugServersDao.insert(server)
            .subscribeOnIo()
    }

    override fun getPreInstalledServers(): Single<List<DebugServer>> {
        return Single.just(preInstalledServers)
            .subscribeOnIo()
    }

    override fun getServers(): Single<List<DebugServer>> {
        return debugServersDao.getAll()
            .subscribeOnIo()
    }

    override fun removeServer(server: DebugServer): Completable {
        return debugServersDao.remove(server)
            .subscribeOnIo()
    }

    override fun updateServer(server: DebugServer): Completable {
        return debugServersDao.update(server)
            .subscribeOnIo()
    }
}