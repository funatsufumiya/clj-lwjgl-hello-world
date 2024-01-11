#!/bin/bash

PREF=https://build.lwjgl.org/release/3.3.2/macosx/arm64
wget $PREF/libbgfx.dylib
wget $PREF/libglfw.dylib
wget $PREF/liblwjgl.dylib