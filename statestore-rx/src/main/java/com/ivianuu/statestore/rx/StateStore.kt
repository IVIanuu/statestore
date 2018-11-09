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

package com.ivianuu.statestore.rx

import com.ivianuu.statestore.StateStore
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Returns a [Completable] which completes on close
 */
val StateStore<*>.onClose: Completable get() = Completable.create { e ->
    val closeListener: () -> Unit = {
        if (!e.isDisposed) {
            e.onComplete()
        }
    }

    e.setCancellable { removeCloseListener(closeListener) }

    if (!e.isDisposed) {
        addCloseListener(closeListener)
    }
}

/**
 * Returns a [Observable] which emits on state changes
 */
val <T> StateStore<T>.observable: Observable<T> get() = Observable.create { e ->
    val stateListener: (T) -> Unit =  {
        if (!e.isDisposed) {
            e.onNext(it)
        }
    }

    val closeListener: () -> Unit = {
        if (!e.isDisposed) {
            e.onComplete()
        }
    }

    e.setCancellable {
        removeStateListener(stateListener)
        removeCloseListener(closeListener)
    }

    if (!e.isDisposed) {
        addCloseListener(closeListener)
        addStateListener(stateListener)
    }
}