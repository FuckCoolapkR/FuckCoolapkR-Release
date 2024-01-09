#include <jni.h>
#include <string>
#include "logging.h"
#include "native_api.h"
#include <dlfcn.h>
#include <sys/system_properties.h>
#include "native.h"
#include "linux_syscall_support.h"
#include <dirent.h>

#define PKG_NAME "com.coolapk.market"

using namespace std;
static HookFunType hook_func = nullptr;

bool hasEnding(std::string_view fullString, std::string_view ending) {
    if (fullString.length() >= ending.length()) {
        return (0 == fullString.compare(fullString.length() - ending.length(), ending.length(),
                                        ending));
    } else {
        return false;
    }
}

char *(*backup_fgets) (char *desk, int size, FILE* stream);
char *fake_fgets(char *buffer, int size, FILE* stream) {
    char tmp[size];
    char *result = backup_fgets(tmp, size, stream);
    if (result == nullptr) return result;
    if (string_view(tmp).find("org.hello.coolapk") == string_view::npos) {
        // 如果找不到就复制正常的，否则保留上一次的
        memcpy(buffer, tmp, size);
    }
    return buffer;
}

static bool string2int(const std::string &str, int &value) {
    char *endptr = nullptr;
    value = static_cast<int>(strtol(str.c_str(), &endptr, 10));
    return endptr != str.c_str() && *endptr == '\0';
}

JNIEXPORT JNICALL extern "C" jstring Java_org_fuck_coolapk_CoolapkUtils_readLink(JNIEnv *env, jclass arg, jint fd) {
    char filename[1024];
    char buffer[1024];
    snprintf(filename, sizeof(filename), "/proc/self/fd/%d", fd);
    readlink(filename, buffer, sizeof(buffer));
    return env->NewStringUTF(buffer);
}

JNIEXPORT JNICALL extern "C" jint Java_org_fuck_coolapk_CoolapkUtils_getFd(JNIEnv *env, jclass arg) {
    // walk through /proc/pid/fd to find the apk path
    std::string selfFdDir = "/proc/" + std::to_string(getpid()) + "/fd";
    DIR *dir = opendir(selfFdDir.c_str());
    if (dir == nullptr) {
        return -1;
    }
    struct dirent *entry;
    while ((entry = readdir(dir)) != nullptr) {
        if (entry->d_type != DT_LNK) {
            continue;
        }
        std::string linkPath = selfFdDir + "/" + entry->d_name;
        char buf[PATH_MAX] = {};
        ssize_t len = sys_readlinkat(AT_FDCWD, linkPath.c_str(), buf, sizeof(buf));
        if (len < 0) {
            continue;
        }
        buf[len] = '\0';
        std::string path(buf);
        if (path.starts_with("/data/app/") && path.find(PKG_NAME) != std::string::npos && path.ends_with(".apk")) {
            closedir(dir);
            int resultFd = -1;
            if (string2int(entry->d_name, resultFd)) {
                return resultFd;
            }
            return -1;
        }
    }
    closedir(dir);
    return -1;
}

void on_library_loaded(const char *name, void *handle) {
    if (!name) return;
    if (hasEnding(name, "libnative-lib.so")) {
        hook_func((void*) fgets, (void*) fake_fgets, (void**) &backup_fgets);
    }
}

extern "C" [[gnu::visibility("default")]] [[gnu::used]]
NativeOnModuleLoaded native_init(const NativeAPIEntries *entries) {
    hook_func = entries->hookFunc;
    return on_library_loaded;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    if (vm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    jclass clazz = env->FindClass("org/fuck/coolapk/CoolapkUtils");
    if (clazz == nullptr) {
        return -1;
    }
    jfieldID fieldId = env->GetStaticFieldID(clazz, "a", "I");
    env->SetStaticIntField(clazz, fieldId, 114);
    return JNI_VERSION_1_6;
}
