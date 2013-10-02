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

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.api.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Checks that a variable has Javadoc comment. Will permit a missing javadoc comment
 * on non-exported packages.
 * 
 * This is essentially a copy of Checkstyle's JavadocVariableCheck as that class
 * is not designed for extension.
 *
 * @author Justin Edelson
 * @author Oliver Burn (original)
 */
public class ExportedJavadocVariableCheck extends Check {
    /** the scope to check */
    private Scope mScope = Scope.PRIVATE;

    /** the visibility scope where Javadoc comments shouldn't be checked **/
    private Scope mExcludeScope;

    /**
     * Sets the scope to check.
     * 
     * @param aFrom
     *            string to get the scope from
     */
    public void setScope(String aFrom) {
        mScope = Scope.getInstance(aFrom);
    }

    /**
     * Set the excludeScope.
     * 
     * @param aScope
     *            a <code>String</code> value
     */
    public void setExcludeScope(String aScope) {
        mExcludeScope = Scope.getInstance(aScope);
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[] { TokenTypes.VARIABLE_DEF, TokenTypes.ENUM_CONSTANT_DEF, };
    }

    @Override
    public void visitToken(DetailAST aAST) {
        if (shouldCheck(aAST)) {
            final FileContents contents = getFileContents();
            final TextBlock cmt = contents.getJavadocBefore(aAST.getLineNo());

            if (cmt == null) {
                log(aAST, "javadoc.missing");
            }
        }
    }

    /**
     * Whether we should check this node.
     * 
     * @param aAST
     *            a given node.
     * @return whether we should check a given node.
     */
    private boolean shouldCheck(final DetailAST aAST) {
        if (ScopeUtils.inCodeBlock(aAST)) {
            return false;
        }

        final Scope scope;
        if (aAST.getType() == TokenTypes.ENUM_CONSTANT_DEF) {
            scope = Scope.PUBLIC;
        } else {
            final DetailAST mods = aAST.findFirstToken(TokenTypes.MODIFIERS);
            final Scope declaredScope = ScopeUtils.getScopeFromMods(mods);
            scope = ScopeUtils.inInterfaceOrAnnotationBlock(aAST) ? Scope.PUBLIC : declaredScope;
        }

        final Scope surroundingScope = ScopeUtils.getSurroundingScope(aAST);

        return Utilities.isExportedPackage(aAST) && scope.isIn(mScope) && surroundingScope.isIn(mScope)
                && ((mExcludeScope == null) || !scope.isIn(mExcludeScope) || !surroundingScope.isIn(mExcludeScope));
    }

}
