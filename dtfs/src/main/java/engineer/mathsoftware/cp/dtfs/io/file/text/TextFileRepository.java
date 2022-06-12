// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.file.text;

import engineer.mathsoftware.cp.dtfs.io.file.FileRepository;

import static engineer.mathsoftware.cp.dtfs.io.File.TextFile;

/**
 * @author Tobias Briones
 */
public interface TextFileRepository extends FileRepository<TextFile,
    TextFileContent> {}
