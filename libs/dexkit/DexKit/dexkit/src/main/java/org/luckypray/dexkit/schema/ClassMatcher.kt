// automatically generated by the FlatBuffers compiler, do not modify

package org.luckypray.dexkit.schema

import com.google.flatbuffers.BaseVector
import com.google.flatbuffers.BooleanVector
import com.google.flatbuffers.ByteVector
import com.google.flatbuffers.Constants
import com.google.flatbuffers.DoubleVector
import com.google.flatbuffers.FlatBufferBuilder
import com.google.flatbuffers.FloatVector
import com.google.flatbuffers.LongVector
import com.google.flatbuffers.StringVector
import com.google.flatbuffers.Struct
import com.google.flatbuffers.Table
import com.google.flatbuffers.UnionVector
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sign

@Suppress("unused")
internal class `-ClassMatcher` : Table() {

    fun __init(_i: Int, _bb: ByteBuffer)  {
        __reset(_i, _bb)
    }
    fun __assign(_i: Int, _bb: ByteBuffer) : `-ClassMatcher` {
        __init(_i, _bb)
        return this
    }
    val smaliSource : `-StringMatcher`? get() = smaliSource(`-StringMatcher`())
    fun smaliSource(obj: `-StringMatcher`) : `-StringMatcher`? {
        val o = __offset(4)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    val className : `-StringMatcher`? get() = className(`-StringMatcher`())
    fun className(obj: `-StringMatcher`) : `-StringMatcher`? {
        val o = __offset(6)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    val accessFlags : `-AccessFlagsMatcher`? get() = accessFlags(`-AccessFlagsMatcher`())
    fun accessFlags(obj: `-AccessFlagsMatcher`) : `-AccessFlagsMatcher`? {
        val o = __offset(8)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    val superClass : `-ClassMatcher`? get() = superClass(`-ClassMatcher`())
    fun superClass(obj: `-ClassMatcher`) : `-ClassMatcher`? {
        val o = __offset(10)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    val interfaces : `-InterfacesMatcher`? get() = interfaces(`-InterfacesMatcher`())
    fun interfaces(obj: `-InterfacesMatcher`) : `-InterfacesMatcher`? {
        val o = __offset(12)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    val annotations : `-AnnotationsMatcher`? get() = annotations(`-AnnotationsMatcher`())
    fun annotations(obj: `-AnnotationsMatcher`) : `-AnnotationsMatcher`? {
        val o = __offset(14)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    val fields : `-FieldsMatcher`? get() = fields(`-FieldsMatcher`())
    fun fields(obj: `-FieldsMatcher`) : `-FieldsMatcher`? {
        val o = __offset(16)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    val methods : `-MethodsMatcher`? get() = methods(`-MethodsMatcher`())
    fun methods(obj: `-MethodsMatcher`) : `-MethodsMatcher`? {
        val o = __offset(18)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    fun usingStrings(j: Int) : `-StringMatcher`? = usingStrings(`-StringMatcher`(), j)
    fun usingStrings(obj: `-StringMatcher`, j: Int) : `-StringMatcher`? {
        val o = __offset(20)
        return if (o != 0) {
            obj.__assign(__indirect(__vector(o) + j * 4), bb)
        } else {
            null
        }
    }
    val usingStringsLength : Int
        get() {
            val o = __offset(20); return if (o != 0) __vector_len(o) else 0
        }
    companion object {
        fun validateVersion() = Constants.FLATBUFFERS_23_5_26()
        fun getRootAsClassMatcher(_bb: ByteBuffer): `-ClassMatcher` = getRootAsClassMatcher(_bb, `-ClassMatcher`())
        fun getRootAsClassMatcher(_bb: ByteBuffer, obj: `-ClassMatcher`): `-ClassMatcher` {
            _bb.order(ByteOrder.LITTLE_ENDIAN)
            return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb))
        }
        fun createClassMatcher(builder: FlatBufferBuilder, smaliSourceOffset: Int, classNameOffset: Int, accessFlagsOffset: Int, superClassOffset: Int, interfacesOffset: Int, annotationsOffset: Int, fieldsOffset: Int, methodsOffset: Int, usingStringsOffset: Int) : Int {
            builder.startTable(9)
            addUsingStrings(builder, usingStringsOffset)
            addMethods(builder, methodsOffset)
            addFields(builder, fieldsOffset)
            addAnnotations(builder, annotationsOffset)
            addInterfaces(builder, interfacesOffset)
            addSuperClass(builder, superClassOffset)
            addAccessFlags(builder, accessFlagsOffset)
            addClassName(builder, classNameOffset)
            addSmaliSource(builder, smaliSourceOffset)
            return endClassMatcher(builder)
        }
        fun startClassMatcher(builder: FlatBufferBuilder) = builder.startTable(9)
        fun addSmaliSource(builder: FlatBufferBuilder, smaliSource: Int) = builder.addOffset(0, smaliSource, 0)
        fun addClassName(builder: FlatBufferBuilder, className: Int) = builder.addOffset(1, className, 0)
        fun addAccessFlags(builder: FlatBufferBuilder, accessFlags: Int) = builder.addOffset(2, accessFlags, 0)
        fun addSuperClass(builder: FlatBufferBuilder, superClass: Int) = builder.addOffset(3, superClass, 0)
        fun addInterfaces(builder: FlatBufferBuilder, interfaces: Int) = builder.addOffset(4, interfaces, 0)
        fun addAnnotations(builder: FlatBufferBuilder, annotations: Int) = builder.addOffset(5, annotations, 0)
        fun addFields(builder: FlatBufferBuilder, fields: Int) = builder.addOffset(6, fields, 0)
        fun addMethods(builder: FlatBufferBuilder, methods: Int) = builder.addOffset(7, methods, 0)
        fun addUsingStrings(builder: FlatBufferBuilder, usingStrings: Int) = builder.addOffset(8, usingStrings, 0)
        fun createUsingStringsVector(builder: FlatBufferBuilder, data: IntArray) : Int {
            builder.startVector(4, data.size, 4)
            for (i in data.size - 1 downTo 0) {
                builder.addOffset(data[i])
            }
            return builder.endVector()
        }
        fun startUsingStringsVector(builder: FlatBufferBuilder, numElems: Int) = builder.startVector(4, numElems, 4)
        fun endClassMatcher(builder: FlatBufferBuilder) : Int {
            val o = builder.endTable()
            return o
        }
    }
}