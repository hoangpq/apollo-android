package com.apollostack.compiler

import com.apollostack.compiler.ir.FragmentTypeSpecBuilder
import com.apollostack.compiler.ir.QueryIntermediateRepresentation
import com.squareup.javapoet.JavaFile
import com.squareup.moshi.Moshi
import java.io.File

open class GraphQLCompiler {
  private val moshi = Moshi.Builder().build()
  private val irAdapter = moshi.adapter(QueryIntermediateRepresentation::class.java)

  fun write(irFile: File, outputDir: File, generateClasses: Boolean = false) {
    val packageName = irFile.absolutePath.formatPackageName()
    val ir = irAdapter.fromJson(irFile.readText())
    val operationTypeBuilders = ir.operations.map { OperationTypeSpecBuilder(it, ir.fragments, generateClasses) }
    val fragmentTypeBuilders = ir.fragments.map { FragmentTypeSpecBuilder(it, generateClasses) }
    (operationTypeBuilders + fragmentTypeBuilders + ir.typesUsed).forEach {
      JavaFile.builder(packageName, it.toTypeSpec()).build().writeTo(outputDir)
    }
  }

  companion object {
    const val FILE_EXTENSION = "graphql"
    val OUTPUT_DIRECTORY = listOf("generated", "source", "apollo")
  }

  private fun String.formatPackageName(): String {
    val parts = split(File.separatorChar)
    (parts.size - 1 downTo 2)
        .filter { parts[it - 2] == "src" && parts[it] == "graphql" }
        .forEach { return parts.subList(it + 1, parts.size).dropLast(1).joinToString(".") }
    throw IllegalArgumentException("Files must be organized like src/main/graphql/...")
  }
}
