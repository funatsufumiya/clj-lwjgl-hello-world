#!/bin/bash

LWJGL_VERSION=3.3.2
OS=macosx
ARCH=arm64
PREF=https://build.lwjgl.org/release/$LWJGL_VERSION/$OS/$ARCH
wget $PREF/libbgfx.dylib
wget $PREF/libglfw.dylib
wget $PREF/liblwjgl.dylib