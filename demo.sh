#!/usr/bin/env bash

set -e

cd "$1"
BAT_PAGING=never bat *.scala
scala-cli test . --server=false || true
