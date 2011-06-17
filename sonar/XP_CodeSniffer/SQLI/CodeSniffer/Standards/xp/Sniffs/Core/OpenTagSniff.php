<?php
/**
 * Generic_Sniffs_PHP_DisallowShortOpenTagSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: OpenTagSniff.php 12553 2010-12-08 17:09:23Z gelli $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Generic_Sniffs_PHP_DisallowShortOpenTagSniff.
 *
 * Makes sure that shorthand PHP open tags are not used.
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
class xp_Sniffs_Core_OpenTagSniff implements PHP_CodeSniffer_Sniff
{


    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(
                T_OPEN_TAG,
                T_OPEN_TAG_WITH_ECHO,
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
    
        if (0 !== $stackPtr) {
            //$phpcsFile->addError('Extra content before open tag', $stackPtr);
            $phpcsFile->addEvent(
              'XP_OPEN_TAG_EXTRA_CONTENT_BEFORE_OPEN_TAG', 
              array(),
              $stackPtr
            );
        }

        $tokens  = $phpcsFile->getTokens();
        $openTag = $tokens[$stackPtr];

        if ($openTag['content'] === '<?') {
            $error = 'Short PHP opening tag used. Found "'.$openTag['content'].'" Expected "<?php".';
            //$phpcsFile->addError($error, $stackPtr);
            $phpcsFile->addEvent(
              'XP_OPEN_TAG_SHORT_PHP_OPENING_TAG', 
              array('message'=> $error),
              $stackPtr
            );
        }

        if ($openTag['code'] === T_OPEN_TAG_WITH_ECHO) {
            $nextVar = $tokens[$phpcsFile->findNext(PHP_CodeSniffer_Tokens::$emptyTokens, ($stackPtr + 1), null, true)];
            $error   = 'Short PHP opening tag used with echo. Found "';
            $error  .= $openTag['content'].' '.$nextVar['content'].' ..." but expected "<?php echo '.$nextVar['content'].' ...".';
            //$phpcsFile->addError($error, $stackPtr);
            $phpcsFile->addEvent(
              'XP_OPEN_TAG_SHORT_PHP_OPENING_TAG_WITH_ECHO', 
              array('message'=> $error),
              $stackPtr
            );
        }

    }//end process()


}//end class

?>
