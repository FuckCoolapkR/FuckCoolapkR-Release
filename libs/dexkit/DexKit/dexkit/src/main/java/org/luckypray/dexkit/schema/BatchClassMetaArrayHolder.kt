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
internal class `-BatchClassMetaArrayHolder` : Table() {

    fun __init(_i: Int, _bb: ByteBuffer)  {
        __reset(_i, _bb)
    }
    fun __assign(_i: Int, _bb: ByteBuffer) : `-BatchClassMetaArrayHolder` {
        __init(_i, _bb)
        return this
    }
    fun items(j: Int) : `-BatchClassMeta`? = items(`-BatchClassMeta`(), j)
    fun items(obj: `-BatchClassMeta`, j: Int) : `-BatchClassMeta`? {
        val o = __offset(4)
        return if (o != 0) {
            obj.__assign(__indirect(__vector(o) + j * 4), bb)
        } else {
            null
        }
    }
    val itemsLength : Int
        get() {
            val o = __offset(4); return if (o != 0) __vector_len(o) else 0
        }
    companion object {
        fun validateVersion() = Constants.FLATBUFFERS_23_5_26()
        fun getRootAsBatchClassMetaArrayHolder(_bb: ByteBuffer): `-BatchClassMetaArrayHolder` = getRootAsBatchClassMetaArrayHolder(_bb, `-BatchClassMetaArrayHolder`())
        fun getRootAsBatchClassMetaArrayHolder(_bb: ByteBuffer, obj: `-BatchClassMetaArrayHolder`): `-BatchClassMetaArrayHolder` {
            _bb.order(ByteOrder.LITTLE_ENDIAN)
            return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb))
        }
        fun createBatchClassMetaArrayHolder(builder: FlatBufferBuilder, itemsOffset: Int) : Int {
            builder.startTable(1)
            addItems(builder, itemsOffset)
            return endBatchClassMetaArrayHolder(builder)
        }
        fun startBatchClassMetaArrayHolder(builder: FlatBufferBuilder) = builder.startTable(1)
        fun addItems(builder: FlatBufferBuilder, items: Int) = builder.addOffset(0, items, 0)
        fun createItemsVector(builder: FlatBufferBuilder, data: IntArray) : Int {
            builder.startVector(4, data.size, 4)
            for (i in data.size - 1 downTo 0) {
                builder.addOffset(data[i])
            }
            return builder.endVector()
        }
        fun startItemsVector(builder: FlatBufferBuilder, numElems: Int) = builder.startVector(4, numElems, 4)
        fun endBatchClassMetaArrayHolder(builder: FlatBufferBuilder) : Int {
            val o = builder.endTable()
            return o
        }
    }
}