// automatically generated by the FlatBuffers compiler, do not modify


#ifndef FLATBUFFERS_GENERATED_ENUMS_DEXKIT_SCHEMA_H_
#define FLATBUFFERS_GENERATED_ENUMS_DEXKIT_SCHEMA_H_

#include "flatbuffers/flatbuffers.h"

// Ensure the included flatbuffers.h is the same version as when this file was
// generated, otherwise it may not be compatible.
static_assert(FLATBUFFERS_VERSION_MAJOR == 23 &&
              FLATBUFFERS_VERSION_MINOR == 5 &&
              FLATBUFFERS_VERSION_REVISION == 26,
             "Non-compatible flatbuffers version included");

namespace dexkit {
namespace schema {

enum class StringMatchType : int8_t {
  Contains = 0,
  StartWith = 1,
  EndWith = 2,
  SimilarRegex = 3,
  Equal = 4
};

inline const StringMatchType (&EnumValuesStringMatchType())[5] {
  static const StringMatchType values[] = {
    StringMatchType::Contains,
    StringMatchType::StartWith,
    StringMatchType::EndWith,
    StringMatchType::SimilarRegex,
    StringMatchType::Equal
  };
  return values;
}

inline const char * const *EnumNamesStringMatchType() {
  static const char * const names[6] = {
    "Contains",
    "StartWith",
    "EndWith",
    "SimilarRegex",
    "Equal",
    nullptr
  };
  return names;
}

inline const char *EnumNameStringMatchType(StringMatchType e) {
  if (::flatbuffers::IsOutRange(e, StringMatchType::Contains, StringMatchType::Equal)) return "";
  const size_t index = static_cast<size_t>(e);
  return EnumNamesStringMatchType()[index];
}

enum class OpCodeMatchType : int8_t {
  Contains = 0,
  StartWith = 1,
  EndWith = 2,
  Equal = 3
};

inline const OpCodeMatchType (&EnumValuesOpCodeMatchType())[4] {
  static const OpCodeMatchType values[] = {
    OpCodeMatchType::Contains,
    OpCodeMatchType::StartWith,
    OpCodeMatchType::EndWith,
    OpCodeMatchType::Equal
  };
  return values;
}

inline const char * const *EnumNamesOpCodeMatchType() {
  static const char * const names[5] = {
    "Contains",
    "StartWith",
    "EndWith",
    "Equal",
    nullptr
  };
  return names;
}

inline const char *EnumNameOpCodeMatchType(OpCodeMatchType e) {
  if (::flatbuffers::IsOutRange(e, OpCodeMatchType::Contains, OpCodeMatchType::Equal)) return "";
  const size_t index = static_cast<size_t>(e);
  return EnumNamesOpCodeMatchType()[index];
}

enum class UsingType : int8_t {
  Any = 0,
  Get = 1,
  Put = 2
};

inline const UsingType (&EnumValuesUsingType())[3] {
  static const UsingType values[] = {
    UsingType::Any,
    UsingType::Get,
    UsingType::Put
  };
  return values;
}

inline const char * const *EnumNamesUsingType() {
  static const char * const names[4] = {
    "Any",
    "Get",
    "Put",
    nullptr
  };
  return names;
}

inline const char *EnumNameUsingType(UsingType e) {
  if (::flatbuffers::IsOutRange(e, UsingType::Any, UsingType::Put)) return "";
  const size_t index = static_cast<size_t>(e);
  return EnumNamesUsingType()[index];
}

enum class MatchType : int8_t {
  Contains = 0,
  Equal = 1
};

inline const MatchType (&EnumValuesMatchType())[2] {
  static const MatchType values[] = {
    MatchType::Contains,
    MatchType::Equal
  };
  return values;
}

inline const char * const *EnumNamesMatchType() {
  static const char * const names[3] = {
    "Contains",
    "Equal",
    nullptr
  };
  return names;
}

inline const char *EnumNameMatchType(MatchType e) {
  if (::flatbuffers::IsOutRange(e, MatchType::Contains, MatchType::Equal)) return "";
  const size_t index = static_cast<size_t>(e);
  return EnumNamesMatchType()[index];
}

enum class TargetElementType : int8_t {
  Type = 0,
  Field = 1,
  Method = 2,
  Parameter = 3,
  Constructor = 4,
  LocalVariable = 5,
  AnnotationType = 6,
  Package = 7,
  TypeParameter = 8,
  TypeUse = 9
};

inline const TargetElementType (&EnumValuesTargetElementType())[10] {
  static const TargetElementType values[] = {
    TargetElementType::Type,
    TargetElementType::Field,
    TargetElementType::Method,
    TargetElementType::Parameter,
    TargetElementType::Constructor,
    TargetElementType::LocalVariable,
    TargetElementType::AnnotationType,
    TargetElementType::Package,
    TargetElementType::TypeParameter,
    TargetElementType::TypeUse
  };
  return values;
}

inline const char * const *EnumNamesTargetElementType() {
  static const char * const names[11] = {
    "Type",
    "Field",
    "Method",
    "Parameter",
    "Constructor",
    "LocalVariable",
    "AnnotationType",
    "Package",
    "TypeParameter",
    "TypeUse",
    nullptr
  };
  return names;
}

inline const char *EnumNameTargetElementType(TargetElementType e) {
  if (::flatbuffers::IsOutRange(e, TargetElementType::Type, TargetElementType::TypeUse)) return "";
  const size_t index = static_cast<size_t>(e);
  return EnumNamesTargetElementType()[index];
}

enum class RetentionPolicyType : int8_t {
  Any = 0,
  Source = 1,
  Class = 2,
  Runtime = 3
};

inline const RetentionPolicyType (&EnumValuesRetentionPolicyType())[4] {
  static const RetentionPolicyType values[] = {
    RetentionPolicyType::Any,
    RetentionPolicyType::Source,
    RetentionPolicyType::Class,
    RetentionPolicyType::Runtime
  };
  return values;
}

inline const char * const *EnumNamesRetentionPolicyType() {
  static const char * const names[5] = {
    "Any",
    "Source",
    "Class",
    "Runtime",
    nullptr
  };
  return names;
}

inline const char *EnumNameRetentionPolicyType(RetentionPolicyType e) {
  if (::flatbuffers::IsOutRange(e, RetentionPolicyType::Any, RetentionPolicyType::Runtime)) return "";
  const size_t index = static_cast<size_t>(e);
  return EnumNamesRetentionPolicyType()[index];
}

enum class AnnotationVisibilityType : int8_t {
  Build = 0,
  Runtime = 1,
  System = 2,
  None = 3
};

inline const AnnotationVisibilityType (&EnumValuesAnnotationVisibilityType())[4] {
  static const AnnotationVisibilityType values[] = {
    AnnotationVisibilityType::Build,
    AnnotationVisibilityType::Runtime,
    AnnotationVisibilityType::System,
    AnnotationVisibilityType::None
  };
  return values;
}

inline const char * const *EnumNamesAnnotationVisibilityType() {
  static const char * const names[5] = {
    "Build",
    "Runtime",
    "System",
    "None",
    nullptr
  };
  return names;
}

inline const char *EnumNameAnnotationVisibilityType(AnnotationVisibilityType e) {
  if (::flatbuffers::IsOutRange(e, AnnotationVisibilityType::Build, AnnotationVisibilityType::None)) return "";
  const size_t index = static_cast<size_t>(e);
  return EnumNamesAnnotationVisibilityType()[index];
}

enum class AnnotationEncodeValueType : int8_t {
  ByteValue = 0,
  ShortValue = 1,
  CharValue = 2,
  IntValue = 3,
  LongValue = 4,
  FloatValue = 5,
  DoubleValue = 6,
  StringValue = 7,
  TypeValue = 8,
  MethodValue = 9,
  EnumValue = 10,
  ArrayValue = 11,
  AnnotationValue = 12,
  NullValue = 13,
  BoolValue = 14
};

inline const AnnotationEncodeValueType (&EnumValuesAnnotationEncodeValueType())[15] {
  static const AnnotationEncodeValueType values[] = {
    AnnotationEncodeValueType::ByteValue,
    AnnotationEncodeValueType::ShortValue,
    AnnotationEncodeValueType::CharValue,
    AnnotationEncodeValueType::IntValue,
    AnnotationEncodeValueType::LongValue,
    AnnotationEncodeValueType::FloatValue,
    AnnotationEncodeValueType::DoubleValue,
    AnnotationEncodeValueType::StringValue,
    AnnotationEncodeValueType::TypeValue,
    AnnotationEncodeValueType::MethodValue,
    AnnotationEncodeValueType::EnumValue,
    AnnotationEncodeValueType::ArrayValue,
    AnnotationEncodeValueType::AnnotationValue,
    AnnotationEncodeValueType::NullValue,
    AnnotationEncodeValueType::BoolValue
  };
  return values;
}

inline const char * const *EnumNamesAnnotationEncodeValueType() {
  static const char * const names[16] = {
    "ByteValue",
    "ShortValue",
    "CharValue",
    "IntValue",
    "LongValue",
    "FloatValue",
    "DoubleValue",
    "StringValue",
    "TypeValue",
    "MethodValue",
    "EnumValue",
    "ArrayValue",
    "AnnotationValue",
    "NullValue",
    "BoolValue",
    nullptr
  };
  return names;
}

inline const char *EnumNameAnnotationEncodeValueType(AnnotationEncodeValueType e) {
  if (::flatbuffers::IsOutRange(e, AnnotationEncodeValueType::ByteValue, AnnotationEncodeValueType::BoolValue)) return "";
  const size_t index = static_cast<size_t>(e);
  return EnumNamesAnnotationEncodeValueType()[index];
}

}  // namespace schema
}  // namespace dexkit

#endif  // FLATBUFFERS_GENERATED_ENUMS_DEXKIT_SCHEMA_H_