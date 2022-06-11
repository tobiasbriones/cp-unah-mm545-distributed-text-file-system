// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CommonPathTest {
    CommonPathTest() {}

    @Test
    void testVarargsConstructor() {
        var p1 = new CommonPath("root/dir1/dir11");
        var p2 = new CommonPath("kittens");
        var p3 = new CommonPath("fluffy/pics");
        var expected = new CommonPath("root/dir1/dir11/kittens/fluffy/pics");
        CommonPath actual = CommonPath.of(p1, p2, p3);
        assertThat(actual, is(expected));
    }

    @Test
    void testVarargsConstructorEmpties() {
        CommonPath empty = CommonPath.of();

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
