#!/usr/bin/env bash

set -e

cd "$1"
bat *.scala
scala-cli test .
