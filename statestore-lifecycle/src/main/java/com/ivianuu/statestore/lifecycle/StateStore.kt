/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.statestore.lifecycle

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ivianuu.statestore.StateStore

/**
 * Returns a [LiveData] which emits on state changes
 */
val <T> StateStore<T>.liveData: LiveData<T>
    get() {
        val liveData = MutableLiveData<T>()
        addStateListener {
            when {
                Looper.myLooper() == Looper.getMainLooper() -> liveData.value = it
                else -> liveData.postValue(it)
            }
        }
        return liveData
    }