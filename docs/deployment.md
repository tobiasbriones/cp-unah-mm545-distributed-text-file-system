<!-- Copyright (c) 2021 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: BSD-3-Clause -->
<!-- This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system -->

# Deployment

The next section will tell you about some important information to understand before making a
deployment for a more serious production environment.

## Introduction

One way to deploy all the system is as a standalone fashion. This way you need tons of RAM to run
many VMs, JVMs, Linux Containers and other open applications as depicted by the following diagram:

![All-In-One Deployment](./img/deployment.svg)

Remind that, as far as I'm aware, the deployment over the Internet (or a WAN) is not feasible as
long as the underlying implementation are remote objects with callbacks. So, in order to make the
deployment on a LAN, for testing the "AIO" option is best. For production, we can assume that all
machines will be on the same LAN. Some machines will be desktop computers running the clients,
others will provide the registry container (private IP), and the fs container for the registry
container to use.

### Requirements

#### AIO deploy

For the "All-In-One" deployment model which is just a testing/development environment, the requirements are as follows:

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

#### Cloud deployment

For a cloud deployment the requirements are simple:

- An Ubuntu VM with at least 1 vCPU, 2 GB RAM, 8 GB storage.
- A desktop computer to run the JavaFX clients.

It doesn't hurt to pick a better one. An Azure B2s / B2ms size is plenty enough for testing.

**Important:** If you plan to set up a cloud deployment over a WAN or the Internet then you must
have a good background in networking since this is a really tough/impossible endeavor. I have tried
for many days to achieve this goal, but it is not feasible to fulfill. I don't plan on doing so,
because the underlying technology is archaic (RMI), and I'm a Software Engineer and not a DevOps.
The problem is likely due to the system using callbacks, and the client has to be a server too to
export its object, then firewall and inbound rules have to be configured for each client machine,
the IPs and ports are another mess, and a machine might just be able to run only one client at a
time. Then, just make an "AIO"-fashioned deploy where all the machines run on the same LAN if you
don't have a larger infrastructure running on the same LAN. More on this:

- [How to send a message from Server to Client using Java RMI?](https://stackoverflow.com/questions/29284276/how-to-send-a-message-from-server-to-client-using-java-rmi)
- [Can I invoke a client´s method from a server with RMI](https://stackoverflow.com/questions/21665300/can-i-invoke-a-client%C2%B4s-method-from-a-server-with-rmi)
- [Java rmi over the internet](https://stackoverflow.com/questions/16268391/java-rmi-over-the-internet)

Since the cloud deployment is not feasible as I mentioned above, then just set up the server
machine(s) and containers abstracting away the fact whether they are cloud or just more machines
inside the same LAN.

## Deployment

For this version of the software, you need to do some manual configs regarding IP addresses or
hostnames. This is because the RMI technology requires knowing not only the server hostname where
the JVM is running but also the client's hostname to answer back to it. If the hostname property
isn't set on the client app then the server won't be able to respond to that client, or it might
take a huge amount of time to respond. Fortunately, everything is set up already, and the config
process just requires a bit of work.

### Server applications

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

#### Common set up

Next, run the following commands on both containers `registry` and `fs` to install common tools that
are suggested to deploy the applications:

`lxc exec { container } -- bash`

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

`sdk install java 17.0.{ x }-open`

`sdk install gradle`

Clone the project repository into a directory of choice:

`git clone https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system.git`

Now the common configuration has finished for both containers.

#### Registry container

Install the proxy device into the registry container to accept communication to the outside:

`lxc config device add registry rmi-port proxy listen=tcp:0.0.0.0:1099 connect=tcp:127.0.0.1:1099`

Prior to continuing, you'd like to take note of both container's IP addresses, check it out
with `lxc list`.

Enter into the registry container and run the server as a registry application assuming you are into
the project root directory:

`lxc exec registry -- bash`

`cd server`

`gradle run --args="{ registry-ip-address } reg"`

Thus, the server should be running as an RMI registry server and listening to incoming clients.

#### FileSystem container

Enter into the fs container and run the server as a file-system server:

`lxc exec fs -- bash`

`cd server`

`gradle run --args="{ fs-ip-address } fs { registry-ip-address }"`

Thus, the server should be running as an RMI file-system server. So, the remote object of this
server has been bound to the registry server so that the registry server knows that clients want to
access *that* remote object located at *this* container.

#### Troubleshooting

- Don't forget to configure the inbound/outbound security groups to allow traffic pass by your VM.

- Since I haven't added the dynamic IP config via the `NetworkInterface` Java API, and they have to
  be passed manually by JVM args, don't forget that IP addresses change when restarting the network.
  I was playing with that API though JShell, and I think it will work well when I implement it.

- [Storage issues](./troubleshooting/storage/storage.md): Container out of space, "no space left on
  device" when running an app.

### Desktop client

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

`sdk install java 17.0.{ x }.fx-zulu`

Now edit the source file:

`cd client`

`sudo nano src/main/java/com/github/tobiasbriones/cp/rmifilesystem/client/FileSystemServices.java`

Then set the `HOST` constant of that file to the public IP address or hostname of your VM.

Finally, run the application and pass the IP address of your current desktop machine where the
client will run:

`gradle run --args="{ client-ip-address }"`

#### Troubleshooting

- If you get a weird error when running the app,
  like [Error initializing QuantumRenderer: no suitable pipeline found](https://stackoverflow.com/questions/68204320/javafx-installation-issues-error-initializing-quantumrenderer)
  make sure to had installed an updated JDK version containing the JavaFX mods like Azul FX or
  BellSoft Liberica and run the application with that JDK.

#### Suggested reading

In [this article](troubleshooting/binary-incompatibility), I talk about
an experience I had with the famous binary compatibility.

#### Issues

One of the main issue of `v0.1.0` is the local FS update regarding deleting files. When a user
deletes a file form the system, it's physically deleted from the user machine but not physically
deleted from the other client machines. This feature will be implemented in a further project
version. Significant performance optimizations will be scheduled for later releases too.

### Bibliography

- Linux Containers. (2021). Linuxcontainers.Org. https://linuxcontainers.org/

- Home - SDKMAN! the Software Development Kit Manager. (2021). Sdkman.Io. https://sdkman.io/

- Xenitellis, S. (2021, February 8). How to use the LXD Proxy Device to map ports between the host
  and the containers.
  Blog.Simos.Info. https://blog.simos.info/how-to-use-the-lxd-proxy-device-to-map-ports-between-the-host-and-the-containers/
