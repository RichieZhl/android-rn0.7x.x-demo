# Copyright (c) Meta Platforms, Inc. and affiliates.
#
# This source code is licensed under the MIT license found in the
# LICENSE file in the root directory of this source tree.

cmake_minimum_required(VERSION 3.13)
set(CMAKE_VERBOSE_MAKEFILE on)

project(appmodules)

include(Android-prebuilt.cmake)

file(GLOB react_codegen_SRCS CONFIGURE_DEPENDS *.cpp)

add_library(
  appmodules
  SHARED
  ${react_codegen_SRCS}
)

add_library(rnsafeareacontext_modules SHARED IMPORTED GLOBAL)
set_target_properties(rnsafeareacontext_modules
        PROPERTIES
        IMPORTED_LOCATION
        ${PROJECT_PARENT}/react-native-safe-area-context/build/intermediates/cmake/release/obj/${ANDROID_ABI}/librnsafeareacontext_modules.so)
target_include_directories(rnsafeareacontext_modules
        INTERFACE
        ${PROJECT_PARENT}/react-native-safe-area-context/src/main/jni)

add_library(rnscreen_modules SHARED IMPORTED GLOBAL)
set_target_properties(rnscreen_modules
        PROPERTIES
        IMPORTED_LOCATION
        ${PROJECT_PARENT}/react-native-screens/build/intermediates/cmake/release/obj/${ANDROID_ABI}/librnscreen_modules.so)
target_include_directories(rnscreen_modules
        INTERFACE
        ${PROJECT_PARENT}/react-native-screens/src/main/jni)

add_library(rnsvg_modules SHARED IMPORTED GLOBAL)
set_target_properties(rnsvg_modules
        PROPERTIES
        IMPORTED_LOCATION
        ${PROJECT_PARENT}/react-native-svg/build/intermediates/cmake/release/obj/${ANDROID_ABI}/librnsvg_modules.so)
target_include_directories(rnsvg_modules
        INTERFACE
        ${PROJECT_PARENT}/react-native-svg/src/main/jni)

target_link_libraries(
  appmodules
  fabricjni
  fbjni
  folly_runtime
  glog
  jsi
  react_codegen_rncore
  react_debug
  react_nativemodule_core
  react_render_core
  react_render_debug
  react_render_graphics
  react_render_mapbuffer
  react_render_imagemanager
  react_render_componentregistry
  rrc_view
  turbomodulejsijni
  runtimeexecutor
  yoga
  rnsafeareacontext_modules
  rnscreen_modules
  rnsvg_modules
)

target_compile_options(
  appmodules
  PRIVATE
  -DLOG_TAG=\"ReactNative\"
  -fexceptions
  -frtti
  -std=c++17
  -Wall
)
