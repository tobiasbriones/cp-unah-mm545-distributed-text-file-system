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

package io.github.tobiasbriones.cp.rmifilesystem.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Tobias Briones
 */
public final class AppProperties {
    public static String readHostname(ClassLoader loader) {
        var prop = new Properties();

        try (InputStream resourceStream = loader.getResourceAsStream("config.properties")) {
            prop.load(resourceStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return prop.getProperty("java.rmi.server.hostname");
    }

    private AppProperties() {}
}
