cmake_minimum_required(VERSION 3.27.1)
project(DexHelper)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_C_STANDARD 11)
find_package(cxx REQUIRED CONFIG)
link_libraries(cxx::cxx)

add_subdirectory(../../../../libs/dexkit/DexKit/Core Core)
aux_source_directory(../../../../libs/syscall/Syscall Syscall_Support)
include_directories(../../../../libs/syscall/Syscall)

add_library(
        DexHelper
        SHARED
        native.cpp
        ../../../../libs/dexkit/DexKit/dexkit/src/main/cpp/native-bridge.cpp
        ${Syscall_Support}
        )

find_library(log-lib log)

target_link_libraries(
        DexHelper
        ${log-lib}
        dexkit_static
        z
        )