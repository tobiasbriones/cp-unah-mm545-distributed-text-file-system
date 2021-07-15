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
 * @author Tobias Briones
 */
public sealed interface Result<C> {
    record Success<C>(C content) implements Result<C> {
        public static <C> Success<C> of(C content) {
            return new Success<>(content);
        }
    }

    record Fail(
        Optional<String> msg,
        Optional<Throwable> throwable
    ) implements Result<Nothing> {
        public static Fail of() {
            return of(null, null);
        }

        public static Fail of(String msg) {
            return of(msg, null);
        }

        public static Fail of(String msg, Throwable throwable) {
            return new Fail(Optional.ofNullable(msg), Optional.ofNullable(throwable));
        }

        @Override
        public Nothing content() {
            return Nothing.of();
        }
    }

    C content();
}
