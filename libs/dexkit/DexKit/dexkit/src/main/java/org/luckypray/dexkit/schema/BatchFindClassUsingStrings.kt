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
internal class `-BatchFindClassUsingStrings` : Table() {

    fun __init(_i: Int, _bb: ByteBuffer)  {
        __reset(_i, _bb)
    }
    fun __assign(_i: Int, _bb: ByteBuffer) : `-BatchFindClassUsingStrings` {
        __init(_i, _bb)
        return this
    }
    fun searchPackages(j: Int) : String? {
        val o = __offset(4)
        return if (o != 0) {
            __string(__vector(o) + j * 4)
        } else {
            null
        }
    }
    val searchPackagesLength : Int
        get() {
            val o = __offset(4); return if (o != 0) __vector_len(o) else 0
        }
    fun excludePackages(j: Int) : String? {
        val o = __offset(6)
        return if (o != 0) {
            __string(__vector(o) + j * 4)
        } else {
            null
        }
    }
    val excludePackagesLength : Int
        get() {
            val o = __offset(6); return if (o != 0) __vector_len(o) else 0
        }
    val ignorePackagesCase : Boolean
        get() {
            val o = __offset(8)
            return if(o != 0) 0.toByte() != bb.get(o + bb_pos) else false
        }
    fun mutateIgnorePackagesCase(ignorePackagesCase: Boolean) : Boolean {
        val o = __offset(8)
        return if (o != 0) {
            bb.put(o + bb_pos, (if(ignorePackagesCase) 1 else 0).toByte())
            true
        } else {
            false
        }
    }
    fun inClasses(j: Int) : Long {
        val o = __offset(10)
        return if (o != 0) {
            bb.getLong(__vector(o) + j * 8)
        } else {
            0
        }
    }
    val inClassesLength : Int
        get() {
            val o = __offset(10); return if (o != 0) __vector_len(o) else 0
        }
    val inClassesAsByteBuffer : ByteBuffer get() = __vector_as_bytebuffer(10, 8)
    fun inClassesInByteBuffer(_bb: ByteBuffer) : ByteBuffer = __vector_in_bytebuffer(_bb, 10, 8)
    fun mutateInClasses(j: Int, inClasses: Long) : Boolean {
        val o = __offset(10)
        return if (o != 0) {
            bb.putLong(__vector(o) + j * 8, inClasses)
            true
        } else {
            false
        }
    }
    fun matchers(j: Int) : `-BatchUsingStringsMatcher`? = matchers(`-BatchUsingStringsMatcher`(), j)
    fun matchers(obj: `-BatchUsingStringsMatcher`, j: Int) : `-BatchUsingStringsMatcher`? {
        val o = __offset(12)
        return if (o != 0) {
            obj.__assign(__indirect(__vector(o) + j * 4), bb)
        } else {
            null
        }
    }
    val matchersLength : Int
        get() {
            val o = __offset(12); return if (o != 0) __vector_len(o) else 0
        }
    companion object {
        fun validateVersion() = Constants.FLATBUFFERS_23_5_26()
        fun getRootAsBatchFindClassUsingStrings(_bb: ByteBuffer): `-BatchFindClassUsingStrings` = getRootAsBatchFindClassUsingStrings(_bb, `-BatchFindClassUsingStrings`())
        fun getRootAsBatchFindClassUsingStrings(_bb: ByteBuffer, obj: `-BatchFindClassUsingStrings`): `-BatchFindClassUsingStrings` {
            _bb.order(ByteOrder.LITTLE_ENDIAN)
            return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb))
        }
        fun createBatchFindClassUsingStrings(builder: FlatBufferBuilder, searchPackagesOffset: Int, excludePackagesOffset: Int, ignorePackagesCase: Boolean, inClassesOffset: Int, matchersOffset: Int) : Int {
            builder.startTable(5)
            addMatchers(builder, matchersOffset)
            addInClasses(builder, inClassesOffset)
            addExcludePackages(builder, excludePackagesOffset)
            addSearchPackages(builder, searchPackagesOffset)
            addIgnorePackagesCase(builder, ignorePackagesCase)
            return endBatchFindClassUsingStrings(builder)
        }
        fun startBatchFindClassUsingStrings(builder: FlatBufferBuilder) = builder.startTable(5)
        fun addSearchPackages(builder: FlatBufferBuilder, searchPackages: Int) = builder.addOffset(0, searchPackages, 0)
        fun createSearchPackagesVector(builder: FlatBufferBuilder, data: IntArray) : Int {
            builder.startVector(4, data.size, 4)
            for (i in data.size - 1 downTo 0) {
                builder.addOffset(data[i])
            }
            return builder.endVector()
        }
        fun startSearchPackagesVector(builder: FlatBufferBuilder, numElems: Int) = builder.startVector(4, numElems, 4)
        fun addExcludePackages(builder: FlatBufferBuilder, excludePackages: Int) = builder.addOffset(1, excludePackages, 0)
        fun createExcludePackagesVector(builder: FlatBufferBuilder, data: IntArray) : Int {
            builder.startVector(4, data.size, 4)
            for (i in data.size - 1 downTo 0) {
                builder.addOffset(data[i])
            }
            return builder.endVector()
        }
        fun startExcludePackagesVector(builder: FlatBufferBuilder, numElems: Int) = builder.startVector(4, numElems, 4)
        fun addIgnorePackagesCase(builder: FlatBufferBuilder, ignorePackagesCase: Boolean) = builder.addBoolean(2, ignorePackagesCase, false)
        fun addInClasses(builder: FlatBufferBuilder, inClasses: Int) = builder.addOffset(3, inClasses, 0)
        fun createInClassesVector(builder: FlatBufferBuilder, data: LongArray) : Int {
            builder.startVector(8, data.size, 8)
            for (i in data.size - 1 downTo 0) {
                builder.addLong(data[i])
            }
            return builder.endVector()
        }
        fun startInClassesVector(builder: FlatBufferBuilder, numElems: Int) = builder.startVector(8, numElems, 8)
        fun addMatchers(builder: FlatBufferBuilder, matchers: Int) = builder.addOffset(4, matchers, 0)
        fun createMatchersVector(builder: FlatBufferBuilder, data: IntArray) : Int {
            builder.startVector(4, data.size, 4)
            for (i in data.size - 1 downTo 0) {
                builder.addOffset(data[i])
            }
            return builder.endVector()
        }
        fun startMatchersVector(builder: FlatBufferBuilder, numElems: Int) = builder.startVector(4, numElems, 4)
        fun endBatchFindClassUsingStrings(builder: FlatBufferBuilder) : Int {
            val o = builder.endTable()
            return o
        }
    }
}