/* Copyright 2017-18, Emmanouil Antonios Platanios. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.platanios.tensorflow.api

import org.platanios.tensorflow.jni.{Tensor => NativeTensor}

import com.google.protobuf.ByteString
import org.tensorflow.framework.DataType._
import org.tensorflow.framework.TensorProto

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

/**
  * @author Emmanouil Antonios Platanios
  */
package object types {
  // TODO: [TYPES] Add some useful implementations.

  case class Half(private[types] val data: Short) extends AnyVal
  case class TruncatedHalf(private[types] val data: Short) extends AnyVal
  case class ComplexFloat(real: Float, imaginary: Float)
  case class ComplexDouble(real: Double, imaginary: Double)
  case class UByte(private[types] val data: Byte) extends AnyVal
  case class UShort(private[types] val data: Short) extends AnyVal
  case class UInt(private[types] val data: Int) extends AnyVal
  case class ULong(private[types] val data: Long) extends AnyVal
  case class QByte(private[types] val data: Byte) extends AnyVal
  case class QShort(private[types] val data: Short) extends AnyVal
  case class QInt(private[types] val data: Int) extends AnyVal
  case class QUByte(private[types] val data: Byte) extends AnyVal
  case class QUShort(private[types] val data: Short) extends AnyVal

  type STRING = types.STRING.type
  type BOOLEAN = types.BOOLEAN.type
  type FLOAT16 = types.FLOAT16.type
  type FLOAT32 = types.FLOAT32.type
  type FLOAT64 = types.FLOAT64.type
  type BFLOAT16 = types.BFLOAT16.type
  type COMPLEX64 = types.COMPLEX64.type
  type COMPLEX128 = types.COMPLEX128.type
  type INT8 = types.INT8.type
  type INT16 = types.INT16.type
  type INT32 = types.INT32.type
  type INT64 = types.INT64.type
  type UINT8 = types.UINT8.type
  type UINT16 = types.UINT16.type
  type UINT32 = types.UINT32.type
  type UINT64 = types.UINT64.type
  type QINT8 = types.QINT8.type
  type QINT16 = types.QINT16.type
  type QINT32 = types.QINT32.type
  type QUINT8 = types.QUINT8.type
  type QUINT16 = types.QUINT16.type
  type RESOURCE = types.RESOURCE.type
  type VARIANT = types.VARIANT.type

  object STRING extends DataType[String](
    name = "STRING",
    cValue = 7,
    byteSize = None,
    protoType = DT_STRING
  ) {
    override val priority: Int    = 1000

    override def zero: String = ""
    override def one: String = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: String): Int = {
      val stringBytes = element.getBytes(StandardCharsets.ISO_8859_1)
      NativeTensor.setStringBytes(stringBytes, buffer.duplicate().position(index).asInstanceOf[ByteBuffer].slice())
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): String = {
      val stringBytes = NativeTensor.getStringBytes(buffer.duplicate().position(index).asInstanceOf[ByteBuffer].slice())
      new String(stringBytes, StandardCharsets.ISO_8859_1)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: String): Unit = {
      tensorProtoBuilder.addStringVal(ByteString.copyFrom(value.getBytes))
    }
  }

  object BOOLEAN extends DataType[Boolean](
    name = "BOOLEAN",
    cValue = 10,
    byteSize = Some(1),
    protoType = DT_BOOL
  ) {
    override val priority: Int    = 0

    override def zero: Boolean = false
    override def one: Boolean = true

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: Boolean): Int = {
      buffer.put(index, if (element) 1 else 0)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): Boolean = {
      buffer.get(index) == 1
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: Boolean): Unit = {
      tensorProtoBuilder.addBoolVal(value)
    }
  }

  // TODO: Fix/complete the following implementations for FLOAT16, BFLOAT16, COMPLEX64, and COMPLEX128.

  object FLOAT16 extends DataType[Half](
    name = "FLOAT16",
    cValue = 19,
    byteSize = Some(2),
    protoType = DT_HALF
  ) {
    override val priority: Int    = -1

    override def zero: Half = ??? // 0.0f
    override def one: Half = ??? // 1.0f
    override def min: Half = ??? // -65504f
    override def max: Half = ??? // 65504f

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: Half): Int = {
      ???
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): Half = {
      ???
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: Half): Unit = {
      ???
    }
  }

  object FLOAT32 extends DataType[Float](
    name = "FLOAT32",
    cValue = 1,
    byteSize = Some(4),
    protoType = DT_FLOAT
  ) {
    override val priority: Int    = 220

    override def zero: Float = 0.0f
    override def one: Float = 1.0f
    override def min: Float = Float.MinValue
    override def max: Float = Float.MaxValue

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: Float): Int = {
      buffer.putFloat(index, element)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): Float = {
      buffer.getFloat(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: Float): Unit = {
      tensorProtoBuilder.addFloatVal(value)
    }
  }

  object FLOAT64 extends DataType[Double](
    name = "FLOAT64",
    cValue = 2,
    byteSize = Some(8),
    protoType = DT_DOUBLE
  ) {
    override val priority: Int    = 230

    override def zero: Double = 0.0
    override def one: Double = 1.0
    override def min: Double = Double.MinValue
    override def max: Double = Double.MaxValue

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: Double): Int = {
      buffer.putDouble(index, element)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): Double = {
      buffer.getDouble(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: Double): Unit = {
      tensorProtoBuilder.addDoubleVal(value)
    }
  }

  object BFLOAT16 extends DataType[TruncatedHalf](
    name = "BFLOAT16",
    cValue = 14,
    byteSize = Some(2),
    protoType = DT_BFLOAT16
  ) {
    override val priority: Int    = -1

    override def zero: TruncatedHalf = ??? // 0.0f
    override def one: TruncatedHalf = ??? // 1.0f
    override def min: TruncatedHalf = ???
    override def max: TruncatedHalf = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: TruncatedHalf): Int = {
      ???
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): TruncatedHalf = {
      ???
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: TruncatedHalf): Unit = {
      ???
    }
  }

  object COMPLEX64 extends DataType[ComplexFloat](
    name = "COMPLEX64",
    cValue = 8,
    byteSize = Some(8),
    protoType = DT_COMPLEX64
  ) {
    override val priority: Int    = -1

    override def zero: ComplexFloat = ???
    override def one: ComplexFloat = ???
    override def min: ComplexFloat = ???
    override def max: ComplexFloat = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: ComplexFloat): Int = {
      ???
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): ComplexFloat = {
      ???
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: ComplexFloat): Unit = {
      ???
    }
  }

  object COMPLEX128 extends DataType[ComplexDouble](
    name = "COMPLEX128",
    cValue = 18,
    byteSize = Some(16),
    protoType = DT_COMPLEX128
  ) {
    override val priority: Int    = -1

    override def zero: ComplexDouble = ???
    override def one: ComplexDouble = ???
    override def min: ComplexDouble = ???
    override def max: ComplexDouble = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: ComplexDouble): Int = {
      ???
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): ComplexDouble = {
      ???
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: ComplexDouble): Unit = {
      ???
    }
  }

  object INT8 extends DataType[Byte](
    name = "INT8",
    cValue = 6,
    byteSize = Some(1),
    protoType = DT_INT8
  ) {
    override val priority: Int    = 40

    override def zero: Byte = 0
    override def one: Byte = 1
    override def min: Byte = (-128).toByte
    override def max: Byte = 127.toByte

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: Byte): Int = {
      buffer.put(index, element)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): Byte = {
      buffer.get(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: Byte): Unit = {
      tensorProtoBuilder.addIntVal(value)
    }
  }

  object INT16 extends DataType[Short](
    name = "INT16",
    cValue = 5,
    byteSize = Some(2),
    protoType = DT_INT16
  ) {
    override val priority: Int    = 80

    override def zero: Short = 0
    override def one: Short = 1
    override def min: Short = (-32768).toShort
    override def max: Short = 32767.toShort

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: Short): Int = {
      buffer.putShort(index, element)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): Short = {
      buffer.getShort(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: Short): Unit = {
      tensorProtoBuilder.addIntVal(value)
    }
  }

  object INT32 extends DataType[Int](
    name = "INT32",
    cValue = 3,
    byteSize = Some(4),
    protoType = DT_INT32
  ) {
    override val priority: Int    = 100

    override def zero: Int = 0
    override def one: Int = 1
    override def min: Int = -2147483648
    override def max: Int = 2147483647

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: Int): Int = {
      buffer.putInt(index, element)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): Int = {
      buffer.getInt(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: Int): Unit = {
      tensorProtoBuilder.addIntVal(value)
    }
  }

  object INT64 extends DataType[Long](
    name = "INT64",
    cValue = 9,
    byteSize = Some(8),
    protoType = DT_INT64
  ) {
    override val priority: Int    = 110

    override def zero: Long = 0L
    override def one: Long = 1L
    override def min: Long = -9223372036854775808L
    override def max: Long = 9223372036854775807L

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: Long): Int = {
      buffer.putLong(index, element)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): Long = {
      buffer.getLong(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: Long): Unit = {
      tensorProtoBuilder.addInt64Val(value)
    }
  }

  object UINT8 extends DataType[UByte](
    name = "UINT8",
    cValue = 4,
    byteSize = Some(1),
    protoType = DT_UINT8
  ) {
    override val priority: Int    = 20

    override def zero: UByte = ??? // UByte(0)
    override def one: UByte = ??? // UByte(1)
    override def min: UByte = ??? // UByte(0)
    override def max: UByte = ??? // UByte(255)

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: UByte): Int = {
      buffer.put(index, element.data)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): UByte = {
      UByte(buffer.get(index))
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: UByte): Unit = {
      tensorProtoBuilder.addIntVal(value.data.toInt)
    }
  }

  object UINT16 extends DataType[UShort](
    name = "UINT16",
    cValue = 17,
    byteSize = Some(2),
    protoType = DT_UINT16
  ) {
    override val priority: Int    = 60

    override def zero: UShort = ??? // UShort(0)
    override def one: UShort = ??? // UShort(1)
    override def min: UShort = ??? // UShort(0)
    override def max: UShort = ??? // UShort(65535)

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: UShort): Int = {
      buffer.putChar(index, element.data.toChar)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): UShort = {
      UShort(buffer.getChar(index))
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: UShort): Unit = {
      tensorProtoBuilder.addIntVal(value.data.toInt)
    }
  }

  object UINT32 extends DataType[UInt](
    name = "UINT32",
    cValue = 22,
    byteSize = Some(4),
    protoType = DT_UINT32
  ) {
    override val priority: Int    = 85

    override def zero: UInt = ??? // 0L
    override def one: UInt = ??? // 1L
    override def min: UInt = ??? // 0L
    override def max: UInt = ??? // 9223372036854775807L

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: UInt): Int = {
      buffer.putInt(index, element.data.toInt)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): UInt = {
      ??? // buffer.getInt(index).toLong
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: UInt): Unit = {
      ???
    }
  }

  object UINT64 extends DataType[ULong](
    name = "UINT64",
    cValue = 23,
    byteSize = Some(8),
    protoType = DT_UINT64
  ) {
    override val priority: Int    = 105

    override def zero: ULong = ???
    override def one: ULong = ???
    override def min: ULong = ???
    override def max: ULong = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: ULong): Int = {
      ???
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): ULong = {
      ???
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: ULong): Unit = {
      ???
    }
  }

  object QINT8 extends DataType[QByte](
    name = "QINT8",
    cValue = 11,
    byteSize = Some(1),
    protoType = DT_QINT8
  ) {
    override val priority: Int    = 30

    override def zero: QByte = ???
    override def one: QByte = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: QByte): Int = {
      ???
      // buffer.put(index, element)
      // byteSize
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): QByte = {
      ??? // buffer.get(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: QByte): Unit = {
      ???
    }
  }

  object QINT16 extends DataType[QShort](
    name = "QINT16",
    cValue = 15,
    byteSize = Some(2),
    protoType = DT_QINT16
  ) {
    override val priority: Int    = 70

    override def zero: QShort = ???
    override def one: QShort = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: QShort): Int = {
      ???
      // buffer.putShort(index, element)
      // byteSize
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): QShort = {
      ??? // buffer.getShort(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: QShort): Unit = {
      ???
    }
  }

  object QINT32 extends DataType[QInt](
    name = "QINT32",
    cValue = 13,
    byteSize = Some(4),
    protoType = DT_QINT32
  ) {
    override val priority: Int    = 90

    override def zero: QInt = ???
    override def one: QInt = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: QInt): Int = {
      ???
      // buffer.putInt(index, element)
      // byteSize
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): QInt = {
      ??? // buffer.getInt(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: QInt): Unit = {
      ???
    }
  }

  object QUINT8 extends DataType[QUByte](
    name = "QUINT8",
    cValue = 12,
    byteSize = Some(1),
    protoType = DT_QUINT8
  ) {
    override val priority: Int    = 10

    override def zero: QUByte = ???
    override def one: QUByte = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: QUByte): Int = {
      ???
      // buffer.put(index, element.toByte)
      // byteSize
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): QUByte = {
      ??? // UByte(buffer.get(index))
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: QUByte): Unit = {
      ???
    }
  }

  object QUINT16 extends DataType[QUShort](
    name = "QUINT16",
    cValue = 16,
    byteSize = Some(2),
    protoType = DT_QUINT16
  ) {
    override val priority: Int    = 50

    override def zero: QUShort = ???
    override def one: QUShort = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: QUShort): Int = {
      ???
      // buffer.putChar(index, element.toChar)
      // byteSize
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): QUShort = {
      ??? // UShort(buffer.getChar(index))
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: QUShort): Unit = {
      ???
    }
  }

  object RESOURCE extends DataType[Long](
    name = "RESOURCE",
    cValue = 20,
    byteSize = Some(1),
    protoType = DT_RESOURCE
  ) {
    override val priority: Int    = -1

    override def zero: Long = ???
    override def one: Long = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: Long): Int = {
      buffer.putLong(index, element)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): Long = {
      buffer.getLong(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: Long): Unit = {
      ???
    }
  }

  object VARIANT extends DataType[Long](
    name = "VARIANT",
    cValue = 21,
    byteSize = Some(1),
    protoType = DT_VARIANT
  ) {
    override val priority: Int    = -1

    override def zero: Long = ???
    override def one: Long = ???

    private[api] override def putElementInBuffer(buffer: ByteBuffer, index: Int, element: Long): Int = {
      buffer.putLong(index, element)
      byteSize.get
    }

    private[api] override def getElementFromBuffer(buffer: ByteBuffer, index: Int): Long = {
      buffer.getLong(index)
    }

    private[api] override def addToTensorProtoBuilder(tensorProtoBuilder: TensorProto.Builder, value: Long): Unit = {
      ???
    }
  }

  //region Type Traits

  // TODO: Complete/Generalize this.

  trait IsFloat32OrFloat64[T]

  object IsFloat32OrFloat64 {
    implicit val floatEvidence : IsFloat32OrFloat64[Float]  = new IsFloat32OrFloat64[Float] {}
    implicit val doubleEvidence: IsFloat32OrFloat64[Double] = new IsFloat32OrFloat64[Double] {}
  }

  trait IsFloat16OrFloat32OrFloat64[T]

  object IsFloat16OrFloat32OrFloat64 {
    implicit val halfEvidence  : IsFloat16OrFloat32OrFloat64[Half]   = new IsFloat16OrFloat32OrFloat64[Half] {}
    implicit val floatEvidence : IsFloat16OrFloat32OrFloat64[Float]  = new IsFloat16OrFloat32OrFloat64[Float] {}
    implicit val doubleEvidence: IsFloat16OrFloat32OrFloat64[Double] = new IsFloat16OrFloat32OrFloat64[Double] {}

    implicit def float32OrFloat64Evidence[T: IsFloat32OrFloat64]: IsFloat16OrFloat32OrFloat64[T] = new IsFloat16OrFloat32OrFloat64[T] {}
  }

  trait IsBFloat16OrFloat32OrFloat64[T]

  object IsBFloat16OrFloat32OrFloat64 {
    implicit val truncatedHalfEvidence: IsBFloat16OrFloat32OrFloat64[TruncatedHalf] = new IsBFloat16OrFloat32OrFloat64[TruncatedHalf] {}
    implicit val floatEvidence        : IsBFloat16OrFloat32OrFloat64[Float]         = new IsBFloat16OrFloat32OrFloat64[Float] {}
    implicit val doubleEvidence       : IsBFloat16OrFloat32OrFloat64[Double]        = new IsBFloat16OrFloat32OrFloat64[Double] {}

    implicit def float32OrFloat64Evidence[T: IsFloat32OrFloat64]: IsBFloat16OrFloat32OrFloat64[T] = new IsBFloat16OrFloat32OrFloat64[T] {}
  }

  trait IsBFloat16OrFloat16OrFloat32[T]

  object IsBFloat16OrFloat16OrFloat32 {
    implicit val truncatedHalfEvidence: IsBFloat16OrFloat16OrFloat32[TruncatedHalf] = new IsBFloat16OrFloat16OrFloat32[TruncatedHalf] {}
    implicit val halfEvidence         : IsBFloat16OrFloat16OrFloat32[Half]          = new IsBFloat16OrFloat16OrFloat32[Half] {}
    implicit val floatEvidence        : IsBFloat16OrFloat16OrFloat32[Float]         = new IsBFloat16OrFloat16OrFloat32[Float] {}
  }

  trait IsDecimal[T]

  object IsDecimal {
    implicit val halfEvidence         : IsDecimal[Half]          = new IsDecimal[Half] {}
    implicit val floatEvidence        : IsDecimal[Float]         = new IsDecimal[Float] {}
    implicit val doubleEvidence       : IsDecimal[Double]        = new IsDecimal[Double] {}
    implicit val truncatedHalfEvidence: IsDecimal[TruncatedHalf] = new IsDecimal[TruncatedHalf] {}

    implicit def float32OrFloat64Evidence[T: IsFloat32OrFloat64]: IsDecimal[T] = new IsDecimal[T] {}
    implicit def float16OrFloat32OrFloat64Evidence[T: IsFloat16OrFloat32OrFloat64]: IsDecimal[T] = new IsDecimal[T] {}
    implicit def bFloat16OrFloat32OrFloat64Evidence[T: IsBFloat16OrFloat32OrFloat64]: IsDecimal[T] = new IsDecimal[T] {}
    implicit def bFloat16OrFloat16OrFloat32Evidence[T: IsBFloat16OrFloat16OrFloat32]: IsDecimal[T] = new IsDecimal[T] {}
  }

  trait IsInt32OrInt64[T]

  object IsInt32OrInt64 extends IsInt32OrInt64LowPriority {
    implicit val intEvidence: IsInt32OrInt64[Int] = new IsInt32OrInt64[Int] {}
  }

  trait IsInt32OrInt64LowPriority {
    implicit val longEvidence: IsInt32OrInt64[Long] = new IsInt32OrInt64[Long] {}
  }

  trait IsInt32OrInt64OrFloat32OrFloat64[T]

  object IsInt32OrInt64OrFloat32OrFloat64 {
    implicit val floatEvidence : IsInt32OrInt64OrFloat32OrFloat64[Float]  = new IsInt32OrInt64OrFloat32OrFloat64[Float] {}
    implicit val doubleEvidence: IsInt32OrInt64OrFloat32OrFloat64[Double] = new IsInt32OrInt64OrFloat32OrFloat64[Double] {}
    implicit val intEvidence   : IsInt32OrInt64OrFloat32OrFloat64[Int]    = new IsInt32OrInt64OrFloat32OrFloat64[Int] {}
    implicit val longEvidence  : IsInt32OrInt64OrFloat32OrFloat64[Long]   = new IsInt32OrInt64OrFloat32OrFloat64[Long] {}

    implicit def float32OrFloat64Evidence[T: IsFloat32OrFloat64]: IsInt32OrInt64OrFloat32OrFloat64[T] = new IsInt32OrInt64OrFloat32OrFloat64[T] {}
    implicit def float16OrFloat32OrFloat64Evidence[T: IsFloat16OrFloat32OrFloat64]: IsInt32OrInt64OrFloat32OrFloat64[T] = new IsInt32OrInt64OrFloat32OrFloat64[T] {}
  }

  trait IsInt32OrInt64OrFloat16OrFloat32OrFloat64[T]

  object IsInt32OrInt64OrFloat16OrFloat32OrFloat64 {
    implicit val halfEvidence  : IsInt32OrInt64OrFloat16OrFloat32OrFloat64[Half]   = new IsInt32OrInt64OrFloat16OrFloat32OrFloat64[Half] {}
    implicit val floatEvidence : IsInt32OrInt64OrFloat16OrFloat32OrFloat64[Float]  = new IsInt32OrInt64OrFloat16OrFloat32OrFloat64[Float] {}
    implicit val doubleEvidence: IsInt32OrInt64OrFloat16OrFloat32OrFloat64[Double] = new IsInt32OrInt64OrFloat16OrFloat32OrFloat64[Double] {}
    implicit val intEvidence   : IsInt32OrInt64OrFloat16OrFloat32OrFloat64[Int]    = new IsInt32OrInt64OrFloat16OrFloat32OrFloat64[Int] {}
    implicit val longEvidence  : IsInt32OrInt64OrFloat16OrFloat32OrFloat64[Long]   = new IsInt32OrInt64OrFloat16OrFloat32OrFloat64[Long] {}

    implicit def float32OrFloat64Evidence[T: IsFloat32OrFloat64]: IsInt32OrInt64OrFloat16OrFloat32OrFloat64[T] = new IsInt32OrInt64OrFloat16OrFloat32OrFloat64[T] {}
    implicit def float16OrFloat32OrFloat64Evidence[T: IsFloat16OrFloat32OrFloat64]: IsInt32OrInt64OrFloat16OrFloat32OrFloat64[T] = new IsInt32OrInt64OrFloat16OrFloat32OrFloat64[T] {}
  }

  trait IsInt32OrInt64OrUInt8[T]

  object IsInt32OrInt64OrUInt8 {
    implicit val uByteEvidence: IsInt32OrInt64OrUInt8[UByte] = new IsInt32OrInt64OrUInt8[UByte] {}
    implicit val intEvidence  : IsInt32OrInt64OrUInt8[Int]   = new IsInt32OrInt64OrUInt8[Int] {}
    implicit val longEvidence : IsInt32OrInt64OrUInt8[Long]  = new IsInt32OrInt64OrUInt8[Long] {}
  }

  trait IsIntOrUInt[T]

  object IsIntOrUInt {
    implicit val byteEvidence  : IsIntOrUInt[Byte]   = new IsIntOrUInt[Byte] {}
    implicit val shortEvidence : IsIntOrUInt[Short]  = new IsIntOrUInt[Short] {}
    implicit val intEvidence   : IsIntOrUInt[Int]    = new IsIntOrUInt[Int] {}
    implicit val longEvidence  : IsIntOrUInt[Long]   = new IsIntOrUInt[Long] {}
    implicit val uByteEvidence : IsIntOrUInt[UByte]  = new IsIntOrUInt[UByte] {}
    implicit val uShortEvidence: IsIntOrUInt[UShort] = new IsIntOrUInt[UShort] {}
    implicit val uIntEvidence  : IsIntOrUInt[UInt]   = new IsIntOrUInt[UInt] {}
    implicit val uLongEvidence : IsIntOrUInt[ULong]  = new IsIntOrUInt[ULong] {}
  }

  trait IsReal[T]

  object IsReal {
    implicit def decimalEvidence[T: IsDecimal]: IsReal[T] = new IsReal[T] {}
    implicit def intOrUIntEvidence[T: IsIntOrUInt]: IsReal[T] = new IsReal[T] {}
  }

  trait IsComplex[T]

  object IsComplex {
    implicit val complexFloatEvidence : IsComplex[ComplexFloat]  = new IsComplex[ComplexFloat] {}
    implicit val complexDoubleEvidence: IsComplex[ComplexDouble] = new IsComplex[ComplexDouble] {}
  }

  trait IsNotQuantized[T]

  object IsNotQuantized {
    implicit def float32OrFloat64Evidence[T: IsFloat32OrFloat64]: IsNotQuantized[T] = new IsNotQuantized[T] {}

    implicit def realEvidence[T: IsReal]: IsNotQuantized[T] = new IsNotQuantized[T] {}
    implicit def complexEvidence[T: IsComplex]: IsNotQuantized[T] = new IsNotQuantized[T] {}
  }

  trait IsQuantized[T]

  object IsQuantized {
    implicit val qByteEvidence  : IsQuantized[QByte]   = new IsQuantized[QByte] {}
    implicit val qShortEvidence : IsQuantized[QShort]  = new IsQuantized[QShort] {}
    implicit val qIntEvidence   : IsQuantized[QInt]    = new IsQuantized[QInt] {}
    implicit val qUByteEvidence : IsQuantized[QUByte]  = new IsQuantized[QUByte] {}
    implicit val qUShortEvidence: IsQuantized[QUShort] = new IsQuantized[QUShort] {}
  }

  trait IsNumeric[T]

  object IsNumeric {
    implicit def notQuantizedEvidence[T: IsNotQuantized]: IsNumeric[T] = new IsNumeric[T] {}
    implicit def quantizedEvidence[T: IsQuantized]: IsNumeric[T] = new IsNumeric[T] {}
  }

  //endregion Type Traits
}
