/*
 * Copyright 2022 Google LLC
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

package app.tivi.data.traktauth.store

import android.app.Application
import app.tivi.data.traktauth.AppAuthAuthStateWrapper
import app.tivi.data.traktauth.AuthState
import com.google.android.gms.auth.blockstore.Blockstore
import com.google.android.gms.auth.blockstore.StoreBytesData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.tasks.await
import me.tatarka.inject.annotations.Inject

@Inject
@Suppress("DEPRECATION") // We need to wait for Play Services update to rollout
class BlockStoreAuthStore(
    private val context: Application,
) : AuthStore {
    private val client by lazy { Blockstore.getClient(context) }

    private val playServicesAvailable: Boolean
        get() = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS

    override suspend fun get(): AuthState? = runCatching {
        client.retrieveBytes()
            .await()
            .decodeToString()
            .let(::AppAuthAuthStateWrapper)
    }.getOrNull()

    override suspend fun save(state: AuthState) {
        val data = StoreBytesData.Builder()
            .setShouldBackupToCloud(false)
            .setBytes(state.serializeToJson().encodeToByteArray())
            .build()

        client.storeBytes(data).await()
    }

    override suspend fun clear() {
        val data = StoreBytesData.Builder()
            .setBytes(ByteArray(0))
            .build()

        client.storeBytes(data).await()
    }

    override suspend fun isAvailable(): Boolean = playServicesAvailable
}
