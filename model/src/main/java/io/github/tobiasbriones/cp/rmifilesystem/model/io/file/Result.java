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

package io.github.tobiasbriones.cp.rmifilesystem.model.io.file;

import java.util.Optional;

/**
 * Defines a result sum type which consists of {@link Success} and
 * {@link Failure}. This implementation is not perfect though because I think
 * that doing it the Kotlin way is not quite possible but this is the idea.
 *
 * @author Tobias Briones
 */
public sealed interface Result<T> {
    record Success<T>(T value) implements Result<T> {
        public static <T> Success<T> of(T content) {
            return new Success<>(content);
        }
    }

    record Failure<T>(Optional<Throwable> reason) implements Result<T> {
        public static<T> Failure<T> of() {
            return of(null);
        }

        public static<T> Failure<T> of(Throwable reason) {
            return new Failure<>(Optional.ofNullable(reason));
        }

        @Override
        public T value() { // :/
            return null;
        }
    }

    T value();
}
