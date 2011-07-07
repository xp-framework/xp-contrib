<?php
/**
 * Parses and verifies the doc comments for files.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: ClassHeaderSniff.php 12553 2010-12-08 17:09:23Z gelli $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

if (class_exists('PHP_CodeSniffer_CommentParser_ClassCommentParser', true) === false) {
    throw new PHP_CodeSniffer_Exception('Class PHP_CodeSniffer_CommentParser_ClassCommentParser not found');
}

/**
 * Parses and verifies the doc comments for files.
 *
 * Verifies that :
 * <ul>
 *  <li>A doc comment exists.</li>
 *  <li>There is a blank newline after the short description.</li>
 *  <li>There is a blank newline between the long and short description.</li>
 *  <li>There is a blank newline between the long description and tags.</li>
 *  <li>A PHP version is specified.</li>
 *  <li>Check the order of the tags.</li>
 *  <li>Check the indentation of each tag.</li>
 *  <li>Check required and optional tags and the format of their content.</li>
 * </ul>
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

class xp_Sniffs_Core_ClassHeaderSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * The header comment parser for the current file.
     *
     * @var PHP_CodeSniffer_Comment_Parser_ClassCommentParser
     */
    protected $commentParser = null;

    /**
     * The current PHP_CodeSniffer_File object we are processing.
     *
     * @var PHP_CodeSniffer_File
     */
    protected $currentFile = null;

    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(T_OPEN_TAG);

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
        $this->currentFile = $phpcsFile;

        // We are only interested if this is the first open tag.
        if ($stackPtr !== 0) {
            if ($phpcsFile->findPrevious(T_OPEN_TAG, ($stackPtr - 1)) !== false) {
                return;
            }
        }

        $tokens = $phpcsFile->getTokens();

        // Find the next non whitespace token.
        $commentStart
            = $phpcsFile->findNext(T_WHITESPACE, ($stackPtr + 1), null, true);
            

        if ($tokens[$commentStart]['code'] === T_CLOSE_TAG) {
            // We are only interested if this is the first open tag.
            return;
        } else if ($tokens[$commentStart]['code'] !== T_COMMENT) {
            //$error = 'You must use "/*" style comments for a file comment';
            //$phpcsFile->addError($error, $commentStart);
            $phpcsFile->addEvent('XP_CLASS_HEADER_INVALID', $commentStart);
            return;
        } else if ($commentStart === false
            || $tokens[$commentStart]['code'] !== T_COMMENT
        ) {
            //$phpcsFile->addError('Missing file doc comment', $errorToken);
            $phpcsFile->addEvent('XP_CLASS_HEADER_MISSING', $commentStart);
            return;
        } else {
            
            // File header must directly follow opening tag
            if ($tokens[$commentStart]['line'] !== $tokens[$stackPtr]['line']+1) {
              //$error = 'File header not directly following open tag';
              //$phpcsFile->addError($error, $commentStart);
              $phpcsFile->addEvent('XP_CLASS_HEADER_NOT_AFTER_OPEN_TAG', $commentStart);
            }

            // Extract the header comment docblock.
            $commentEnd = $phpcsFile->findNext(
                T_COMMENT,
                ($commentStart + 1),
                null,
                true
            ) - 1;

            $comment = $phpcsFile->getTokensAsString(
                $commentStart,
                ($commentEnd - $commentStart + 1)
            );

            // Parse the header comment docblock.
            try {
                $this->commentParser = new PHP_CodeSniffer_CommentParser_ClassCommentParser($comment, $phpcsFile);
                $this->commentParser->parse();
            } catch (PHP_CodeSniffer_CommentParser_ParserException $e) {
                $line = ($e->getLineWithinComment() + $commentStart);
                //$phpcsFile->addError($e->getMessage(), $line);
                $phpcsFile->addEvent('XP_CLASS_HEADER_EXCEPTION', array('message' => $e->getMessage()), $line);
                return;
            }

            $comment = $this->commentParser->getComment();

            // No extra newline before short description.
            $short        = $comment->getShortComment();
            $newlineCount = 0;
            $newlineSpan  = strspn($short, $phpcsFile->eolChar);
            
            if ($short !== '' && $newlineSpan > 0) {
                $line  = ($newlineSpan > 1) ? 'newlines' : 'newline';
                //$error = "Extra $line found before file comment short description";
                //$phpcsFile->addError($error, ($commentStart + 1));
                $phpcsFile->addEvent('XP_CLASS_HEADER_NEWLINE_BEFORE_SHORT_DESCRIPTION', array(), ($commentStart + 1));
            }
            
            $found= FALSE;
            if (strstr(trim($short), '$Id')) {
                //$error = "There must be an empty line between SVN ID tag and the description";
                //$phpcsFile->addError($error, ($commentStart));
                $phpcsFile->addEvent('XP_CLASS_HEADER_EMPTYLINE_BETWEEN_SHORT_DESCRIPTION_AND_SVNID', array(), $commentStart + 1);
                $found= TRUE;
            }

            $newlineCount = (substr_count($short, $phpcsFile->eolChar) + 1);

            // Exactly one blank line between short and long description.
            $long = $comment->getLongComment();
            
            if (empty($long) === false) {
                $between        = $comment->getWhiteSpaceBetween();
                $newlineBetween = substr_count($between, $phpcsFile->eolChar);
                if ($newlineBetween !== 2) {
                    //$error = 'There must be exactly one blank line between description and the ID Tag';
                    //$phpcsFile->addError($error, ($commentStart + $newlineCount + 1));
                    $phpcsFile->addEvent('XP_CLASS_HEADER_EMPTYLINE_BETWEEN_DESCRIPTION_AND_ID', array(), ($commentStart + $newlineCount + 1));
                }

                
                if (!strstr(trim($long), '$Id')) {
                    //$error = 'SVN Id tag is corrupt';
                    //$phpcsFile->addError($error, ($commentStart + $newlineCount + 1));
                    $phpcsFile->addEvent('XP_CLASS_HEADER_SVNID_CORRUPT', array(), ($commentStart + $newlineCount + 1));
                }
                $newlineCount += $newlineBetween;
            } elseif (!$found) {
              //$error = 'SVN Id tag is missing';
              //$phpcsFile->addError($error, ($commentStart + $newlineCount + 1));
              $phpcsFile->addEvent('XP_CLASS_HEADER_SVNID_MISSING', array(), ($commentStart + $newlineCount + 1));
            }
            
            if (2 < $comment->getNewlineAfter()) {
              //$error = 'Extra content after SVN ID Tag';
              //$phpcsFile->addError($error, ($commentStart + $newlineCount));
              $phpcsFile->addEvent('XP_CLASS_HEADER_CONTENT_AFTER_SVNID', array(), ($commentStart + $newlineCount));
            }
            
        }//end if

    }//end process()
}//end class
?>
