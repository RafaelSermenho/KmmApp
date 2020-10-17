package dev.rafaelsermenho.handson.kmmapp.shared

import dev.rafaelsermenho.handson.kmmapp.shared.cache.Database
import dev.rafaelsermenho.handson.kmmapp.shared.cache.DatabaseDriverFactory
import dev.rafaelsermenho.handson.kmmapp.shared.entity.RocketLaunch
import dev.rafaelsermenho.handson.kmmapp.shared.network.SpaceXApi

class SpaceXSdk(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunhes = database.getAllLaunches()
        return if (cachedLaunhes.isNotEmpty() && !forceReload) {
            cachedLaunhes
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}