// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client;

import engineer.mathsoftware.cp.dtfs.impl.io.file.text.AppLocalTextFileRepository;
import engineer.mathsoftware.cp.dtfs.impl.io.file.text.CommonPaths;
import engineer.mathsoftware.cp.dtfs.io.Directory;
import engineer.mathsoftware.cp.dtfs.io.File;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileRepository;
import engineer.mathsoftware.cp.dtfs.io.node.DirectoryNode;
import engineer.mathsoftware.cp.dtfs.io.node.FileSystem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Tobias Briones
 */
public final class AppLocalFiles {
    private static final String FS_FILE_NAME = "fs.data";
    private static final String RELATIVE_ROOT = "fs";
    private static final String ROOT =
        System.getProperty("user.dir") + java.io.File.separator + RELATIVE_ROOT;

    public static FileSystem readFs() throws IOException {
        createRootIfNotExists();
         var file = new java.io.File(ROOT, FS_FILE_NAME);

        if (!file.exists()) {
            return new FileSystem(DirectoryNode.of());
        }

        try (var input = new ObjectInputStream(new FileInputStream(file))) {
            return (FileSystem) input.readObject();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new FileSystem(DirectoryNode.of());
    }

    public static void saveFs(FileSystem system) throws IOException {
        createRootIfNotExists();
         var file = new java.io.File(ROOT, FS_FILE_NAME);

        try (var output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(system);
        }
    }

    public static void setDownloaded(File file) throws IOException {
        var statuses = readStatuses();
        statuses.put(file, FileSystem.LastUpdateStatus.of(file));
        saveStatuses(statuses);
    }

    public static Map<File, FileSystem.LastUpdateStatus> readStatuses() throws IOException {
        createRootIfNotExists();
        var path = Path.of(ROOT, ".statuses.data");

        if (!Files.exists(path)) {
            return new HashMap<>(0);
        }

        try (var input = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Map<File, FileSystem.LastUpdateStatus>) input.readObject();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>(0);
        }
    }

    public static void saveStatuses(
        Map<File, FileSystem.LastUpdateStatus> statuses
    ) throws IOException {
        createRootIfNotExists();
         var file = new java.io.File(ROOT, ".statuses.data");

        try (var output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(statuses);
        }
    }

    public static void addToChangeList(File file) throws IOException {
        var changelist = readChangelist();
        changelist.add(file);
        saveChanglist(changelist);
    }

    public static void removeFromChangeList(File file) throws IOException {
        var changelist = readChangelist();
        changelist.remove(file);
        saveChanglist(changelist);
    }

    public static Set<File> readChangelist() throws IOException {
        createRootIfNotExists();
        var path = Path.of(ROOT, ".changelist.data");

        if (!Files.exists(path)) {
            return new HashSet<>(0);
        }

        try (var input = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Set<File>) input.readObject();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new HashSet<>(0);
        }
    }

    public static void saveChanglist(Set<File> changelist) throws IOException {
        createRootIfNotExists();
         var file = new java.io.File(ROOT, ".changelist.data");

        try (var output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(changelist);
        }
    }

    public static void createDirectory(Directory directory) throws IOException {
        createRootIfNotExists();
        var path = CommonPaths.toPath(Path.of(ROOT), directory.path());
        Files.createDirectories(path);
    }

    public static void deleteDirectory(Directory directory) throws IOException {
        createRootIfNotExists();
        var path = CommonPaths.toPath(Path.of(ROOT), directory.path());
        Files.delete(path);
    }

    public static TextFileRepository newTextFileRepository() {
        return new AppLocalTextFileRepository(Path.of(ROOT));
    }

    private AppLocalFiles() {}

    private static void createRootIfNotExists() throws IOException {
        createDirsIfNotExist(Path.of(ROOT));
    }

    private static void createDirsIfNotExist(Path path) throws IOException {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            Files.createDirectories(path);
        }
    }
}
