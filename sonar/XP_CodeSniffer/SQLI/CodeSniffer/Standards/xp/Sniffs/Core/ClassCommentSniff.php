<?php

/**
 * Parses and verifies the doc comments for classes.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: ClassCommentSniff.php 12553 2010-12-08 17:09:23Z gelli $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

if (class_exists('SQLI_CodeSniffer_Standards_xp_parser_ClassCommentParser', true) === false) {
    $error = 'Class SQLI_CodeSniffer_Standards_xp_parser_ClassCommentParser not found';
    throw new PHP_CodeSniffer_Exception($error);
}

/**
 * Parses and verifies the doc comments for classes.
 *
 * Verifies that :
 * <ul>
 *  <li>A doc comment exists.</li>
 *  <li>There is a blank newline after the short description.</li>
 *  <li>There is a blank newline between the long and short description.</li>
 *  <li>There is a blank newline between the long description and tags.</li>
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
class xp_Sniffs_Core_ClassCommentSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * Tags in correct order and related info.
     *
     * @var array
     */
    protected $tags = array(
            'ext'   => array(
                             'required'       => FALSE,
                             'allow_multiple' => TRUE

                            ),
            'see'    => array(
                             'required'       => FALSE,
                             'allow_multiple' => TRUE
                            ),
            'purpose' => array(
                             'required'       => TRUE,
                             'allow_multiple' => FALSE
                            ),
            'test'     => array(
                             'required'       => FALSE,
                             'allow_multiple' => FALSE
                            ),
            'deprecated'  => array(
                             'required'       => FALSE,
                             'allow_multiple' => FALSE,
                             'allow_empty'    => TRUE
                            ),
    );

    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(
                T_CLASS,
                T_INTERFACE,
               );

    }//end register()

    /**
     * (Insert method's description here)
     *
     * @param   
     * @return  
     */
    protected function findDocComment($stackPtr, $phpcsFile) {
        $find   = array(T_ABSTRACT, T_WHITESPACE, T_FINAL);
        $tokens = $phpcsFile->getTokens();
        $type = strtolower($tokens[$stackPtr]['content']);
        $commentEnd = $phpcsFile->findPrevious($find, ($stackPtr - 1), null, true);

        if ($commentEnd !== false && $tokens[$commentEnd]['code'] === T_COMMENT) {

            // Check for annotation
            if ('#[@' === substr($tokens[$commentEnd]['content'], 0, 3)) {
              return $this->findDocComment($stackPtr - 1, $phpcsFile);
            }

            //$error = "You must use \"/**\" style comments for a $type comment";
            //$phpcsFile->addError($error, $stackPtr);
            $phpcsFile->addEvent('XP_CLASS_COMMENT_INVALID', array(), $stackPtr);
            return FALSE;

        } else if ($commentEnd === false || $tokens[$commentEnd]['code'] !== T_DOC_COMMENT) {
            //$phpcsFile->addError("Missing $type doc comment", $stackPtr);
            $phpcsFile->addEvent('XP_CLASS_COMMENT_MISSING', array(), $stackPtr);
            return FALSE;
        }
        
        return $commentEnd;
    }


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

        $tokens = $phpcsFile->getTokens();
        $type   = strtolower($tokens[$stackPtr]['content']);

        // Extract the class comment docblock.
       $commentEnd= $this->findDocComment($stackPtr, $phpcsFile);
       
       // var_dump($commentEnd);
        

        $commentStart = ($phpcsFile->findPrevious(T_DOC_COMMENT, ($commentEnd - 1), null, true) + 1);
        $commentNext  = $phpcsFile->findPrevious(T_WHITESPACE, ($commentEnd + 1), $stackPtr, false, $phpcsFile->eolChar);

        // Distinguish file and class comment.
        $prevClassToken = $phpcsFile->findPrevious(T_CLASS, ($stackPtr - 1));
        if ($prevClassToken === false) {
            // This is the first class token in this file, need extra checks.
            $prevNonComment = $phpcsFile->findPrevious(T_DOC_COMMENT, ($commentStart - 1), null, true);
            if ($prevNonComment !== false) {
                $prevComment = $phpcsFile->findPrevious(T_DOC_COMMENT, ($prevNonComment - 1));
                if ($prevComment === false) {
                    // There is only 1 doc comment between open tag and class token.
                    $newlineToken = $phpcsFile->findNext(T_WHITESPACE, ($commentEnd + 1), $stackPtr, false, $phpcsFile->eolChar);
                    if ($newlineToken !== false) {
                        $newlineToken = $phpcsFile->findNext(
                            T_WHITESPACE,
                            ($newlineToken + 1),
                            $stackPtr,
                            false,
                            $phpcsFile->eolChar
                        );

                        if ($newlineToken !== false) {
                            // Blank line between the class and the doc block.
                            // The doc block is most likely a file comment.
                            //$error = "Missing $type doc comment";
                            //$phpcsFile->addError($error, ($stackPtr + 1));
                            $phpcsFile->addEvent('XP_CLASS_COMMENT_MISSING', array(), ($stackPtr + 1));
                            return;
                        }
                    }//end if
                }//end if
            }//end if
        }//end if

        $comment = $phpcsFile->getTokensAsString(
            $commentStart,
            ($commentEnd - $commentStart + 1)
        );

        // Parse the class comment.docblock.
        try {
            $this->commentParser = new SQLI_CodeSniffer_Standards_xp_parser_ClassCommentParser($comment, $phpcsFile);
            $this->commentParser->parse();
        } catch (PHP_CodeSniffer_CommentParser_ParserException $e) {
            //$line = ($e->getLineWithinComment() + $commentStart);
            //$phpcsFile->addError($e->getMessage(), $line);
            $phpcsFile->addEvent('XP_CLASS_COMMENT_EXCEPTION', array('message' => $e->getMessage()), $line);
            return;
        }

        $comment = $this->commentParser->getComment();
        if (is_null($comment) === true) {
            //$error = ucfirst($type).' doc comment is empty';
            //$phpcsFile->addError($error, $commentStart);
            $phpcsFile->addEvent('XP_CLASS_COMMENT_EMPTY', array(), ($stackPtr + 1));
            return;
        }

        // No extra newline before short description.
        $short        = $comment->getShortComment();
        $newlineCount = 0;
        $newlineSpan  = strspn($short, $phpcsFile->eolChar);
        if ($short !== '' && $newlineSpan > 0) {
            $line  = ($newlineSpan > 1) ? 'newlines' : 'newline';
            //$error = "Extra $line found before $type comment short description";
            //$phpcsFile->addWarning($error, ($commentStart + 1));
            $phpcsFile->addEvent('XP_CLASS_COMMENT_EXTRA_NEWLINE_BEFORE_SHORT_DESCRIPTION', array(), ($commentStart + 1));
        }

        $newlineCount = (substr_count($short, $phpcsFile->eolChar) + 1);

        // Exactly one blank line between short and long description.
        $long = $comment->getLongComment();
        if (empty($long) === false) {
            $between        = $comment->getWhiteSpaceBetween();
            $newlineBetween = substr_count($between, $phpcsFile->eolChar);
            if ($newlineBetween !== 2) {
                //$error = "There must be exactly one blank line between descriptions in $type comments";
                //$phpcsFile->addError($error, ($commentStart + $newlineCount + 1));
                $phpcsFile->addEvent('XP_CLASS_COMMENT_EXTRA_NEWLINE_BETWEEN_DESCRIPTION', array(), ($commentStart + $newlineCount + 1));
            }

            $newlineCount += $newlineBetween;
        }

        // Exactly one blank line before tags.
        $tags = $this->commentParser->getTagOrders();
        if (count($tags) > 1) {
            $newlineSpan = $comment->getNewlineAfter();
            if ($newlineSpan !== 2) {
                if ($long !== '') {
                    $newlineCount += (substr_count($long, $phpcsFile->eolChar) - $newlineSpan + 1);
                }

                //$error = "There must be exactly one blank line before the tags in $type comments";
                //$phpcsFile->addError($error, ($commentStart + $newlineCount));
                $phpcsFile->addEvent('XP_CLASS_COMMENT_EXTRA_NEWLINE_BEFORE_TAGS', array(), ($commentStart + $newlineCount));

                $short = rtrim($short, $phpcsFile->eolChar.' ');
            }
        }

        // Check each tag.
        $this->processTags($commentStart, $commentEnd);

    }//end process()

    /**
     * Processes each required or optional tag.
     *
     * @param int $commentStart Position in the stack where the comment started.
     * @param int $commentEnd   Position in the stack where the comment ended.
     *
     * @return void
     */
    protected function processTags($commentStart, $commentEnd)
    {
        $foundTags   = $this->commentParser->getTagOrders();
        $orderIndex  = 0;
        $indentation = array();
        $longestTag  = 0;
        $errorPos    = 0;
        
        

        foreach ($this->commentParser->getUnknown() as $unknown) {
          $error= sprintf('Tag @%s not allowed in class comment', $unknown['tag']);
         //$this->currentFile->addError($error, $commentStart + $unknown['line']);
          $this->currentFile->addEvent('XP_CLASS_COMMENT_TAG_INVALID', array('message' => $error), $commentStart + $unknown['line']);
        }

        foreach ($this->tags as $tag => $info) {

            // Required tag missing.
            if ($info['required'] === true && in_array($tag, $foundTags) === false) {
                $error = "Missing @$tag tag in class comment";
                //$this->currentFile->addError($error, $commentEnd);
                $this->currentFile->addEvent('XP_CLASS_COMMENT_TAG_MISSING', array('message' => $error), $commentEnd);
                continue;
            }
            

             // Get the line number for current tag.
            $tagName = ucfirst($tag);
            if ($info['allow_multiple'] === true) {
                $tagName .= 's';
            }

            $getMethod  = 'get'.$tagName;
            $tagElement = $this->commentParser->$getMethod();
            if (is_null($tagElement) === true || empty($tagElement) === true) {
                continue;
            }

            $errorPos = $commentStart;
            if (is_array($tagElement) === false) {
                $errorPos = ($commentStart + $tagElement->getLine());
            }

            // Get the tag order.
            $foundIndexes = array_keys($foundTags, $tag);

            if (count($foundIndexes) > 1) {
                // Multiple occurance not allowed.
                if ($info['allow_multiple'] === false) {
                    $error = "Only 1 @$tag tag is allowed in a class comment";
                    //$this->currentFile->addError($error, $errorPos);
                    $this->currentFile->addEvent('XP_CLASS_COMMENT_TAG_DUPLICATE', array('message' => $error), $errorPos);
                } else {
                    // Make sure same tags are grouped together.
                    $i     = 0;
                    $count = $foundIndexes[0];
                    foreach ($foundIndexes as $index) {
                        if ($index !== $count) {
                            $errorPosIndex
                                = ($errorPos + $tagElement[$i]->getLine());
                            $error = "@$tag tags must be grouped together";
                            //$this->currentFile->addError($error, $errorPosIndex);
                            $this->currentFile->addEvent('XP_CLASS_COMMENT_TAG_GROPUPPED', array('message' => $error), $errorPosIndex);
                        }

                        $i++;
                        $count++;
                    }
                }
            }//end if

            // Store the indentation for checking.
            $len = strlen($tag);
            if ($len > $longestTag) {
                $longestTag = $len;
            }

            if (is_array($tagElement) === true) {
                foreach ($tagElement as $key => $element) {
                    $indentation[] = array(
                                      'tag'   => $tag,
                                      'space' => $this->getIndentation($tag, $element),
                                      'line'  => $element->getLine(),
                                     );
                }
            } else {
                $indentation[] = array(
                                  'tag'   => $tag,
                                  'space' => $this->getIndentation($tag, $tagElement),
                                 );
            }

            $method = 'process'.$tagName;
            if (method_exists($this, $method) === true) {
                // Process each tag if a method is defined.
                call_user_func(array($this, $method), $errorPos);
            } else {
                if (!isset($this->tags[$tag]['allow_empty']) || $this->tags[$tag]['allow_empty'] !== TRUE) {
                    if (is_array($tagElement) === true) {
                        foreach ($tagElement as $key => $element) {
                            $element->process(
                                $this->currentFile,
                                $commentStart,
                                'class'
                            );
                        }
                    } else {
                         $tagElement->process(
                             $this->currentFile,
                             $commentStart,
                             'class'
                         );
                    }
                }
            }
        }//end foreach

        /* Do not check for indentation of tags
        
        foreach ($indentation as $indentInfo) {
            if ($indentInfo['space'] !== 0
                && $indentInfo['space'] !== ($longestTag + 1)
            ) {
                $expected = (($longestTag - strlen($indentInfo['tag'])) + 1);
                $space    = ($indentInfo['space'] - strlen($indentInfo['tag']));
                $error    = "@$indentInfo[tag] tag comment indented incorrectly. ";
                $error   .= "Expected $expected spaces but found $space.";

                $getTagMethod = 'get'.ucfirst($indentInfo['tag']);

                if ($this->tags[$indentInfo['tag']]['allow_multiple'] === true) {
                    $line = $indentInfo['line'];
                } else {
                    $tagElem = $this->commentParser->$getTagMethod();
                    $line    = $tagElem->getLine();
                }

                $this->currentFile->addError($error, ($commentStart + $line));
            }
        } */

    }//end processTags()

    /**
     * Get the indentation information of each tag.
     *
     * @param string                                   $tagName    The name of the
     *                                                             doc comment
     *                                                             element.
     * @param PHP_CodeSniffer_CommentParser_DocElement $tagElement The doc comment
     *                                                             element.
     *
     * @return void
     */
    protected function getIndentation($tagName, $tagElement)
    {
        if ($tagElement instanceof PHP_CodeSniffer_CommentParser_SingleElement) {
            if ($tagElement->getContent() !== '') {
                return (strlen($tagName) + substr_count($tagElement->getWhitespaceBeforeContent(), ' '));
            }
        } else if ($tagElement instanceof PHP_CodeSniffer_CommentParser_PairElement) {
            if ($tagElement->getValue() !== '') {
                return (strlen($tagName) + substr_count($tagElement->getWhitespaceBeforeValue(), ' '));
            }
        }

        return 0;

    }//end getIndentation()


}//end class

?>
