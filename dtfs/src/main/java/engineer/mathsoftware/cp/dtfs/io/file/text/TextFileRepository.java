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

package engineer.mathsoftware.cp.dtfs.io.file.text;

import engineer.mathsoftware.cp.dtfs.io.file.FileRepository;

import static engineer.mathsoftware.cp.dtfs.io.File.TextFile;

/**
 * @author Tobias Briones
 */
public interface TextFileRepository extends FileRepository<TextFile, TextFileContent> {}
