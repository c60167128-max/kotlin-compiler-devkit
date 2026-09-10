package org.jetbrains.kotlin.test.helper.listeners

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NewDiagnosticsTestDataFileListenerTest {
    @Test
    fun `paths inside the diagnostics test data directory`() {
        assertTrue("/home/u/kotlin/compiler/testData/diagnostics/tests/Foo.kt".isInDiagnosticsTestData())
        assertTrue("/home/u/kotlin/compiler/testData/diagnostics/testsWithStdlib/Foo.kt".isInDiagnosticsTestData())
        assertTrue("/home/u/kotlin/compiler/testData/diagnostics/tests/inference/nested/Foo.kt".isInDiagnosticsTestData())
    }

    @Test
    fun `paths outside the diagnostics test data directory`() {
        assertFalse("/home/u/kotlin/compiler/testData/diagnostics/Foo.kt".isInDiagnosticsTestData())
        assertFalse("/home/u/kotlin/compiler/testData/diagnostics".isInDiagnosticsTestData())
        assertFalse("/home/u/kotlin/compiler/testData/diagnostics/klibInlinerTests/Foo.kt".isInDiagnosticsTestData())
        assertFalse("/home/u/kotlin/compiler/testData/codegen/box/Foo.kt".isInDiagnosticsTestData())
        assertFalse("/home/u/kotlin/compiler/testData/diagnosticsExtra/Foo.kt".isInDiagnosticsTestData())
        assertFalse("/home/u/kotlin/compiler/testData/diagnostics-old/Foo.kt".isInDiagnosticsTestData())
        assertFalse("/home/u/kotlin/compiler/testData/Foo.kt".isInDiagnosticsTestData())
        assertFalse("/home/u/kotlin/plugins/foo/testData/diagnostics/Foo.kt".isInDiagnosticsTestData())
        assertFalse("".isInDiagnosticsTestData())
    }
}
