// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.file;

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
        public static <T extends Serializable> Failure<T> of() {
            return of(null);
        }

        public static <T extends Serializable> Failure<T> of(Throwable reason) {
            var nonNullReason = reason == null
                                ? new RuntimeException("")
                                : reason;
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
