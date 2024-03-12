#!/usr/bin/env bash

set -e

# Compile sources
if [ ! -e src/main/java/google/devtools/build/lib/analysis/AnalysisProtos.java ]; then
    ./scripts/gen_proto.sh
fi

mvn package
