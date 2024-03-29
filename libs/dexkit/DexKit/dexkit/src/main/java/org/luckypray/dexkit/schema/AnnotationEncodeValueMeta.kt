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
internal class `-AnnotationEncodeValueMeta` : Table() {

    fun __init(_i: Int, _bb: ByteBuffer)  {
        __reset(_i, _bb)
    }
    fun __assign(_i: Int, _bb: ByteBuffer) : `-AnnotationEncodeValueMeta` {
        __init(_i, _bb)
        return this
    }
    val type : Byte
        get() {
            val o = __offset(4)
            return if(o != 0) bb.get(o + bb_pos) else 0
        }
    fun mutateType(type: Byte) : Boolean {
        val o = __offset(4)
        return if (o != 0) {
            bb.put(o + bb_pos, type)
            true
        } else {
            false
        }
    }
    val valueType : UByte
        get() {
            val o = __offset(6)
            return if(o != 0) bb.get(o + bb_pos).toUByte() else 0u
        }
    fun mutateValueType(valueType: UByte) : Boolean {
        val o = __offset(6)
        return if (o != 0) {
            bb.put(o + bb_pos, valueType.toByte())
            true
        } else {
            false
        }
    }
    fun value(obj: Table) : Table? {
        val o = __offset(8); return if (o != 0) __union(obj, o + bb_pos) else null
    }
    companion object {
        fun validateVersion() = Constants.FLATBUFFERS_23_5_26()
        fun getRootAsAnnotationEncodeValueMeta(_bb: ByteBuffer): `-AnnotationEncodeValueMeta` = getRootAsAnnotationEncodeValueMeta(_bb, `-AnnotationEncodeValueMeta`())
        fun getRootAsAnnotationEncodeValueMeta(_bb: ByteBuffer, obj: `-AnnotationEncodeValueMeta`): `-AnnotationEncodeValueMeta` {
            _bb.order(ByteOrder.LITTLE_ENDIAN)
            return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb))
        }
        fun createAnnotationEncodeValueMeta(builder: FlatBufferBuilder, type: Byte, valueType: UByte, valueOffset: Int) : Int {
            builder.startTable(3)
            addValue(builder, valueOffset)
            addValueType(builder, valueType)
            addType(builder, type)
            return endAnnotationEncodeValueMeta(builder)
        }
        fun startAnnotationEncodeValueMeta(builder: FlatBufferBuilder) = builder.startTable(3)
        fun addType(builder: FlatBufferBuilder, type: Byte) = builder.addByte(0, type, 0)
        fun addValueType(builder: FlatBufferBuilder, valueType: UByte) = builder.addByte(1, valueType.toByte(), 0)
        fun addValue(builder: FlatBufferBuilder, value: Int) = builder.addOffset(2, value, 0)
        fun endAnnotationEncodeValueMeta(builder: FlatBufferBuilder) : Int {
            val o = builder.endTable()
            return o
        }
    }
}