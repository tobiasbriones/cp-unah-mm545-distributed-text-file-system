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

/**
 * @author Tobias Briones
 */
module engineer.mathsoftware.cp.dtfs.client {
    requires java.rmi;
    requires javafx.controls;
    requires engineer.mathsoftware.cp.dtfs;
    requires engineer.mathsoftware.cp.dtfs.impl;

    opens engineer.mathsoftware.cp.dtfs.client
        to javafx.graphics, java.rmi;
}
