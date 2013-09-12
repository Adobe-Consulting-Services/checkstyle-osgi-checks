package com.adobe.acs.checkstyle.osgi.javadoc;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocMethodCheck;

/**
 * Checkstyle check which performs the standard method check, but will
 * permit a missing javadoc comment on non-exported packages.
 * 
 * @author Justin Edelson
 */
public class ExportedJavadocMethodCheck extends JavadocMethodCheck {

    @Override
    protected boolean isMissingJavadocAllowed(DetailAST aAST) {
        if (!super.isMissingJavadocAllowed(aAST)) {
            return !Utilities.isExportedPackage(aAST);
        } else {
            return true;
        }
    }

}
