#!/bin/bash

LWJGL_VERSION="3.3.2"
OS=macosx
ARCH=arm64
PREF=https://build.lwjgl.org/release/$LWJGL_VERSION/$OS/$ARCH

# define dl function
dl() {
    if [ -f $1 ]; then
        echo "$1 already exists"
        return
    fi
    wget $PREF/$1
}

cd $(dirname $0)

dl libbgfx.dylib
dl libglfw.dylib
dl liblwjgl.dylib