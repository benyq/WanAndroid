package com.benyq.wanandroid

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.regex.Pattern

/**
 *
 * @author benyq
 * @date 12/15/2023
 *
 */
private fun removeHtmlTag(content: String): String {
    val regEx = "(<em class='highlight'>|</em>)"
    return content.replace(Regex(regEx), "")
}

class RemoveHtmlTagTest {
    @Test
    fun `removeHtmlTag should remove regex from the content`() {
        // Arrange
        val content = "<em class='highlight'>Test</em> Content</em>"
        val expected = "Test Content"

        // Act
        val result = removeHtmlTag(content)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `removeHtmlTag should not remove any content if no regex is present`() {
        // Arrange
        val content = "Test Content"
        val expected = "Test Content"

        // Act
        val result = removeHtmlTag(content)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `removeHtmlTag should remove multiple regex from the content`() {
        // Arrange
        val content = "<em class='highlight'>Test</em> <em class='highlight'>Content</em></em>"
        val expected = "Test Content"

        // Act
        val result = removeHtmlTag(content)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `removeHtmlTag should not remove any content if regex1 is present but no regex2`() {
        // Arrange
        val content = "<em class='highlight'>Test"
        val expected = "Test"

        // Act
        val result = removeHtmlTag(content)

        // Assert
        assertEquals(expected, result)
    }
}
