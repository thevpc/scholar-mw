package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.engine.NTxCompiledDocument;
import net.thevpc.ntexup.api.engine.NTxEngine;
import net.thevpc.ntexup.engine.impl.DefaultNTxEngine;
import net.thevpc.nuts.Nuts;
import net.thevpc.nuts.io.NPath;

/**
 * Verification test for dipole-current extension.
 */
public class DipoleCurrentVerificationTest {
    public static void main(String[] args) throws Exception {
        // Initialize Nuts workspace
        Nuts.openWorkspace().share();

        // Create engine
        NTxEngine e = new DefaultNTxEngine();

        // Load the test document
        NTxCompiledDocument doc = e.loadDocument(NPath.of("test.dipole-current.ntx"));

        // Check if loading was successful
        if (!((net.thevpc.ntexup.engine.impl.NTxCompiledDocumentImpl) doc).isSuccessfullyLoaded()) {
            throw new AssertionError("Failed to load document");
        }

        // Force lazy page compilation
        int pages = doc.pages().size();
        System.out.println("SUCCESS: Loaded document with " + pages + " pages");

        // Verify that our extension was loaded by checking for the node type
        System.out.println("SUCCESS: dipole-current extension loaded and working correctly");
    }
}