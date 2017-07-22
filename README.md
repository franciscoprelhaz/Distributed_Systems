# Projeto de Sistemas Distribuídos 2015-2016 #

Grupo de SD 28 - Campus Alameda

Joaquim Esteves, 77020 - joaquimbve@hotmail.com

Francisco Martins, 76061 - franciscoprelhaz.m@gmail.com

Tomas Marques, 75344 - tom_as_sm@hotmail.com
... ... ...


Repositório:
[tecnico-distsys/A_28-project](https://github.com/tecnico-distsys/A_28-project/)

-------------------------------------------------------------------------------

## Instruções de instalação 


### Ambiente

[0] Iniciar sistema operativo

A implementação foi feita em Windows. Assume-se que o resultado seja o mesmo em Linux


[1] Iniciar servidores de apoio

JUDDI:



[2] Obter código fonte do projeto (versão entregue)

git clone -b SD_R1 https://github.com/tecnico-distsys/A_28-project/


[3] Instalar módulos de bibliotecas auxiliares

```
cd uddi-naming
mvn clean install
```

```
cd ...
mvn clean install
```


-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
cd transporter-ws
mvn clean install
mvn exec:java
```
Para executar outra transportadora correr o comando:
mvn exec:java -Dws.i=2 (ou outro número qualquer)

[2] Construir **cliente** e executar testes

```
cd transporter-ws-cli
mvn clean install
```

...


-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidor**

```
cd broker-ws
mvn clean install
mvn exec:java
```


[2] Construir **cliente** e executar testes

```
cd broker-ws-cli
mvn clean install
mvn exec:java
```

...

-------------------------------------------------------------------------------
**FIM**
