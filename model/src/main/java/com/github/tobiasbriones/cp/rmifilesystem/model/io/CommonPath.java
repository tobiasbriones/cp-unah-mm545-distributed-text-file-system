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

package com.github.tobiasbriones.cp.rmifilesystem.model.io;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

/**
 * @author Tobias Briones
 */
public record CommonPath(String value) implements Serializable {
    /**
     * Standardize to use this character to define the system path separator
     * character at the domain model level (just for CommonFile objects) but not
     * for anything else. This is useful for this application to persist the
     * file path as string and forget whether the file separator is "/", "\",
     * ":" or ";".
     */
    public static final char SEPARATOR_CHAR;

    /**
     * See {@link CommonPath#SEPARATOR_CHAR}.
     */
    public static final String SEPARATOR;

    public static final String ROOT_PATH;

    /**
     * Defines the accepted CommonFile path regex.
     */
    public static final String VALID_PATH_REGEX;
    private static final Pattern PATH_PATTERN;

    static {
        SEPARATOR_CHAR = '/';
        SEPARATOR = String.valueOf(SEPARATOR_CHAR);
        ROOT_PATH = "";
        VALID_PATH_REGEX = "^$|\\w+/*\\.*-*";
        PATH_PATTERN = Pattern.compile(VALID_PATH_REGEX, Pattern.CASE_INSENSITIVE);
    }

    public static CommonPath of() {
        return new CommonPath(ROOT_PATH);
    }

    public static Optional<CommonPath> of(Iterable<? extends Path> pathValue) {
        final String stringValue = StreamSupport.stream(pathValue.spliterator(), false)
                                                .map(Path::toString)
                                                .reduce("", (s1, s2) -> s1 + SEPARATOR + s2);
        return of(stringValue);
    }

    public static Optional<CommonPath> of(String value) {
        return Optional.of(value).filter(CommonPath::isValid).map(CommonPath::new);
    }

    public static CommonPath of(CommonPath... paths) {
        final String value = Arrays.stream(paths)
                                   .map(CommonPath::split)
                                   .map(Arrays::asList)
                                   .flatMap(Collection::stream)
                                   .reduce("", (s1, s2) -> s1 + SEPARATOR + s2)
                                   .substring(1);

        if (value.equals(SEPARATOR) || value.startsWith(SEPARATOR + SEPARATOR)) {
            return of();
        }
        return new CommonPath(value);
    }

    private static boolean isValid(CharSequence value) {
        return PATH_PATTERN.matcher(value).find();
    }

    public CommonPath {
        if (!isValid(value)) {
            throw new InvalidPathException(value);
        }
    }

    public CommonPath getParent() {
        final Predicate<String> hasParent = path -> path.contains(SEPARATOR);
        final Function<String, String> parentSubstring = path ->
            path.substring(0, path.lastIndexOf(SEPARATOR_CHAR));
        return Optional.of(value)
                       .filter(hasParent)
                       .map(parentSubstring)
                       .map(CommonPath::new)
                       .orElse(of());
    }

    public String[] split() {
        return value.split(SEPARATOR);
    }
}
