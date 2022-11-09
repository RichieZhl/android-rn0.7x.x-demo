# Copyright (c) Meta Platforms, Inc. and affiliates.
#
# This source code is licensed under the MIT license found in the
# LICENSE file in the root directory of this source tree.

cmake_minimum_required(VERSION 3.13)
set(CMAKE_VERBOSE_MAKEFILE on)

project(rnsafeareacontext_modules)

include(Android-prebuilt.cmake)

file(GLOB react_codegen_SRCS CONFIGURE_DEPENDS *.cpp react/renderer/components/safeareacontext/*.cpp)

add_library(
  rnsafeareacontext_modules
  SHARED
  ${react_codegen_SRCS}
)

target_include_directories(rnsafeareacontext_modules PUBLIC . react/renderer/components/safeareacontext)

target_link_libraries(
  rnsafeareacontext_modules
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
  rrc_view
  turbomodulejsijni
  yoga
)

target_compile_options(
  rnsafeareacontext_modules
  PRIVATE
  -DLOG_TAG=\"ReactNative\"
  -fexceptions
  -frtti
  -std=c++17
  -Wall
)
