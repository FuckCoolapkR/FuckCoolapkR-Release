package org.fuck.coolapk.utils

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.function.Consumer

class InterfaceProxy private constructor(private val interfaces: Array<Class<*>>, private val classLoader: ClassLoader) {

    private val callback = arrayListOf<AttachMethodPair>()

    fun attachMethod(matcher: MethodMatcher, handler: MethodHandler) {
        synchronized(callback) {
            callback.add(AttachMethodPair(matcher, handler))
        }
    }

    fun detachMethod(matcher: MethodMatcher, handler: MethodHandler) {
        synchronized(callback) {
            callback.retainAll { it.matcher != matcher && it.handler != handler }
        }
    }

    private fun create(): Any {
        return Proxy.newProxyInstance(classLoader, interfaces) { proxy, method, args ->
            callback.forEach { pair ->
                if (MethodMatcher.match(pair.matcher, method)) {
                    val param = ProxyParam(proxy, args)
                    pair.handler.onCalled(param)
                    if (param.isResultInit) {
                        return@newProxyInstance param.result
                    } else {
                        return@newProxyInstance null
                    }
                }
            }
            return@newProxyInstance null
        }
    }

    @Suppress("ClassName")
    companion object `-Static` {

        @Throws(IllegalArgumentException::class, SecurityException::class)
        fun createProxy(interfaces: Array<Class<*>>, classLoader: ClassLoader, block: InterfaceProxy.() -> Unit) = InterfaceProxy(interfaces, classLoader).also(block).create()

        @JvmStatic
        @Throws(IllegalArgumentException::class, SecurityException::class)
        fun createProxy(interfaces: Array<Class<*>>, classLoader: ClassLoader, block: Consumer<InterfaceProxy>) = InterfaceProxy(interfaces, classLoader).also { block.accept(it) }.create()

    }

    interface MethodHandler {
        fun onCalled(param: ProxyParam)
    }

    class MethodMatcher {

        private lateinit var name: String
        private val isNameInit
            get() = this::name.isInitialized

        private lateinit var declaredInterface: Class<*>
        private val isDeclaredInterfaceInit
            get() = this::declaredInterface.isInitialized

        private val parameterCounts: ValueContainer<Int> = ValueContainer()
        private val isParameterCountsInit
            get() = parameterCounts.isValueInit

        private lateinit var parameterTypes: Array<out Class<*>>
        private val isParameterTypesInit
            get() = this::parameterTypes.isInitialized

        private lateinit var returnType: Class<*>
        private val isReturnTypeInit
            get() = this::returnType.isInitialized

        fun setName(value: String) {
            name = value
        }

        fun getName(): String? {
            return if (isNameInit) {
                name
            } else {
                null
            }
        }

        fun setDeclaredInterface(value: Class<*>) {
            declaredInterface = value
        }

        fun getDeclaredInterface(): Class<*>? {
            return if (isDeclaredInterfaceInit) {
                declaredInterface
            } else {
                null
            }
        }

        fun setParameterCounts(value: Int) {
            parameterCounts.value = value
        }

        fun getParameterCounts(): Int? {
            return if (isParameterCountsInit) {
                parameterCounts.value
            } else {
                null
            }
        }

        fun setParameterTypes(vararg value: Class<*>) {
            parameterTypes = value
        }

        fun getParameterTypes(): Array<out Class<*>>? {
            return if (isParameterTypesInit) {
                parameterTypes
            } else {
                null
            }
        }

        fun setReturnType(value: Class<*>) {
            returnType = value
        }

        fun getReturnType(): Class<*>? {
            return if (isReturnTypeInit) {
                returnType
            } else {
                null
            }
        }

        @Suppress("ClassName")
        companion object `-Static` {

            private fun Array<out Class<*>>.checkArray(b: Array<Class<*>>): Boolean {
                if (size != b.size) return false
                val tmp = filterIndexed { index, clazz -> clazz == b[index] }
                return size == tmp.size
            }

            @JvmStatic
            fun match(matcher: MethodMatcher, method: Method): Boolean {
                val declaredClass = method.declaringClass
                val name = method.name
                val parameterTypes = method.parameterTypes
                val parameterCount = method.parameterCount
                val returnType = method.returnType
                if (matcher.isDeclaredInterfaceInit && matcher.getDeclaredInterface() != declaredClass) {
                    return false
                }
                if (matcher.isNameInit && matcher.getName() != name) {
                    return false
                }
                if (matcher.isParameterTypesInit && matcher.getParameterTypes()?.checkArray(parameterTypes) != true) {
                    return false
                }
                if (matcher.isParameterCountsInit && matcher.getParameterCounts() != parameterCount) {
                    return false
                }
                if (matcher.isReturnTypeInit && matcher.getReturnType() != returnType) {
                    return false
                }
                return true
            }

        }

    }

    open inner class ProxyParam(val thisObj: Any, val args: Array<out Any>?) {
        var result: Any? = null
            set(value) {
                field = value
                isResultInitInner = true
            }
        private var isResultInitInner = false
        val isResultInit
            get() = isResultInitInner
    }

    private class AttachMethodPair(val matcher: MethodMatcher, val handler: MethodHandler)

    private class ValueContainer <T> {
        var value: T? = null
        val isValueInit: Boolean
            get() {
                value?.let {
                    return true
                }
                return false
            }
    }

}