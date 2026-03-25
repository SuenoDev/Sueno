#include <jni.h>
#include <omp.h>
#include <cmath>
#include <algorithm>
#include <cstring>
#include <vector>

static float* g_data = nullptr;
static std::vector<float> g_swap;

static int g_width = 0;
static int g_height = 0;

extern "C" {

JNIEXPORT void JNICALL Java_org_durmiendo_sueno_temperature_NativeTemperature_initNative
  (JNIEnv *env, jclass clazz, jint width, jint height, jobject directBuffer) {

    if (width <= 0 || height <= 0) return;

    g_width = width;
    g_height = height;
    size_t size = (size_t)width * height;

    g_data = (float*)env->GetDirectBufferAddress(directBuffer);

    g_swap.resize(size);
    std::fill(g_swap.begin(), g_swap.end(), -1.0f);
}


JNIEXPORT void JNICALL Java_org_durmiendo_sueno_temperature_NativeTemperature_update
  (JNIEnv *env, jclass clazz, jfloat transferCoeff, jfloat ambientChange) {

    if (!g_data || g_width == 0) return;

    const int w = g_width;
    const int h = g_height;
    float* next = g_swap.data();

    #pragma omp parallel for schedule(static)
    for (int y = 1; y < h - 1; ++y) {

        const float* row_up   = g_data + (y - 1) * w;
        const float* row_curr = g_data + y * w;
        const float* row_down = g_data + (y + 1) * w;

        float* row_next = next + y * w;

        #pragma omp simd
        for (int x = 1; x < w - 1; ++x) {
            float c = row_curr[x];

            float neighbors = row_curr[x - 1] +
                              row_curr[x + 1] +
                              row_up[x]       +
                              row_down[x];

            float diff = neighbors - (4.0f * c);
            float newVal = c + (diff * transferCoeff) + ambientChange;

            newVal = fmaxf(-1.0f, fminf(newVal, 1.0f));

            row_next[x] = newVal;
        }
    }

    memcpy(g_data, next, w * h * sizeof(float));
}

}