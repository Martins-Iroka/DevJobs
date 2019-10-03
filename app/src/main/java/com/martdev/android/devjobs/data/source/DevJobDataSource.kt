/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.martdev.android.devjobs.data.source


import com.martdev.android.devjobs.data.DevJob
import com.martdev.android.devjobs.data.Result

/**
 * Main entry point for accessing tasks data.
 */
interface DevJobDataSource {

    suspend fun getDevJobs(keyword: String = ""): Result<List<DevJob>>

    suspend fun getDevJob(jobId: String): Result<DevJob>

    suspend fun saveDevJob(job: DevJob) {}

    suspend fun deleteDevJobs() {}
}
