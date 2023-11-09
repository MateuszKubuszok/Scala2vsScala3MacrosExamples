#!/usr/bin/env bash

set -x
set -e

cd "$1"
bat "example.scala"
bat "example.test.scala"
scala-cli test .
