# Project of Distributed Systems Instituto Superior Técnico 2015-2016 #

Group de Distributed Systems 28 - Campus Alameda

Joaquim Esteves, 77020 - joaquimbve@hotmail.com

Francisco Martins, 76061 - franciscoprelhaz.m@gmail.com

Tomas Marques, 75344 - tom_as_sm@hotmail.com
... ... ...


Repo:
[tecnico-distsys/A_28-project](https://github.com/tecnico-distsys/A_28-project/)

-------------------------------------------------------------------------------

## Installation Instructions


### Environment

[0] Initiate Operating System

The implementation was done in Windows. We assume the output will be the same in any UNIX based System


[1] Initiate support systems

JUDDI:



[2] Get base code

git clone -b SD_R1 https://github.com/tecnico-distsys/A_28-project/


[3] Install auxiliar modules

```
cd uddi-naming
mvn clean install
```

```
cd ...
mvn clean install
```


-------------------------------------------------------------------------------

### Transporter Service

[1] Build and execute **server**

```
cd transporter-ws
mvn clean install
mvn exec:java
```
To execute another transporter run the following command:
mvn exec:java -Dws.i=2 //(or any other number)

[2] Build **client** and execute tests

```
cd transporter-ws-cli
mvn clean install
```

...


-------------------------------------------------------------------------------

### Broker Service

[1] Build and execute **server**

```
cd broker-ws
mvn clean install
mvn exec:java
```


[2] Build **client** and execute tests

```
cd broker-ws-cli
mvn clean install
mvn exec:java
```

...

-------------------------------------------------------------------------------
**The end**
