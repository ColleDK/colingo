package com.colledk.accessibility

import org.junit.Rule
import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

open class BaseAccessibilityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    open fun testAccessibility() {
        testContentDescription()
        testHasStateDescriptions()
        testOnClickLabels()
        testClickableSizes()
        testInputFieldHints()
        testInputFieldImeAction()
    }

    open fun testContentDescription() {
        val children = composeTestRule.onRoot(true).onChildren()
        val nodes = children.fetchSemanticsNodes().map { it.getAllChildren() }.flatten()

        nodes.assertAll(hasSetImageDescription())
    }

    open fun testHasStateDescriptions() {
        composeTestRule.onAllNodes(isSelectable().or(isToggleable())).assertAll(hasSetStateDescription())
    }

    open fun testOnClickLabels() {
        composeTestRule.onAllNodes(hasClickAction().and(!hasSetTextAction())).assertAll(hasOnClickLabel())
    }

    open fun testClickableSizes() {
        composeTestRule.onAllNodes(hasClickAction()).assertAll(hasMinimumClickSize())
    }

    open fun testInputFieldHints() {
        composeTestRule.onAllNodes(hasSetTextAction()).assertAll(hasInputFieldHints())
    }

    open fun testInputFieldImeAction() {
        composeTestRule.onAllNodes(hasSetTextAction()).assertAll(hasInputFieldImeAction())
    }

    /**
     * Get a list of all nodes inside the given [SemanticsNode]
     */
    fun SemanticsNode.getAllChildren(): List<SemanticsNode> {
        return if (this.children.isNotEmpty()) {
            this.children.map {
                it.getAllChildren()
            }.flatten()
        } else {
            listOf(this)
        }
    }

    /**
     * Assert all [SemanticsNode] to match a given criteria
     */
    fun List<SemanticsNode>.assertAll(
        matcher: SemanticsMatcher
    ): List<SemanticsNode> {
        val violations = mutableListOf<SemanticsNode>()
        this.forEach {
            if (!matcher.matches(it)) {
                violations.add(it)
            }
        }

        if (violations.isNotEmpty()) {
            throw AssertionError()
        }
        return this
    }

    /**
     * Function that checks if images have a set content description
     */
    fun hasSetImageDescription(): SemanticsMatcher {
        return SemanticsMatcher(
            description = "Has image description test"
        ) {
            when(it.config.getOrNull(SemanticsProperties.Role)) {
                Role.Image -> {
                    it.config.getOrNull(SemanticsProperties.ContentDescription)?.all { item ->
                        item.isNotEmpty()
                    } ?: false
                }
                else -> {
                    true
                }
            }
        }
    }

    /**
     * Function that checks if the state description is set on the [SemanticsNode]
     */
    fun hasSetStateDescription(): SemanticsMatcher {
        return SemanticsMatcher(
            description = SemanticsProperties.StateDescription.name
        ) {
            it.config.getOrNull(SemanticsProperties.StateDescription)?.isNotEmpty() ?: false
        }
    }

    /**
     * Function that checks if clickable targets is at least 48.dp
     * @return returns false if the size of the clickable component is less than 48.dp
     */
    fun hasMinimumClickSize(): SemanticsMatcher {
        return SemanticsMatcher(
            description = "Has minimum click size test"
        ) {
            with(composeTestRule.density) {
                it.size.width.toDp() >= 48.dp && it.size.height.toDp() >= 48.dp
            }
        }
    }

    /**
     * Function that checks if clickable targets contain a talkbalk label.
     * @return returns false if the clickable component does not contain any label.
     */
    fun hasOnClickLabel(): SemanticsMatcher {
        return SemanticsMatcher(
            description = SemanticsActions.OnClick.name
        ) {
            it.config.getOrNull(SemanticsActions.OnClick)?.label?.isNotEmpty() ?: false
        }
    }

    /**
     * Function that checks if input fields contain any hint or label.
     * @return returns false if the input field contains no hint or label texts.
     */
    fun hasInputFieldHints(): SemanticsMatcher {
        return SemanticsMatcher(
            description = SemanticsActions.SetText.name
        ) {
            (it.config.getOrNull(SemanticsProperties.Text)?.size ?: 0 ) > 0
        }
    }

    /**
     * Function that checks if input fields contains any IME actions
     * @return returns false if the input field contains no IME action or the action is [ImeAction.None]
     */
    fun hasInputFieldImeAction(): SemanticsMatcher {
        return SemanticsMatcher(
            description = SemanticsProperties.ImeAction.name
        ) {
            it.config.getOrNull(SemanticsProperties.ImeAction)?.let { ime ->
                ime != ImeAction.None
            } ?: false
        }
    }
}