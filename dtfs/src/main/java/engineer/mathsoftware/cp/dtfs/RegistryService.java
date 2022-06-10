// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Tobias Briones
 */
public interface RegistryService extends Remote {
    boolean regObject(String name, Remote obj) throws RemoteException;
}
