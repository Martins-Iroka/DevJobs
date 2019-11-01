package com.martdev.android.devjobs.di

import android.content.Context
import com.martdev.android.devjobs.DevJobsApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    AndroidSupportInjectionModule::class,
    DevJobResultModule::class,
    DevJobDetailModule::class
])
interface ApplicationComponent : AndroidInjector<DevJobsApplication>{

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}