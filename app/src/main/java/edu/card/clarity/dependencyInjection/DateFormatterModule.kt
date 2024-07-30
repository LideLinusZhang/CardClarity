package edu.card.clarity.dependencyInjection

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DateFormatterModule {
    @Singleton
    @Provides
    fun provideDateFormatter(): DateFormat {
        return SimpleDateFormat.getDateInstance()
    }
}