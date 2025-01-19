cmake_minimum_required(VERSION 3.14)
project(ProjectName)

# Require C++17 or higher
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Include GoogleTest via FetchContent
include(FetchContent)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/refs/tags/release-1.12.1.zip
)
# Prevent overriding the parent project's compiler/linker settings
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

# Include header files
include_directories(${CMAKE_SOURCE_DIR}/src/header)

# Collect all source files from the `src` directory
file(GLOB_RECURSE SRC_FILES ${CMAKE_SOURCE_DIR}/src/*.cpp)

# Exclude main other main functions
list(REMOVE_ITEM SRC_FILES ${CMAKE_SOURCE_DIR}/src/Server.cpp)

# Collect all test files from the `tests` directory
file(GLOB_RECURSE TEST_FILES ${CMAKE_SOURCE_DIR}/tests/*.cpp)

# Create an executable for tests
add_executable(runTests ${SRC_FILES} ${TEST_FILES})

# Link GoogleTest libraries
target_link_libraries(runTests gtest gtest_main)

# Enable test discovery
include(GoogleTest)
gtest_discover_tests(runTests)
