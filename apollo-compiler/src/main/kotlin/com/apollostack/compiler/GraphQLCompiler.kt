package com.apollostack.compiler

import com.apollostack.compiler.ir.FragmentTypeSpecBuilder
import com.apollostack.compiler.ir.OperationIntermediateRepresentation
import com.apollostack.compiler.ir.TypeDeclarationTypeSpecBuilder
import com.squareup.javapoet.JavaFile
import com.squareup.moshi.Moshi
import java.io.File

open class GraphQLCompiler {
  private val moshi = Moshi.Builder().build()
  private val irAdapter = moshi.adapter(OperationIntermediateRepresentation::class.java)

  fun write(irFile: File, outputDir: File, generateClasses: Boolean = false) {
    val ir = irAdapter.fromJson(irFile.readText())
    val operationTypeBuilders = ir.operations.map { OperationTypeSpecBuilder(it, ir.fragments, generateClasses) }
    val fragmentTypeBuilders = ir.fragments.map { FragmentTypeSpecBuilder(it, generateClasses,
        "${irFile.absolutePath.formatPackageName()}.fragment") }
    val typeDeclarationTypeBuilders = ir.typesUsed.map { TypeDeclarationTypeSpecBuilder(it,
        "${irFile.absolutePath.formatPackageName()}.type") }

    (operationTypeBuilders + fragmentTypeBuilders + typeDeclarationTypeBuilders).forEach {
      JavaFile.builder(it.packageName(), it.toTypeSpec(it.packageName())).build().writeTo(outputDir)
    }
  }

  companion object {
    const val FILE_EXTENSION = "graphql"
    val OUTPUT_DIRECTORY = listOf("generated", "source", "apollo")
  }
}
