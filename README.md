<a href="https://eclipse.org/ditto/">
  <img src="https://eclipse.org/ditto/images/ditto.svg" alt="Ditto Logo" width="100%" height="250">
</a>

# Eclipse Ditto

[![Join the chat at https://gitter.im/eclipse/ditto](https://badges.gitter.im/eclipse/ditto.svg)](https://gitter.im/eclipse/ditto?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://github.com/eclipse/ditto/workflows/build/badge.svg)](https://github.com/eclipse/ditto/actions?query=workflow%3Abuild)
[![Maven Central](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/org/eclipse/ditto/ditto/maven-metadata.xml.svg)](http://search.maven.org/#search|ga|1|org.eclipse.ditto)
[![License](https://img.shields.io/badge/License-EPL%202.0-green.svg)](https://opensource.org/licenses/EPL-2.0)
[![Lines of code](https://img.shields.io/badge/dynamic/xml.svg?label=Lines%20of%20code&url=https%3A%2F%2Fwww.openhub.net%2Fprojects%2Feclipse-ditto.xml%3Fapi_key%3D11ac3aa12a364fd87b461559a7eedcc53e18fb5a4cf1e43e02cb7a615f1f3d4f&query=%2Fresponse%2Fresult%2Fproject%2Fanalysis%2Ftotal_code_lines&colorB=lightgrey)](https://www.openhub.net/p/eclipse-ditto)

[Eclipse Ditto](https://eclipse.org/ditto/) is the open-source project of Eclipse IoT that provides a ready-to-use functionality to manage the state of Digital Twins. It provides access to them and mediates between the physical world and this digital representation.

## Documentation

Find the documentation on the project site: [https://eclipse.org/ditto/](https://eclipse.org/ditto)

## Getting started

In order to start up Ditto, you'll need
* a running Docker daemon (at least version 18.06 CE)
* Docker Compose installed (at least version 1.22)

### Start Ditto

In order to start the latest built Docker images from Docker Hub, simply execute:

```bash
cd deployment/docker/
docker-compose up -d
```

Check the logs after starting up:
```bash
docker-compose logs -f
```

Open following URL to get started: [http://localhost:8080](http://localhost:8080)<br/>
Or have a look at the ["Hello World"](https://eclipse.org/ditto/intro-hello-world.html)

### Build and start Ditto

In order to build Ditto, you'll need
* JDK 8 >= 1.8.0_92 (due to a bug in older versions of the JDK you'll get a compile error)
* Apache Maven 3.x installed

In order to first build Ditto and then start the built Docker images

```bash
# if you have the docker daemon running with remote access enabled (e.g. in a Vagrant box or on localhost):
mvn clean install -Pdocker-build-image -Ddocker.daemon.hostname=<ip/host of your docker daemon>
# if you have the docker daemon running on your machine and you are running on Unix, you can also connect against the docker socket:
mvn clean install -Pdocker-build-image -Ddocker.daemon.url=unix:///var/run/docker.sock

cd deployment/docker/
# the "dev.env" file contains the SNAPSHOT number of Ditto, copy it to ".env" so that docker compose uses it:
cp dev.env .env
docker-compose up -d
```

Check the logs after starting up:
```bash
docker-compose logs -f
```

You have now running:
* a MongoDB as backing datastore of Ditto (not part of Ditto but started via Docker)
* Ditto microservices:
   * Policies
   * Things
   * Thing-Search
   * Gateway
   * Connectivity
   * Concierge
* an nginx acting as a reverse proxy performing a simple "basic authentication" listening on port `8080`
   * including some static HTTP + API documentation on [http://localhost:8080](http://localhost:8080)
