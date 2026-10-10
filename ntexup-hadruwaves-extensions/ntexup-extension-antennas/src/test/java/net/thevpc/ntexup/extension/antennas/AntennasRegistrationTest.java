package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.engine.NTxCompiledDocument;
import net.thevpc.ntexup.api.engine.NTxEngine;
import net.thevpc.ntexup.engine.impl.DefaultNTxEngine;
import net.thevpc.ntexup.engine.impl.NTxCompiledDocumentImpl;
import net.thevpc.nuts.Nuts;
import net.thevpc.nuts.io.NPath;

import java.io.File;

public class AntennasRegistrationTest {
    public static void main(String[] args) throws Exception {
        Nuts.openWorkspace().share();
        NTxEngine e = new DefaultNTxEngine();
        File f = new File("test.antennas-components.ntx");
        if (!f.exists()) {
            throw new AssertionError("Test deck not found: " + f.getAbsolutePath());
        }
        NTxCompiledDocument doc = e.loadDocument(NPath.of(f.toPath()));
        if (!((NTxCompiledDocumentImpl) doc).isSuccessfullyLoaded()) {
            throw new AssertionError("Failed to load document");
        }
        int pages = doc.pages().size();
        System.out.println("SUCCESS: Loaded document with " + pages + " pages");
    }
}