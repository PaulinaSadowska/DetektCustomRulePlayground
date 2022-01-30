package com.github.paulinasadowska.detektcustomruleplayground

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class RequiresVersion(val version: String)

class Foo {

    fun b() {
        Bar().a()
    }
}

class Bar {

    @RequiresVersion("1.2.0")
    fun a() = "I'm not ready yet!"
}
