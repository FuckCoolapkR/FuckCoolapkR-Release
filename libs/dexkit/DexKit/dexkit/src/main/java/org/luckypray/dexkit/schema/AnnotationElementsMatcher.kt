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
internal class `-AnnotationElementsMatcher` : Table() {

    fun __init(_i: Int, _bb: ByteBuffer)  {
        __reset(_i, _bb)
    }
    fun __assign(_i: Int, _bb: ByteBuffer) : `-AnnotationElementsMatcher` {
        __init(_i, _bb)
        return this
    }
    fun elements(j: Int) : `-AnnotationElementMatcher`? = elements(`-AnnotationElementMatcher`(), j)
    fun elements(obj: `-AnnotationElementMatcher`, j: Int) : `-AnnotationElementMatcher`? {
        val o = __offset(4)
        return if (o != 0) {
            obj.__assign(__indirect(__vector(o) + j * 4), bb)
        } else {
            null
        }
    }
    val elementsLength : Int
        get() {
            val o = __offset(4); return if (o != 0) __vector_len(o) else 0
        }
    val matchType : Byte
        get() {
            val o = __offset(6)
            return if(o != 0) bb.get(o + bb_pos) else 0
        }
    fun mutateMatchType(matchType: Byte) : Boolean {
        val o = __offset(6)
        return if (o != 0) {
            bb.put(o + bb_pos, matchType)
            true
        } else {
            false
        }
    }
    val elementCount : `-IntRange`? get() = elementCount(`-IntRange`())
    fun elementCount(obj: `-IntRange`) : `-IntRange`? {
        val o = __offset(8)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    companion object {
        fun validateVersion() = Constants.FLATBUFFERS_23_5_26()
        fun getRootAsAnnotationElementsMatcher(_bb: ByteBuffer): `-AnnotationElementsMatcher` = getRootAsAnnotationElementsMatcher(_bb, `-AnnotationElementsMatcher`())
        fun getRootAsAnnotationElementsMatcher(_bb: ByteBuffer, obj: `-AnnotationElementsMatcher`): `-AnnotationElementsMatcher` {
            _bb.order(ByteOrder.LITTLE_ENDIAN)
            return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb))
        }
        fun createAnnotationElementsMatcher(builder: FlatBufferBuilder, elementsOffset: Int, matchType: Byte, elementCountOffset: Int) : Int {
            builder.startTable(3)
            addElementCount(builder, elementCountOffset)
            addElements(builder, elementsOffset)
            addMatchType(builder, matchType)
            return endAnnotationElementsMatcher(builder)
        }
        fun startAnnotationElementsMatcher(builder: FlatBufferBuilder) = builder.startTable(3)
        fun addElements(builder: FlatBufferBuilder, elements: Int) = builder.addOffset(0, elements, 0)
        fun createElementsVector(builder: FlatBufferBuilder, data: IntArray) : Int {
            builder.startVector(4, data.size, 4)
            for (i in data.size - 1 downTo 0) {
                builder.addOffset(data[i])
            }
            return builder.endVector()
        }
        fun startElementsVector(builder: FlatBufferBuilder, numElems: Int) = builder.startVector(4, numElems, 4)
        fun addMatchType(builder: FlatBufferBuilder, matchType: Byte) = builder.addByte(1, matchType, 0)
        fun addElementCount(builder: FlatBufferBuilder, elementCount: Int) = builder.addOffset(2, elementCount, 0)
        fun endAnnotationElementsMatcher(builder: FlatBufferBuilder) : Int {
            val o = builder.endTable()
            return o
        }
    }
}