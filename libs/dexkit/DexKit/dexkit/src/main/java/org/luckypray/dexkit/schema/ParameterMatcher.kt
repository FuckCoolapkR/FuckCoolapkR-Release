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
internal class `-ParameterMatcher` : Table() {

    fun __init(_i: Int, _bb: ByteBuffer)  {
        __reset(_i, _bb)
    }
    fun __assign(_i: Int, _bb: ByteBuffer) : `-ParameterMatcher` {
        __init(_i, _bb)
        return this
    }
    val annotations : `-AnnotationsMatcher`? get() = annotations(`-AnnotationsMatcher`())
    fun annotations(obj: `-AnnotationsMatcher`) : `-AnnotationsMatcher`? {
        val o = __offset(4)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    val parameterType : `-ClassMatcher`? get() = parameterType(`-ClassMatcher`())
    fun parameterType(obj: `-ClassMatcher`) : `-ClassMatcher`? {
        val o = __offset(6)
        return if (o != 0) {
            obj.__assign(__indirect(o + bb_pos), bb)
        } else {
            null
        }
    }
    companion object {
        fun validateVersion() = Constants.FLATBUFFERS_23_5_26()
        fun getRootAsParameterMatcher(_bb: ByteBuffer): `-ParameterMatcher` = getRootAsParameterMatcher(_bb, `-ParameterMatcher`())
        fun getRootAsParameterMatcher(_bb: ByteBuffer, obj: `-ParameterMatcher`): `-ParameterMatcher` {
            _bb.order(ByteOrder.LITTLE_ENDIAN)
            return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb))
        }
        fun createParameterMatcher(builder: FlatBufferBuilder, annotationsOffset: Int, parameterTypeOffset: Int) : Int {
            builder.startTable(2)
            addParameterType(builder, parameterTypeOffset)
            addAnnotations(builder, annotationsOffset)
            return endParameterMatcher(builder)
        }
        fun startParameterMatcher(builder: FlatBufferBuilder) = builder.startTable(2)
        fun addAnnotations(builder: FlatBufferBuilder, annotations: Int) = builder.addOffset(0, annotations, 0)
        fun addParameterType(builder: FlatBufferBuilder, parameterType: Int) = builder.addOffset(1, parameterType, 0)
        fun endParameterMatcher(builder: FlatBufferBuilder) : Int {
            val o = builder.endTable()
            return o
        }
    }
}