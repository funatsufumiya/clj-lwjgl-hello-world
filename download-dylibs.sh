#!/bin/bash

LWJGL_VERSION="3.3.2"
OS=macosx
ARCH=arm64
# OS=windows
# ARCH=x64
PREF=https://build.lwjgl.org/release/$LWJGL_VERSION/$OS/$ARCH

# set lib prefix if macosx or linux
if [ "${OS}" == "macosx" ]; then
    LIBPREFIX=lib
elif [ "${OS}" == "linux" ]; then
    LIBPREFIX=lib
else
    LIBPREFIX=""
fi

# set lib suffix (extension)
if [ "${OS}" == "windows" ]; then
    LIBSUFFIX=dll
elif [ "${OS}" == "macosx" ]; then
    LIBSUFFIX=dylib
elif [ "${OS}" == "linux" ]; then
    LIBSUFFIX=so
else
    # unknown OS, exit
    echo "Unknown OS: ${OS}"
    exit 1
fi


# define dl function
dl() {
    f=${LIBPREFIX}$1.${LIBSUFFIX}
    if [ -f $f ]; then
        echo "$f already exists"
        return
    fi
    wget $PREF/$f
}

cd $(dirname $0)

dl lwjgl
dl glfw
dl bgfx
dl jemalloc