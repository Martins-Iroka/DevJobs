package com.martdev.android.devjobs.di

import androidx.lifecycle.ViewModel
import com.martdev.android.devjobs.devjobresult.DevJobResultFragment
import com.martdev.android.devjobs.devjobresult.DevJobResultVM
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class DevJobResultModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun devJobResultFragment(): DevJobResultFragment

    @Binds
    @IntoMap
    @ViewModelKey(DevJobResultVM::class)
    abstract fun bindViewModel(viewModel: DevJobResultVM): ViewModel
}