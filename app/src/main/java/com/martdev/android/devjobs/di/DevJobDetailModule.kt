package com.martdev.android.devjobs.di

import androidx.lifecycle.ViewModel
import com.martdev.android.devjobs.devjobdetail.DevJobDetail
import com.martdev.android.devjobs.devjobdetail.DevJobDetailVM
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Scope

@Module
abstract class DevJobDetailModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun devJobDetailActivity(): DevJobDetail

    @Binds
    @IntoMap
    @ViewModelKey(DevJobDetailVM::class)
    abstract fun bindViewModel(viewModel: DevJobDetailVM): ViewModel
}