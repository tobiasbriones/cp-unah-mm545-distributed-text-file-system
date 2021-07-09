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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Defines the sum type of accepted files for the system. Only text files are
 * allowed so far.
 *
 * @author Tobias Briones
 */
public sealed interface File extends CommonFile {
    record TextFile(CommonPath path) implements File {
        public static final String EXTENSION = "txt";
    }

    default Name fileName() {
        return Name.from(this);
    }

    record Name(String value) {
        /**
         * Defines the dot "." present as the delimiter for file extensions.
         */
        public static final String FILE_EXTENSION_DOT = ".";

        /**
         * Defines the accepted file names regex.
         */
        public static final String VALID_NAME_REGEX;
        private static final Pattern NAME_PATTERN;

        static {
            VALID_NAME_REGEX = "\\w+\\.*-*";
            NAME_PATTERN = Pattern.compile(VALID_NAME_REGEX, Pattern.CASE_INSENSITIVE);
        }

        static Name from(CommonFile file) {
            return new Name(file.name());
        }

        static boolean isValid(CharSequence value) {
            return NAME_PATTERN.matcher(value).find();
        }

        public Name {
            if (!isValid(value)) {
                final var msg = "Invalid file name: " + value;
                throw new RuntimeException(msg);
            }
        }

        String base() {
            final int length = value.length();
            final String extension = extension();
            return value.substring(0, length - extension.length() - 1);
        }

        String extension() {
            final Predicate<String> hasExtension = name -> name.contains(FILE_EXTENSION_DOT);
            final Function<String, Integer> getIndex = name -> name.lastIndexOf(FILE_EXTENSION_DOT) + 1;
            final Function<String, String> getSubstring = name -> name.substring(getIndex.apply(name));
            return Optional.of(value).filter(hasExtension).map(getSubstring).orElse("");
        }
    }
}
