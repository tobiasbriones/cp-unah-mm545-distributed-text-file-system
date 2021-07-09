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

package io.github.tobiasbriones.cp.rmifilesystem.model.io;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author Tobias Briones
 */
public record CommonPath(String value) {
    /**
     * Defines the accepted CommonFile path regex.
     */
    public static final String VALID_PATH_REGEX;
    private static final Pattern PATH_PATTERN;

    static {
        VALID_PATH_REGEX = "\\w+/*\\.*-*";
        PATH_PATTERN = Pattern.compile(VALID_PATH_REGEX, Pattern.CASE_INSENSITIVE);
    }

    public static Optional<CommonPath> of(String value) {
        return Optional.of(value).filter(CommonPath::isValid).map(CommonPath::new);
    }

    private static boolean isValid(CharSequence value) {
        return PATH_PATTERN.matcher(value).find();
    }

    public CommonPath {
        if (!isValid(value)) {
            throw new InvalidPathException(value);
        }
    }
}
