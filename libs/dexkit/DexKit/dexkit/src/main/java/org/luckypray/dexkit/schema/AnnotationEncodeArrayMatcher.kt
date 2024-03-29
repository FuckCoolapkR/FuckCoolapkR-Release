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
internal class `-AnnotationEncodeArrayMatcher` : Table() {

    fun __init(_i: Int, _bb: ByteBuffer)  {
        __reset(_i, _bb)
    }
    fun __assign(_i: Int, _bb: ByteBuffer) : `-AnnotationEncodeArrayMatcher` {
        __init(_i, _bb)
        return this
    }
    fun valuesType(j: Int) : UByte {
        val o = __offset(4)
        return if (o != 0) {
            bb.get(__vector(o) + j * 1).toUByte()
        } else {
            0u
        }
    }
    val valuesTypeLength : Int
        get() {
            val o = __offset(4); return if (o != 0) __vector_len(o) else 0
        }
    val valuesTypeAsByteBuffer : ByteBuffer get() = __vector_as_bytebuffer(4, 1)
    fun valuesTypeInByteBuffer(_bb: ByteBuffer) : ByteBuffer = __vector_in_bytebuffer(_bb, 4, 1)
    fun mutateValuesType(j: Int, valuesType: UByte) : Boolean {
        val o = __offset(4)
        return if (o != 0) {
            bb.put(__vector(o) + j * 1, valuesType.toByte())
            true
        } else {
            false
        }
    }
    fun values(obj: Table, j: Int) : Table? {
        val o = __offset(6)
        return if (o != 0) {
            __union(obj, __vector(o) + j * 4)
        } else {
            null
        }
    }
    val valuesLength : Int
        get() {
            val o = __offset(6); return if (o != 0) __vector_len(o) else 0
        }
    val matchType : Byte
        get() {
            val o = __offset(8)
            return if(o != 0) bb.get(o + bb_pos) else 0
        }
    fun mutateMatchType(matchType: Byte) : Boolean {
        val o = __offset(8)
        return if (o != 0) {
            bb.put(o + bb_pos, matchType)
            true
        } else {
            false
        }
    }
    val valueCount : `-IntRange`? get() = valueCount(`-IntRange`())
    fun valueCount(obj: `-IntRange`) : `-IntRange`? {
        val o = __offset(10)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    companion object {
        fun validateVersion() = Constants.FLATBUFFERS_23_5_26()
        fun getRootAsAnnotationEncodeArrayMatcher(_bb: ByteBuffer): `-AnnotationEncodeArrayMatcher` = getRootAsAnnotationEncodeArrayMatcher(_bb, `-AnnotationEncodeArrayMatcher`())
        fun getRootAsAnnotationEncodeArrayMatcher(_bb: ByteBuffer, obj: `-AnnotationEncodeArrayMatcher`): `-AnnotationEncodeArrayMatcher` {
            _bb.order(ByteOrder.LITTLE_ENDIAN)
            return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb))
        }
        fun createAnnotationEncodeArrayMatcher(builder: FlatBufferBuilder, valuesTypeOffset: Int, valuesOffset: Int, matchType: Byte, valueCountOffset: Int) : Int {
            builder.startTable(4)
            addValueCount(builder, valueCountOffset)
            addValues(builder, valuesOffset)
            addValuesType(builder, valuesTypeOffset)
            addMatchType(builder, matchType)
            return endAnnotationEncodeArrayMatcher(builder)
        }
        fun startAnnotationEncodeArrayMatcher(builder: FlatBufferBuilder) = builder.startTable(4)
        fun addValuesType(builder: FlatBufferBuilder, valuesType: Int) = builder.addOffset(0, valuesType, 0)
        @kotlin.ExperimentalUnsignedTypes
        fun createValuesTypeVector(builder: FlatBufferBuilder, data: UByteArray) : Int {
            builder.startVector(1, data.size, 1)
            for (i in data.size - 1 downTo 0) {
                builder.addByte(data[i].toByte())
            }
            return builder.endVector()
        }
        fun startValuesTypeVector(builder: FlatBufferBuilder, numElems: Int) = builder.startVector(1, numElems, 1)
        fun addValues(builder: FlatBufferBuilder, values: Int) = builder.addOffset(1, values, 0)
        fun createValuesVector(builder: FlatBufferBuilder, data: IntArray) : Int {
            builder.startVector(4, data.size, 4)
            for (i in data.size - 1 downTo 0) {
                builder.addOffset(data[i])
            }
            return builder.endVector()
        }
        fun startValuesVector(builder: FlatBufferBuilder, numElems: Int) = builder.startVector(4, numElems, 4)
        fun addMatchType(builder: FlatBufferBuilder, matchType: Byte) = builder.addByte(2, matchType, 0)
        fun addValueCount(builder: FlatBufferBuilder, valueCount: Int) = builder.addOffset(3, valueCount, 0)
        fun endAnnotationEncodeArrayMatcher(builder: FlatBufferBuilder) : Int {
            val o = builder.endTable()
            return o
        }
    }
}