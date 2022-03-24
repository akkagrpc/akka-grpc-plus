#!/bin/bash

cd $PWD/src/main/protobuf
docker run -v $PWD:/defs namely/protoc-all -f ./movie.proto -l descriptor_set --descr-filename movie.pb