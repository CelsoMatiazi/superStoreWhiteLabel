package com.crsm.whitelabelstore.config.di

import com.crsm.whitelabelstore.config.Config
import com.crsm.whitelabelstore.config.ConfigImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ConfigModule {

    @Binds
    fun bindConfig(config: ConfigImpl): Config

}