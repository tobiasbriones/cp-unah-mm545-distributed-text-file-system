# Course Project at UNAH-MM545: Distributed Text File System

[![GitHub Repository](https://raw.githubusercontent.com/tobiasbriones/general-images/main/example-projects/badges/ep-gh-repo-badge.svg)](https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system)

[![Project GitHub License](https://img.shields.io/github/license/tobiasbriones/cp-unah-mm545-distributed-text-file-system.svg?style=flat-square)](https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system/blob/main/LICENSE)

Implementation of a server/client application in Java RMI and JavaFX to manage concurrent
connections for managing a file system that accepts operations on users' text files.

The original course project required a much simpler implementation, and the main objective was to
apply DevOps skills. Needless to say, as I often do, I have added a huge amount of extra value to
this project to make it worth it. My objectives are to keep working on some new features after the
first release and build good DevOps documentation. Additionally, it's a good chance to show good use
cases of the main underlying technologies: Java and JavaFX.

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

### Deployment

For this version of the software, you need to do some manual configs regarding IP addresses or
hostnames. This is because the RMI technology requires knowing not only the server hostname where
the JVM is running but also the client's hostname to answer back to it. If the hostname property
isn't set on the client app then the server won't be able to respond to that client, or it might
take a huge amount of time to respond. Fortunately, everything is set up already, and the config
process just requires a bit of work.

#### Server applications

On server machine (assumed to be Ubuntu 20.x x64), install LXC, configure LXD and create two
containers: `registry` and `fs`; for the registry server and the file-system server respectively. I
don't recommend using snap along with LXC since I had a trouble which made me uninstall it later.
Then install a proxy device into the registry container to map the VM RMI port to the registry
container port:

`sudo apt update`

`sudo apt upgrade`

`sudo apt install lxd`

Choose the latest version of LXD (4.x+) and run `lxc list` to check it appears an empty list of
containers, and the installation was successful. Run `sudo lxd init` and choose all the defaults to
finish the lxd set up. Now install an Ubuntu image (in this case 20.04) and create the two
containers required:

`lxc launch ubuntu:20.04 registry`

`lxc launch ubuntu:20.04 fs`

Then check with `lxc list` that you have created two new linux containers.

##### Common set up

Next, run the following commands on both containers `registry` and `fs` to install common tools that
are suggested to deploy the applications:

`lxc exec {container-name} -- bash`

`sudo apt update`

`sudo apt upgrade`

`sudo apt install zip`

`sudo apt install unzip`

Install SDKMAN:

`curl -s "https://get.sdkman.io" | bash`

`source "$HOME/.sdkman/bin/sdkman-init.sh"`

Check SDKMAN installation: `sdk version`. Now install Gradle and Java. The current non-LTS version
of Java I used is JDK16 with enable-preview, I suggest installing JDK17+ (which is not GA yet at the
time of this release) for the current release v0.1.0:

`sdk list java` (choose the latest 17.0.x, 17+ version)

`sdk install java 17.0.{x}-open`

`sdk install gradle`

Clone the project repository into a directory of choice:

`git clone https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system.git`

Now the common configuration has finished for both containers.

##### Registry container

Install the proxy device into the registry container to accept communication to the outside:

`lxc config device add registry rmi-port proxy listen=tcp:0.0.0.0:1099 connect=tcp:127.0.0.1:1099`

Prior to continuing, you'd like to take note of both container's IP addresses, check it out
with `lxc list`.

Enter into the registry container and run the server as a registry application assuming you are into
the project root directory:

`lxc exec registry -- bash`

`cd server`

`gradle run --args="{registry-ip-address} reg"`

Thus, the server should be running as an RMI registry server and listening to incoming clients.

##### FileSystem container

Enter into the fs container and run the server as a file-system server:

`lxc exec fs -- bash`

`cd server`

`gradle run --args="{fs-ip-address} fs {registry-ip-address}"`

Thus, the server should be running as an RMI file-system server. So, the remote object of this
server has been bound to the registry server so that the registry server knows that clients want to
access *that* remote object located at *this* container.

##### Troubleshooting

- Don't forget to configure the inbound/outbound security groups to allow traffic pass by your VM.

- Since I haven't added the dynamic IP config via the `NetworkInterface` Java API, and they have to
  be passed manually by JVM args, don't forget that IP addresses change when restarting the network.
  I was playing with that API though JShell, and I think it will work well when I implement it.
  
- [Storage issues](./docs/troubleshooting/storage/storage.md): Container out of space, "no space left on device" when running an app.

#### Desktop client

Finally, to deploy the client into a desktop machine: clone the repository, install SDKMAN, Gradle
and a version of JDK with FX mods (Zulu FX or Liberica). If you use IntelliJ IDEA then it can be
faster to run from the Gradle window. You will need the familiar commands if you are in Linux or
Mac, for Windows the installations may be harder that's why I recommend using IntelliJ IDEA if you'
re using Windows. The following commands work for an Ubuntu desktop:

`sudo apt update`

`sudo apt upgrade`

`sudo apt install zip`

`sudo apt install unzip`

`curl -s "https://get.sdkman.io" | bash`

`source "$HOME/.sdkman/bin/sdkman-init.sh"`

`sdk install gradle`

Install a JDK version of your choice (JDK17+) but make sure it contains the `fx` modules:

`sdk list java | grep fx`

`sdk install java 17.0.{x}.fx-zulu`

Now edit the source file:

`cd client`

`sudo nano src/main/java/com/github/tobiasbriones/cp/rmifilesystem/client/FileSystemServices.java`

Then set the `HOST` constant of that file to the public IP address or hostname of your VM.

Finally, run the application and pass the IP address of your current desktop machine where the
client will run:

`gradle run --args="{client-ip-address}"`

##### Issues

One of the main issue of `v0.1.0` is the local FS update regarding deleting files. When a user
deletes a file form the system, it's physically deleted from the user machine but not physically
deleted from the other client machines. This feature will be implemented in a further project
version. Significant performance optimizations will be scheduled for later releases too.

## Screenshots

**LXC and Registry Container**
![Registry](./docs/img/lxc-list-and-registry-screenshot.png)

**LXC and FS container**
![FS](./docs/img/lxc-list-and-fs-screenshot.png)

**Client running and FS Server files**
![Client](./docs/img/client-and-fs-files-screenshot.png)

**Demo Animation**
![Demo Animation](./docs/img/demo.gif)

## Contact

This project: [Docs](https://tobiasbriones.github.io/cp-unah-mm545-distributed-text-file-system),
[Repository](https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system)

Tobias Briones: [GitHub](https://github.com/tobiasbriones)

## About

**Course Project at UNAH-MM545: Distributed Text File System**

Implementation of a server/client application in Java RMI and JavaFX to manage concurrent
connections for managing a file system that accepts operations on users' text files.

Copyright © 2021 Tobias Briones. All rights reserved.

### License

This project implementation is licensed under the [BSD 3-Clause LICENSE](./LICENSE).

---

This project's original specification is provided "AS IS" at [./docs/course](./docs/course).
