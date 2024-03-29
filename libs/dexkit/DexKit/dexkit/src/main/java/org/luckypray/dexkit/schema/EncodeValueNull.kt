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
internal class `-EncodeValueNull` : Table() {

    fun __init(_i: Int, _bb: ByteBuffer)  {
        __reset(_i, _bb)
    }
    fun __assign(_i: Int, _bb: ByteBuffer) : `-EncodeValueNull` {
        __init(_i, _bb)
        return this
    }
    val value : Byte
        get() {
            val o = __offset(4)
            return if(o != 0) bb.get(o + bb_pos) else 0
        }
    fun mutateValue(value: Byte) : Boolean {
        val o = __offset(4)
        return if (o != 0) {
            bb.put(o + bb_pos, value)
            true
        } else {
            false
        }
    }
    companion object {
        fun validateVersion() = Constants.FLATBUFFERS_23_5_26()
        fun getRootAsEncodeValueNull(_bb: ByteBuffer): `-EncodeValueNull` = getRootAsEncodeValueNull(_bb, `-EncodeValueNull`())
        fun getRootAsEncodeValueNull(_bb: ByteBuffer, obj: `-EncodeValueNull`): `-EncodeValueNull` {
            _bb.order(ByteOrder.LITTLE_ENDIAN)
            return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb))
        }
        fun createEncodeValueNull(builder: FlatBufferBuilder, value: Byte) : Int {
            builder.startTable(1)
            addValue(builder, value)
            return endEncodeValueNull(builder)
        }
        fun startEncodeValueNull(builder: FlatBufferBuilder) = builder.startTable(1)
        fun addValue(builder: FlatBufferBuilder, value: Byte) = builder.addByte(0, value, 0)
        fun endEncodeValueNull(builder: FlatBufferBuilder) : Int {
            val o = builder.endTable()
            return o
        }
    }
}