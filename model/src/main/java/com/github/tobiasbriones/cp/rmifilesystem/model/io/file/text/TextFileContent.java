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

package com.github.tobiasbriones.cp.rmifilesystem.model.io.file.text;

import com.github.tobiasbriones.cp.rmifilesystem.model.io.file.FileContent;

import static com.github.tobiasbriones.cp.rmifilesystem.model.io.File.TextFile;

/**
 * @author Tobias Briones
 */
public record TextFileContent(
    TextFile file,
    String value
) implements FileContent<TextFile, String> {}
