package org.example.detekt

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


@RequiresOptIn(message = "This API is experimental. It may be changed in the future without notice.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class MyDateTime // Opt-in requirement annotation
