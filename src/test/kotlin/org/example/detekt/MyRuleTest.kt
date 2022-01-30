package org.example.detekt

import com.google.common.truth.Truth.assertThat
import io.github.detekt.test.utils.KotlinCoreEnvironmentWrapper
import io.github.detekt.test.utils.createEnvironment
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

internal class CustomRuleSpec {

    @Test
    fun `reports versioning`() {
        val code = """
        class Bar {
            @RequiresVersion("1.2.0")
            fun a() : String {
                "I'm not ready yet!"
            }
        }

        class Foo {
            private val bar = Bar()
            fun b() {
                println(bar.a())
            }
        }
        """
        val findings = VersionRule(Config.empty).compileAndLintWithContext(env, code)
        assertThat(findings.map { it.message }).isEqualTo(listOf("FOUND a"))
    }

    @Test
    fun `reports versioning - different order`() {
        val code = """
        class Foo {
            private val bar = Bar()
            fun b() {
                println(bar.a())
            }
        }

        class Bar {
            @RequiresVersion("1.2.0")
            fun a() : String {
                "I'm not ready yet!"
            }
        }
        """
        val findings = VersionRule(Config.empty).compileAndLintWithContext(env, code)
        assertThat(findings.map { it.message }).isEqualTo(listOf("FOUND a"))
    }

    private val env: KotlinCoreEnvironment
        get() = envWrapper.env

    companion object {
        private lateinit var envWrapper: KotlinCoreEnvironmentWrapper

        @BeforeClass
        @JvmStatic
        fun setUp() {
            envWrapper = createEnvironment()
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            envWrapper.dispose()
        }
    }
}
