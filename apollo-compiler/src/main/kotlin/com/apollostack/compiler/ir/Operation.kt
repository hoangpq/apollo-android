package com.apollostack.compiler.ir

import com.apollostack.api.graphql.Operation
import com.apollostack.compiler.SchemaTypeSpecBuilder
import com.apollostack.compiler.resolveNestedTypeNameDuplication
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec

data class Operation(
    val operationName: String,
    val operationType: String,
    val variables: List<Variable>,
    val source: String,
    val fields: List<Field>,
    val path: String
) : CodeGenerator {
  override fun toTypeSpec(pkgName: String): TypeSpec =
    SchemaTypeSpecBuilder()
        .build(INTERFACE_TYPE_SPEC_NAME, fields, emptyList(), emptyList(), pkgName)
        .toBuilder()
        .addSuperinterface(ClassName.get(Operation.Data::class.java))
        .build()
        .resolveNestedTypeNameDuplication(emptyList())

  companion object {
    val INTERFACE_TYPE_SPEC_NAME = "Data"
  }
}
