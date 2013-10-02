/*
 * #%L
 * Checkstyle OSGi Checks
 * %%
 * Copyright (C) 2013 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
