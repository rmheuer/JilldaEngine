#!/bin/bash

rm Sandbox/target/Sandbox-1.0.jar
mvn clean package
java -jar Sandbox/target/Sandbox-1.0.jar
