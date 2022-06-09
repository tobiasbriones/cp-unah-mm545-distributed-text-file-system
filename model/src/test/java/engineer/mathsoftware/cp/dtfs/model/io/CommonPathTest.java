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

package engineer.mathsoftware.cp.dtfs.model.io;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CommonPathTest {
    CommonPathTest() {}

    @Test
    void testVarargsConstructor() {
        final var p1 = new CommonPath("root/dir1/dir11");
        final var p2 = new CommonPath("kittens");
        final var p3 = new CommonPath("fluffy/pics");
        final var expected = new CommonPath("root/dir1/dir11/kittens/fluffy/pics");
        final CommonPath actual = CommonPath.of(p1, p2, p3);

        assertThat(actual, CoreMatchers.is(expected));
    }

    @Test
    void testVarargsConstructorEmpties() {
        final CommonPath empty = CommonPath.of();

        assertThat(
            CommonPath.of(empty),
            is(CommonPath.of())
        );

        assertThat(
            CommonPath.of(empty, empty),
            is(CommonPath.of())
        );

        assertThat(
            CommonPath.of(empty, empty, empty),
            is(CommonPath.of())
        );

        assertThat(
            CommonPath.of(empty, empty, empty, empty),
            is(CommonPath.of())
        );
    }
}
