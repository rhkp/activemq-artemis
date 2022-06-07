# ActiveMQ Artemis

This file describes some minimum 'stuff one needs to know' to get started coding in this project.

## Source

For details about the modifying the code, building the project, running tests, IDE integration, etc. see
our [Hacking Guide](./docs/hacking-guide/en/SUMMARY.md).

## Build Status

Build Status: [![Build Status](https://travis-ci.org/apache/activemq-artemis.svg?branch=main)](https://travis-ci.org/apache/activemq-artemis)

## Building the ASYNC IO library

ActiveMQ Artemis provides two journal persistence types, NIO (which uses the Java NIO libraries), and ASYNCIO which interacts with the linux kernel libaio library.   The ASYNCIO journal type should be used where possible as it is far superior in terms of performance.

ActiveMQ Artemis does not ship with the Artemis Native ASYNCIO library in the source distribution.  These need to be built prior to running "mvn install", to ensure that the ASYNCIO journal type is available in the resulting build.  Don't worry if you don't want to use ASYNCIO or your system does not support libaio, ActiveMQ Artemis will check at runtime to see if the required libraries and system dependencies are available, if not it will default to using NIO.

To build the ActiveMQ Artemis ASYNCIO native libraries, please follow the instructions in the artemis-native/README.

## Documentation

Our documentation is always in sync with our releases at the [Apache ActiveMQ Artemis](https://activemq.apache.org/artemis/docs.html) website.

Or you can also look at the current main version on [github](https://github.com/apache/activemq-artemis/blob/main/docs/user-manual/en/SUMMARY.md).

## Examples

To run an example firstly make sure you have run

    $ mvn -Prelease install

If the project version has already been released then this is unnecessary.

Each individual example can be run using this command from its corresponding directory:

    $ mvn verify

If you wish to run groups of examples then use this command from a parent directory (e.g. examples/features/standard):

    $ mvn -Pexamples verify

### Recreating the examples

If you are trying to copy the examples somewhere else and modifying them. Consider asking Maven to explicitly list all the dependencies:

    # if trying to modify the 'topic' example:
    cd examples/jms/topic && mvn dependency:list

### Open Web Application Security Project (OWASP) Report

If you wish to generate the report for CCV dependencies, you may run it with the -Powasp profile

    $ mvn -Powasp verify

The output will be under ./target/dependency-check-report.html **for each** sub-module.

## Bugs

Issues are tracked at https://issues.apache.org/jira/projects/ARTEMIS/

## Debug Mirroring Features of ActiveMQ Artemis code with two Eclipse IDE Instances on local machine with two broker instances
* On local machine create two directories artemis1 & artemis2 and clone fork of ActiveMQ Artemis Code
```shell
mkdir artemis1 
mkdir artemis2
cd artemis1
git clone https://github.com/rhkp/activemq-artemis.git

cd artemis2
git clone https://github.com/rhkp/activemq-artemis.git
```

* Install [OS X Eclipse laucher](http://ananthchellathurai.blogspot.com/2013/08/how-to-open-multiple-eclipse-on-mac.html) utility on Eclipse so you can open multiple Eclipse IDE Instances

* In Eclipse create workspace for artemis1 by File -> Open Workspace -> Advanced -> Browse -> Select artemis1 directory

* Click File -> Import -> Existing Maven Projects -> Next and select activemq-artemis directory

* Let import complete and press OK or Accept in any dialog that may appear

* This sets up first broker's workspace

* Repeat the procedure above for artemis2 by creating another workspace for artemis2

* In both workspaces activate any breakpoints as needed as per your requirements (such as mirroring)

* In the artemis2 workspace, right click artemis-examples -> broker-features -> run-broker-2 project and click Debug as Maven build. If a dialog appears enter verify in goal field.

* In the artemis1 workspace, right click artemis-examples -> broker-features -> run-broker-1 project and click Debug as Maven build. If a dialog appears enter verify in goal field.

* When both brokers above have started, you will see Connection Established log message in both brokers and log window will become stable without any periodic messages.

* In either artemis1 or artemis2 workspace, right click artemis-examples -> broker-features -> disaster-recovery -> TestMirroringTwoBrokers.java class and click Debug As Java

* When broker1 breakpoint will be hit, artemis1 workspace Eclipse IDE will suspend execution and you will be able to debug broker1 code. Similarly broker2 will be available for debugging in Eclipse Instance two via artemis2 workspace.

## Debug Mirroring Features of ActiveMQ Artemis code with One Eclipse IDE Instances on local machine with two broker instances
* In this case only use one workspaace such as artemis1 or artemis2. Instead of launching other instance from other workspace invoke both broker instances from one workspace itself by right click artemis-examples -> broker-features -> run-broker-1 or run-broker-2 project and click Debug as Maven build
* This approach may be confusing however you may not know which broker's breakpoint is hit and you are debugging e.g.