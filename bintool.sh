#!/bin/bash

if [[ -f "BinTool/target/BinTool-1.0.jar" ]]; then
    echo "BinTool already built"
else
    mvn clean package
fi

java -jar BinTool/target/BinTool-1.0.jar "$@"
