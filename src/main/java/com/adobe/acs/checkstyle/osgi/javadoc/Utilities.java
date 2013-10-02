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

import java.util.Arrays;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

class Utilities {
    
    private Utilities() {}

    static boolean isExportedPackage(DetailAST aAST) {
        DetailAST packageAST = findPackage(aAST);

        if (packageAST == null) {
            System.err.println("No package available for " + aAST);
            return true;
        }

        String packageName = getPackageName(packageAST);
        String[] parts = packageName.split("\\.");
        Arrays.sort(parts);

        boolean exported = Arrays.binarySearch(parts, "impl") < 0 && Arrays.binarySearch(parts, "internal") < 0;

        return exported;
    }

    private static String getPackageName(DetailAST packageAST) {
        final DetailAST nameAST = packageAST.getLastChild().getPreviousSibling();
        final FullIdent full = FullIdent.createFullIdent(nameAST);
        return full.getText();
    }

    private static DetailAST findPackage(DetailAST aAST) {
        if (aAST == null) {
            return null;
        }
        
        switch (aAST.getType()) {
        case TokenTypes.PACKAGE_DEF:
            return aAST;
            
        case TokenTypes.CLASS_DEF:
        case TokenTypes.INTERFACE_DEF:
        case TokenTypes.IMPORT:
        case TokenTypes.STATIC_IMPORT:
            return findPackage(aAST.getPreviousSibling());
            
        default:
            return findPackage(aAST.getParent());
        }
    }

}
