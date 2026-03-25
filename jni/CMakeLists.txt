cmake_minimum_required(VERSION 3.10)
project(sueno-native)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)


find_package(JNI REQUIRED)

find_package(OpenMP REQUIRED)


add_library(sueno-native SHARED sueno_native.cpp)

target_include_directories(sueno-native PRIVATE ${JNI_INCLUDE_DIRS} include)


if(MSVC)
    target_compile_options(sueno-native PRIVATE
        /O2          # Максимальная скорость
        /Oi          # Использовать intrinsic-функции
        /Ot          # Приоритет скорости над размером
        /fp:fast     # Быстрая математика (снижает точность, повышает FPS)
        /arch:AVX2   # Использовать AVX2 (ВНИМАНИЕ: Если у игрока старый CPU, игра крашнется. Для релиза можно убрать или заменить на /arch:AVX)
    )
else()
    target_compile_options(sueno-native PRIVATE
        -O3               # Максимальный уровень оптимизации
        -ffast-math       # Агрессивная оптимизация float (игнорирует стандарты IEEE ради скорости)
        -funroll-loops    # Разворачивать циклы
        -fomit-frame-pointer # Не хранить указатель кадра (освобождает регистр)
    )

    if(NOT ANDROID)
        target_compile_options(sueno-native PRIVATE -march=native)
    endif()
endif()

target_link_libraries(sueno-native PRIVATE ${JNI_LIBRARIES})

if(TARGET OpenMP::OpenMP_CXX)
    target_link_libraries(sueno-native PRIVATE OpenMP::OpenMP_CXX)
else()
    target_link_libraries(sueno-native PRIVATE ${OpenMP_CXX_FLAGS})
endif()

if(ANDROID)
    find_library(log-lib log)
    target_link_libraries(sueno-native PRIVATE ${log-lib} m)
endif()

if(WIN32)
    set_target_properties(sueno-native PROPERTIES PREFIX "")
endif()