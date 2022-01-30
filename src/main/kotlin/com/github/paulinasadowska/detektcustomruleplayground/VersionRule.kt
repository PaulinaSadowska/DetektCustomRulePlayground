package org.example.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getCallNameExpression
import org.jetbrains.kotlin.util.isAnnotated

class VersionRule(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Defect,
        "Versioning issue",
        Debt.FIVE_MINS,
    )

    private val annotatedClasses = mutableSetOf<String>()

    override fun preVisit(root: KtFile) {
        annotatedClasses.clear()
        //annotatedClasses.add("a")
    }

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        expression.getCallNameExpression()?.let { expressionName ->
            val match: Boolean = annotatedClasses.map {
                expressionName.textMatches(it)
            }.any { it }
            if (match) {
                report(
                    CodeSmell(issue, Entity.from(expression), message = "FOUND ${expressionName.text}")
                )
            }
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (function.isAnnotated) {
            function.annotationEntries.forEach { annotation ->
                if (annotation.textMatches("""@RequiresVersion("1.2.0")""")) {
                    function.name?.let { annotatedClasses.add(it) }
                }
            }
        }
    }
}
