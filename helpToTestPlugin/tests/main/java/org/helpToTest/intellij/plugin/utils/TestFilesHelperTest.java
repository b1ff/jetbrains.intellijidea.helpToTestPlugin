package main.java.org.helpToTest.intellij.plugin.utils;

import junit.framework.TestCase;

public class TestFilesHelperTest extends TestCase {

    public void testGetExtensionsCorrectlyReturns() {
        assertEquals("js", TestFilesHelper.getExtension("my.js"));
        assertEquals("js", TestFilesHelper.getExtension("my.some.prefix.js"));
        assertEquals("coffee", TestFilesHelper.getExtension("my.coffee"));
    }
}