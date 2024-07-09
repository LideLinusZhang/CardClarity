package edu.card.clarity.dependencyInjection

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.card.clarity.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlacesModule {
    @Singleton
    @Provides
    fun providePlacesClient(
        @ApplicationContext context: Context
    ): PlacesClient {
        val apiKey = BuildConfig.PLACES_API_KEY

        Places.initializeWithNewPlacesApiEnabled(context, apiKey)

        return Places.createClient(context)
    }
}