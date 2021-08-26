# Course Project at UNAH-MM545: Distributed Text File System

Implementation of a server/client application in Java RMI and JavaFX to manage concurrent
connections for managing a file system that accepts operations on users text files.

## Required technologies

- Java RMI or Python Pyro only.
- Anything that works for the client.
- Linux containers.

## Getting started

This system can be deployed in several ways. According to the course topics, you should use linux
containers for the server applications. For this server, there are two applications that run the
distributed system:

- **RMI Registry:** Receives the request from clients to connect to the remote object from the file
  server.


- **RMI File System Server:** Manages the actual file system and physical files in storage. The
  clients access the remote object of this application through the RMI Registry, so the registry may
  be deployed separately from the server storing the actual files.

On the other side, for the client application you should run three or more of them to show the real
time functionality.

One way to deploy all the system is as a standalone fashion. This way you need tons of RAM to run
many VMs, JVMs, Linux Containers and other open applications as depicted by the following diagram:

![All-In-One Deployment](./docs/img/deployment.svg)

In that case, I would even suggest trying to use GraalVM to avoid that crazy amount of JVMs. That is
something I haven't tested yet and the project at this stage is quite unstable. GraalVM also doesn't
play well with the latest non-LTS Java versions and just properly deploying JavaFX is a bit of a
mess by itself these days mostly if you use the latest versions of tools prior waiting for issue
fixes. So let's keep that game for later with Java 17 LTS since I am also using `JDK 16` +
`--enable-preview` as a good mathematician to take advantage of the new data oriented and FP
features for the *model* (or domain) layer.

### Requirements

#### AIO deploy

For the "All-In-One" deployment model the requirements are as follows:

**Minimum requirements**

- Intel® Core™ i3-6100 / Intel® Core™ i5-2400
- 8 GB of system memory
- 35 GB of available storage

**Recommended requirements**

- Intel® Core™ i5-4590 / Intel® Core™ i7-2600
- 12 GB of system memory
- 50 GB of available SSD storage
- Nvidia GT-730 / 256 MB of dedicated VRAM

Additionally, a broadband internet connection is required to install all the tools.

Some of these requirements are for the Ubuntu VM installation and accelerated graphics boots to make
the VM run smoother.

#### Cloud deploy

For a cloud deployment the requirements are simple:

- An Ubuntu VM with at least 1 vCPU, 2 GB RAM, 8 GB storage.

- A desktop computer to run the JavaFX clients.

It doesn't hurt to pick a better one. An Azure B2s / B2ms size is plenty enough for testing.

## About

**Course Project at UNAH-MM545: Distributed Text File System**

Implementation of a server/client application in Java RMI and JavaFX to manage concurrent
connections for managing a file system that accepts operations on users text files.

Copyright © 2021 Tobias Briones. All rights reserved.

### License

This project implementation is licensed under the [BSD 3-Clause LICENSE](./LICENSE).

---

This project's original specification is provided "AS IS" at [./docs/course](./docs/course).
