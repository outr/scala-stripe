#!/usr/bin/env bash

sbt +clean +package +coreJS/publishSigned +coreJVM/publishSigned sonatypeRelease