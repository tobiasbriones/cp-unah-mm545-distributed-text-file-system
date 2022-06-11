// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io;

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

        public TextFile(String path) {
            this(new CommonPath(path));
        }
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
            NAME_PATTERN = Pattern.compile(
                VALID_NAME_REGEX,
                Pattern.CASE_INSENSITIVE
            );
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
            final Predicate<String> hasExtension = name -> name.contains(
                FILE_EXTENSION_DOT);
            final Function<String, Integer> getIndex = name -> name.lastIndexOf(
                FILE_EXTENSION_DOT) + 1;
            final Function<String, String> getSubstring =
                name -> name.substring(
                getIndex.apply(name));
            return Optional.of(value).filter(hasExtension).map(getSubstring)
                           .orElse("");
        }

        static Name from(CommonFile file) {
            return new Name(file.name());
        }

        static boolean isValid(CharSequence value) {
            return NAME_PATTERN.matcher(value).find();
        }
    }

    static File of(CommonPath path) {
        final String[] tokens = path.split();
        final String fileNameValue = tokens[tokens.length - 1];
        final Name fileName = new Name(fileNameValue);

        // Check for the File type but only text files are supported right
        // now...
        return new TextFile(path);
    }

    default Name fileName() {
        return Name.from(this);
    }
}
