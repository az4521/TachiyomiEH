package eu.kanade.tachiyomi

import android.app.Application
import com.google.gson.Gson
import eu.kanade.tachiyomi.data.cache.ChapterCache
import eu.kanade.tachiyomi.data.cache.CoverCache
import eu.kanade.tachiyomi.data.database.DatabaseHelper
import eu.kanade.tachiyomi.data.download.DownloadManager
import eu.kanade.tachiyomi.data.preference.PreferencesHelper
import eu.kanade.tachiyomi.data.track.TrackManager
import eu.kanade.tachiyomi.extension.ExtensionManager
import eu.kanade.tachiyomi.network.NetworkHelper
import eu.kanade.tachiyomi.source.SourceManager
import exh.eh.EHentaiUpdateHelper
import io.noties.markwon.Markwon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uy.kohesive.injekt.api.*

class AppModule(val app: Application) : InjektModule {

    override fun InjektRegistrar.registerInjectables() {

        addSingleton(app)

        addSingletonFactory { PreferencesHelper(app) }

        addSingletonFactory { DatabaseHelper(app) }

        addSingletonFactory { ChapterCache(app) }

        addSingletonFactory { CoverCache(app) }

        addSingletonFactory { NetworkHelper(app) }

        addSingletonFactory { SourceManager(app).also { get<ExtensionManager>().init(it) } }

        addSingletonFactory { ExtensionManager(app) }

        addSingletonFactory { DownloadManager(app) }

        addSingletonFactory { TrackManager(app) }

        addSingletonFactory { Gson() }

        addSingletonFactory { EHentaiUpdateHelper(app) }

        addSingletonFactory { Markwon.create(app) }

        // Asynchronously init expensive components for a faster cold start

        GlobalScope.launch { get<PreferencesHelper>() }

        GlobalScope.launch { get<NetworkHelper>() }

        GlobalScope.launch { get<SourceManager>() }

        GlobalScope.launch { get<DatabaseHelper>() }

        GlobalScope.launch { get<DownloadManager>() }

    }

}
