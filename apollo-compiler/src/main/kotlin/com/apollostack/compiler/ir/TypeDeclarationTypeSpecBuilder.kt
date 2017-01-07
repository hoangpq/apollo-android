package com.apollostack.compiler.ir

import com.squareup.javapoet.TypeSpec

class TypeDeclarationTypeSpecBuilder(
    val typeDeclaration: TypeDeclaration,
    val packageName: String
) : PackageAwareGenerator {
  override fun packageName(): String {
    return packageName
  }

  override fun toTypeSpec(pkgName: String): TypeSpec = typeDeclaration.toTypeSpec()
}
