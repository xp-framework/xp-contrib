<?php
/**
 * Zend Coding Standard.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: xpCodingStandard.php 12539 2010-11-01 16:18:39Z gelli $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

if (class_exists('PHP_CodeSniffer_Standards_CodingStandard', true) === false) {
    throw new PHP_CodeSniffer_Exception('Class PHP_CodeSniffer_Standards_CodingStandard not found');
}

/**
 * Zend Coding Standard.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.0
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class PHP_CodeSniffer_Standards_xp_XpCodingStandard extends PHP_CodeSniffer_Standards_CodingStandard
{


    /**
     * Return a list of external sniffs to include with this standard.
     *
     * The Zend standard uses some PEAR sniffs.
     *
     * @return array
     */
    public function getIncludedSniffs()
    {
        return array(
                // 'Zend/Sniffs/NamingConventions/ValidVariableNameSniff.php',
                'Generic/Sniffs/Functions/OpeningFunctionBraceKernighanRitchieSniff.php',
                'Generic/Sniffs/WhiteSpace/DisallowTabIndentSniff.php',

                //'PEAR/Sniffs/WhiteSpace/ScopeClosingBraceSniff.php',
                'Generic/Sniffs/Formatting/DisallowMultipleStatementsSniff.php',
                'Generic/Sniffs/Formatting/NoSpaceAfterCastSniff.php',
                'Generic/Sniffs/Files/LineEndingsSniff.php',
                // 'Generic/Sniffs/Files/LineLengthSniff.php',
                'Generic/Sniffs/Strings/UnnecessaryStringConcatSniff.php',
                'Generic/Sniffs/CodeAnalysis/UnusedFunctionParameterSniff.php',
                'Generic/Sniffs/CodeAnalysis/UselessOverridingMethodSniff.php',
                'Generic/Sniffs/CodeAnalysis/UnnecessaryFinalModifierSniff.php',
                'Generic/Sniffs/PHP/NoSilencedErrorsSniff.php',
                'Generic/Sniffs/PHP/UpperCaseConstantSniff.php',
                'Generic/Sniffs/PHP/ForbiddenFunctionsSniff.php',
                'Generic/Sniffs/Commenting/TodoSniff.php',
                'Generic/Sniffs/WhiteSpace/DisallowTabIndentSniff.php',
                'Generic/Sniffs/NamingConventions/ConstructorNameSniff.php',
                'Generic/Sniffs/NamingConventions/UpperCaseConstantNameSniff.php',
                'Squiz/Sniffs/Scope/StaticThisUsageSniff.php',
                'Squiz/Sniffs/Scope/MethodScopeSniff.php',
                // 'Squiz/Sniffs/Scope/MemberVarScopeSniff.php',
                // 'Squiz/Sniffs/Functions/FunctionDeclarationArgumentSpacingSniff.php', 
                'Squiz/Sniffs/Functions/LowercaseFunctionKeywordsSniff.php',
                'Squiz/Sniffs/Classes/LowercaseClassKeywordsSniff.php',
                'Squiz/Sniffs/Strings/DoubleQuoteUsageSniff.php',
                'Squiz/Sniffs/Classes/ValidClassNameSniff.php',
                'Squiz/Sniffs/Classes/SelfMemberReferenceSniff.php',
                'Squiz/Sniffs/PHP/HeredocSniff.php',
                'Squiz/Sniffs/PHP/EvalSniff.php',
                
                // Fancy shit
                'Generic/Sniffs/Metrics/NestingLevelSniff.php',
                'Generic/Sniffs/Metrics/CyclomaticComplexitySniff.php',
                
/*                'PEAR/Sniffs/ControlStructures/ControlSignatureSniff.php',
                'PEAR/Sniffs/Functions/FunctionCallArgumentSpacingSniff.php',
                'PEAR/Sniffs/Functions/FunctionCallSignatureSniff.php',
                'PEAR/Sniffs/Functions/ValidDefaultValueSniff.php',

                'Squiz/Sniffs/Functions/GlobalFunctionSniff.php',  */
               );

    }//end getIncludedSniffs()


}//end class
?>
