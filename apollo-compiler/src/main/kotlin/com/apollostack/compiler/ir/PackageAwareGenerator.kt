package com.apollostack.compiler.ir

interface PackageAwareGenerator : CodeGenerator {
  fun packageName(): String
}
