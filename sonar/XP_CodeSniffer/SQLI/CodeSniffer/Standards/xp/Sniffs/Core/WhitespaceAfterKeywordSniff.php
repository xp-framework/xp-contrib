<?php
/**
 * Generic_Sniffs_Formatting_MultipleStatementAlignmentSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: WhitespaceAfterKeywordSniff.php 12553 2010-12-08 17:09:23Z gelli $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Generic_Sniffs_Formatting_MultipleStatementAlignmentSniff.
 *
 * Checks alignment of assignments. If there are multiple adjacent assignments,
 * it will check that the equals signs of each assignment are aligned. It will
 * display a warning to advise that the signs should be aligned.
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
class xp_Sniffs_Core_WhitespaceAfterKeywordSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(
            T_IF,
            T_ELSE,
            T_ELSEIF,
            T_FOREACH,
            T_WHILE,
            T_SWITCH,
            T_FOR,
            T_TRY,
            T_CLOSE_PARENTHESIS
        );

    }//end register()

    /**
     * Processes this test, when one of its tokens is encountered.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file being scanned.
     * @param int                  $stackPtr  The position of the current token
     *                                        in the stack passed in $tokens.
     *
     * @return void
     */
    public function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();
        
        // Check if there is a scope opening after a closing bracket
        if($tokens[$stackPtr]['code'] == T_CLOSE_PARENTHESIS) {
          $check= $phpcsFile->findNext(T_WHITESPACE, $stackPtr + 1, NULL, TRUE);
          if ($tokens[$check]['code'] == T_OPEN_CURLY_BRACKET) {
            // var_dump($tokens[$check]);
          }
          return;
        }
        
        $nextToken= $tokens[$stackPtr + 1];
        
        if ($nextToken['code'] === T_WHITESPACE) {
          
        } else {
          $error= sprintf('Not enough whitespace after Keyword: %s', $tokens[$stackPtr]['content']);
          //$phpcsFile->addError($error, $stackPtr);
          $phpcsFile->addEvent(
            'XP_WHITESPACE_AFTER_KEYWORD_MISSING', 
            array('message'=> $error),
            $stackPtr
          );
          
        }
        
    } //end process
}//end class

?>

