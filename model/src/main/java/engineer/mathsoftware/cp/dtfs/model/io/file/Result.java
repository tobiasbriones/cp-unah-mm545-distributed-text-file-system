/*
 * Copyright (c) 2021 Tobias Briones. All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * This file is part of Course Project at UNAH-MM545: Distributed Text File
 * System.
 *
 * This source code is licensed under the BSD-3-Clause License found in the
 * LICENSE file in the root directory of this source tree or at
 * https://opensource.org/licenses/BSD-3-Clause.
 */

package engineer.mathsoftware.cp.dtfs.model.io.file;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Defines a result sum type which consists of {@link Success} and
 * {@link Failure}. This implementation is not perfect though because I think
 * that doing it the Kotlin way is not quite possible but this is the idea.
 *
 * @author Tobias Briones
 */
public sealed interface Result<T extends Serializable> extends Serializable {
    record Success<T extends Serializable>(T value) implements Result<T> {
        public static <T extends Serializable> Success<T> of(T content) {
            return new Success<>(content);
        }
    }

    record Failure<T extends Serializable>(Throwable reason) implements Result<T> {
        public static<T extends Serializable> Failure<T> of() {
            return of(null);
        }

        public static<T extends Serializable> Failure<T> of(Throwable reason) {
            final Throwable nonNullReason = reason == null ? new RuntimeException("") : reason;
            return new Failure<>(nonNullReason);
        }

        @Override
        public T value() { // :/
            return null;
        }

        public void ifPresent(Consumer<? super Throwable> consumer) {
            if (!reason.getMessage().isBlank()) {
                consumer.accept(reason);
            }
        }
    }

    T value();
}
