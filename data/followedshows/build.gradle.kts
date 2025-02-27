/*
 * Copyright 2017 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


plugins {
    id("kotlin")
    alias(libs.plugins.android.lint)
    alias(libs.plugins.ksp)
}

dependencies {
    api(projects.data.models)
    api(projects.data.traktauth)
    implementation(projects.data.db)
    implementation(projects.data.legacy) // remove this eventually

    api(projects.api.trakt)
    api(projects.api.tmdb)

    api(libs.store)
    implementation(libs.kotlinx.atomicfu)

    implementation(libs.kotlininject.runtime)
    ksp(libs.kotlininject.compiler)
}
