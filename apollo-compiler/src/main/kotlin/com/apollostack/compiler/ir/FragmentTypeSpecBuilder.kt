package com.apollostack.compiler.ir

import com.apollostack.compiler.convertToPOJO
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class FragmentTypeSpecBuilder(
    val fragment: Fragment,
    val generateClasses: Boolean,
    val packageName: String
) : PackageAwareGenerator {
  override fun packageName(): String {
    return packageName
  }

  override fun toTypeSpec(pkg: String): TypeSpec = fragment.toTypeSpec(pkg).let {
    if (generateClasses) it.convertToPOJO(Modifier.PUBLIC) else it
  }
}
