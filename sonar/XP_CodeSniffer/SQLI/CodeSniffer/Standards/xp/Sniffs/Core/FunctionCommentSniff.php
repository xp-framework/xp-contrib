<?php
/**
 * Parses and verifies the doc comments for functions.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: FunctionCommentSniff.php 12553 2010-12-08 17:09:23Z gelli $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

if (class_exists('PHP_CodeSniffer_CommentParser_FunctionCommentParser', true) === false) {
    throw new PHP_CodeSniffer_Exception('Class PHP_CodeSniffer_CommentParser_FunctionCommentParser not found');
}

/**
 * Parses and verifies the doc comments for functions.
 *
 * Verifies that :
 * <ul>
 *  <li>A comment exists</li>
 *  <li>There is a blank newline after the short description.</li>
 *  <li>There is a blank newline between the long and short description.</li>
 *  <li>There is a blank newline between the long description and tags.</li>
 *  <li>Parameter names represent those in the method.</li>
 *  <li>Parameter comments are in the correct order</li>
 *  <li>Parameter comments are complete</li>
 *  <li>A space is present before the first and after the last parameter</li>
 *  <li>A return type exists</li>
 *  <li>There must be one blank line between body and headline comments.</li>
 *  <li>Any throw tag must have an exception class.</li>
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
class xp_Sniffs_Core_FunctionCommentSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * The name of the method that we are currently processing.
     *
     * @var string
     */
    private $_methodName = '';

    /**
     * The position in the stack where the fucntion token was found.
     *
     * @var int
     */
    private $_functionToken = null;

    /**
     * The position in the stack where the class token was found.
     *
     * @var int
     */
    private $_classToken = null;

    /**
     * The function comment parser for the current method.
     *
     * @var PHP_CodeSniffer_Comment_Parser_FunctionCommentParser
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
        return array(T_FUNCTION);

    }//end register()

    /**
     * (Insert method's description here)
     *
     * @param   
     * @return  
     */
    protected function findDocComment($stackPtr, $phpcsFile) {
        $find = array(T_COMMENT, T_DOC_COMMENT, T_CLASS, T_FUNCTION, T_OPEN_TAG);
        $tokens = $phpcsFile->getTokens();
        $commentEnd = $phpcsFile->findPrevious($find, ($stackPtr - 1));
        
        if (FALSE === $commentEnd) return FALSE;
        
        $code = $tokens[$commentEnd]['code'];
        
        if ($code === T_COMMENT) {
          
            // Check for annotation
            if ('#[@' === substr($tokens[$commentEnd]['content'], 0, 3)) {
              return $this->findDocComment($stackPtr - 1, $phpcsFile);
            }
        
            //$error = "You must use \"/**\" style comments for a $type comment";
            //$phpcsFile->addError($error, $stackPtr);
            $phpcsFile->addEvent('XP_FUNCTION_COMMENT_INVALID', array(), $stackPtr);
            return FALSE;

        } else if ($code !== T_DOC_COMMENT) {
            //$phpcsFile->addError('Missing function doc comment', $stackPtr);
            $phpcsFile->addEvent('XP_FUNCTION_COMMENT_MISSING', array(), $stackPtr);
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
        $tokens            = $phpcsFile->getTokens();
    
        $commentEnd = $this->findDocComment($stackPtr, $phpcsFile);

        if ($commentEnd === FALSE) {
            return;
        }



        // If there is any code between the function keyword and the doc block
        // then the doc block is not for us.
        $ignore    = PHP_CodeSniffer_Tokens::$scopeModifiers;
        $ignore[]  = T_STATIC;
        $ignore[]  = T_WHITESPACE;
        $ignore[]  = T_ABSTRACT;
        $ignore[]  = T_FINAL;
        $ignore[]  = T_COMMENT;
        $prevToken = $phpcsFile->findPrevious($ignore, ($stackPtr - 1), null, true);
        if ($prevToken !== $commentEnd) {
            //$phpcsFile->addError('Missing function doc comment', $stackPtr);
            $phpcsFile->addEvent('XP_FUNCTION_COMMENT_MISSING', array(), $stackPtr);
            return;
        }
        
        // Check for empty lines between method comment and declaration
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
                $phpcsFile->addEvent('XP_FUNCTION_COMMENT_MISSING', array(), ($stackPtr + 1));
                return;
            }
        }//end if

        $this->_functionToken = $stackPtr;

        $this->_classToken = null;
        foreach ($tokens[$stackPtr]['conditions'] as $condPtr => $condition) {
            if ($condition === T_CLASS || $condition === T_INTERFACE) {
                $this->_classToken = $condPtr;
                break;
            }
        }

        // If the first T_OPEN_TAG is right before the comment, it is probably
        // a file comment.
        $commentStart = ($phpcsFile->findPrevious(T_DOC_COMMENT, ($commentEnd - 1), null, true) + 1);
        $prevToken    = $phpcsFile->findPrevious(T_WHITESPACE, ($commentStart - 1), null, true);
        if ($tokens[$prevToken]['code'] === T_OPEN_TAG) {
            // Is this the first open tag?
            if ($stackPtr === 0 || $phpcsFile->findPrevious(T_OPEN_TAG, ($prevToken - 1)) === false) {
                //$phpcsFile->addError('Missing function doc comment', $stackPtr);
                $phpcsFile->addEvent('XP_FUNCTION_COMMENT_MISSING', array(), $stackPtr);
                return;
            }
        }

        $comment           = $phpcsFile->getTokensAsString($commentStart, ($commentEnd - $commentStart + 1));
        $this->_methodName = $phpcsFile->getDeclarationName($stackPtr);

        try {
            $this->commentParser = new PHP_CodeSniffer_CommentParser_FunctionCommentParser($comment, $phpcsFile);
            $this->commentParser->parse();
        } catch (PHP_CodeSniffer_CommentParser_ParserException $e) {
            $line = ($e->getLineWithinComment() + $commentStart);
            $phpcsFile->addError($e->getMessage(), $line);
            return;
        }

        $comment = $this->commentParser->getComment();
        if (is_null($comment) === true) {
            //$error = 'Function doc comment is empty';
            //$phpcsFile->addError($error, $commentStart);
            $phpcsFile->addEvent('XP_FUNCTION_COMMENT_EMPTY', array(), $commentStart);
            return;
        }

        $this->processParams($commentStart);
        $this->processThrows($commentStart);
        
        $functionToken= $tokens[$this->_functionToken];
        
        if ($phpcsFile->findNext(T_RETURN, $functionToken['scope_opener'], $functionToken['scope_closer'])) {
          $this->processReturn($commentStart, $commentEnd);
        }
        
        // No extra newline before short description.
        $short        = $comment->getShortComment();
        $newlineCount = 0;
        $newlineSpan  = strspn($short, $phpcsFile->eolChar);
        if ($short !== '' && $newlineSpan > 0) {
            //$line  = ($newlineSpan > 1) ? 'newlines' : 'newline';
            //$error = "Extra $line found before function comment short description";
            //$phpcsFile->addError($error, ($commentStart + 1));
            $phpcsFile->addEvent('XP_FUNCTION_COMMENT_NEWLINE_BEFORE_SHORT_DESCRIPTION', array(), ($commentStart + 1));
        }

        $newlineCount = (substr_count($short, $phpcsFile->eolChar) + 1);

        // Exactly one blank line between short and long description.
        $long = $comment->getLongComment();
        if (empty($long) === false) {
            $between        = $comment->getWhiteSpaceBetween();
            $newlineBetween = substr_count($between, $phpcsFile->eolChar);
            if ($newlineBetween !== 2) {
                //$error = 'There must be exactly one blank line between descriptions in function comment';
                //$phpcsFile->addError($error, ($commentStart + $newlineCount + 1));
                $phpcsFile->addEvent('XP_FUNCTION_COMMENT_EXTRA_NEWLINE_BETWEEN_DESCRIPTION', array(), ($commentStart + $newlineCount + 1));
            }

            $newlineCount += $newlineBetween;
        }

        // Exactly one blank line before tags.
        $params = $this->commentParser->getTagOrders();
        if (count($params) > 1) {
            $newlineSpan = $comment->getNewlineAfter();
            if ($newlineSpan !== 2) {
                $error = 'There must be exactly one blank line before the tags in function comment';
                if ($long !== '') {
                    $newlineCount += (substr_count($long, $phpcsFile->eolChar) - $newlineSpan + 1);
                }

                //$phpcsFile->addError($error, ($commentStart + $newlineCount));
                $phpcsFile->addEvent('XP_FUNCTION_COMMENT_EXTRA_NEWLINE_BEFORE_TAGS', array(), ($commentStart + $newlineCount));
                $short = rtrim($short, $phpcsFile->eolChar.' ');
            }
        }

    }//end process()


    /**
     * Process any throw tags that this function comment has.
     *
     * @param int $commentStart The position in the stack where the
     *                          comment started.
     *
     * @return void
     */
    protected function processThrows($commentStart)
    {
        if (count($this->commentParser->getThrows()) === 0) {
            return;
        }

        foreach ($this->commentParser->getThrows() as $throw) {

            $exception = $throw->getValue();
            $errorPos  = ($commentStart + $throw->getLine());

            if ($exception === '') {
                //$error = '@throws tag must contain the exception class name';
                //$this->currentFile->addError($error, $errorPos);
                $this->currentFile->addEvent('XP_FUNCTION_COMMENT_THROWS_MUST_CONTAIN_CLASSNAME', array(), $errorPos);
            }
        }

    }//end processThrows()


    /**
     * Process the return comment of this function comment.
     *
     * @param int $commentStart The position in the stack where the comment started.
     * @param int $commentEnd   The position in the stack where the comment ended.
     *
     * @return void
     */
    protected function processReturn($commentStart, $commentEnd)
    {
        // Skip constructor and destructor.
        $className = '';
        if ($this->_classToken !== null) {
            $className = $this->currentFile->getDeclarationName($this->_classToken);
            $className = strtolower(ltrim($className, '_'));
        }

        $methodName      = strtolower(ltrim($this->_methodName, '_'));
        $isSpecialMethod = ($this->_methodName === '__construct' || $this->_methodName === '__destruct');

        if ($isSpecialMethod === false && $methodName !== $className) {
            // Report missing return tag.
            if ($this->commentParser->getReturn() === null) {
                //$error = 'Missing @return tag in function comment';
                //$this->currentFile->addError($error, $commentEnd);
                $this->currentFile->addEvent('XP_FUNCTION_COMMENT_RETURN_MISSING', array(), $commentEnd);
            } else if (trim($this->commentParser->getReturn()->getRawContent()) === '') {
                //$error    = '@return tag is empty in function comment';
                $errorPos = ($commentStart + $this->commentParser->getReturn()->getLine());
                //$this->currentFile->addError($error, $errorPos);
                $this->currentFile->addEvent('XP_FUNCTION_COMMENT_RETURN_EMPTY', array(), $errorPos);
            }
        }

    }//end processReturn()


    /**
     * Process the function parameter comments.
     *
     * @param int $commentStart The position in the stack where
     *                          the comment started.
     *
     * @return void
     */
    protected function processParams($commentStart)
    {
        $realParams = $this->currentFile->getMethodParameters($this->_functionToken);

        $params      = $this->commentParser->getParams();
        $foundParams = array();

        if (empty($params) === false) {

            $lastParm = (count($params) - 1);
            /* if (substr_count($params[$lastParm]->getWhitespaceAfter(), $this->currentFile->eolChar) !== 2) {
                $error    = 'Last parameter comment requires a blank newline after it';
                $errorPos = ($params[$lastParm]->getLine() + $commentStart);
                $this->currentFile->addError($error, $errorPos);
            } */

            // Parameters must appear immediately after the comment.
            /*if ($params[0]->getOrder() !== 2) {
                $error    = 'Parameters must appear immediately after the comment';
                $errorPos = ($params[0]->getLine() + $commentStart);
                $this->currentFile->addError($error, $errorPos);
            } */

            $previousParam      = null;
            $spaceBeforeVar     = 10000;
            $spaceBeforeComment = 10000;
            $longestType        = 0;
            $longestVar         = 0;

            foreach ($params as $param) {

                $paramComment = trim($param->getComment());
                $errorPos     = ($param->getLine() + $commentStart);

                // Make sure that there is only one space before the var type.
                /* if ($param->getWhitespaceBeforeType() !== ' ') {
                    $error = 'Expected 1 space before variable type';
                    $this->currentFile->addError($error, $errorPos);
                } */

                $spaceCount = substr_count($param->getWhitespaceBeforeVarName(), ' ');
                if ($spaceCount < $spaceBeforeVar) {
                    $spaceBeforeVar = $spaceCount;
                    $longestType    = $errorPos;
                }

                $spaceCount = substr_count($param->getWhitespaceBeforeComment(), ' ');

                if ($spaceCount < $spaceBeforeComment && $paramComment !== '') {
                    $spaceBeforeComment = $spaceCount;
                    $longestVar         = $errorPos;
                }

                // Make sure they are in the correct order,
                // and have the correct name.
                $pos = $param->getPosition();

                $paramName = ($param->getVarName() !== '') ? $param->getVarName() : '[ UNKNOWN ]';

                if ($previousParam !== null) {
                    $previousName = ($previousParam->getVarName() !== '') ? $previousParam->getVarName() : 'UNKNOWN';

                    // Check to see if the parameters align properly.
                    /*if ($param->alignsVariableWith($previousParam) === false) {
                        $error = 'The variable names for parameters '.$previousName.' ('.($pos - 1).') and '.$paramName.' ('.$pos.') do not align';
                        $this->currentFile->addError($error, $errorPos);
                    }*/

                    /*if ($param->alignsCommentWith($previousParam) === false) {
                        $error = 'The comments for parameters '.$previousName.' ('.($pos - 1).') and '.$paramName.' ('.$pos.') do not align';
                        $this->currentFile->addError($error, $errorPos);
                    }*/
                }//end if

                // Make sure the names of the parameter comment matches the
                // actual parameter.
                if (isset($realParams[($pos - 1)]) === true) {
                    $realName      = substr($realParams[($pos - 1)]['name'], 1);
                    $foundParams[] = $realName;
                    // Append ampersand to name if passing by reference.
                    /*if ($realParams[($pos - 1)]['pass_by_reference'] === true) {
                        $realName = '&'.$realName;
                    }*/

                    if ($realName !== $param->getVarName()) {
                        $error  = 'Doc comment var "'.$paramName;
                        $error .= '" does not match actual variable name "'.$realName;
                        $error .= '" at position '.$pos;

                        //$this->currentFile->addError($error, $errorPos);
                        $this->currentFile->addEvent('XP_FUNCTION_COMMENT_PARAM_NOMATCH', array('message' => $error), $errorPos);
                    }
                } else {
                    // We must have an extra parameter comment.
                    $error = 'Superfluous doc comment at position '.$pos;
                    //$this->currentFile->addError($error, $errorPos);
                    $this->currentFile->addEvent('XP_FUNCTION_COMMENT_PARAM_SUPERFLUOUS', array('message' => $error), $errorPos);
                }

                if ($param->getVarName() === '') {
                    $error = 'Missing parameter name at position '.$pos;
                     //$this->currentFile->addError($error, $errorPos);
                     $this->currentFile->addEvent('XP_FUNCTION_COMMENT_PARAM_MISSING_NAME', array('message' => $error), $errorPos);
                }
                
                // var_dump($realParams[($pos - 1)]['pass_by_reference']);

                if ($param->getType() === '') {
                    $error = 'Missing type at position '.$pos;
                    //$this->currentFile->addError($error, $errorPos);
                    $this->currentFile->addEvent('XP_FUNCTION_COMMENT_PARAM_MISSING_TYPE', array('message' => $error), $errorPos);
                } else if (
                    ($realParams[($pos - 1)]['pass_by_reference'] === FALSE) &&
                    ('&' === substr($param->getType(), 0, 1))
                ) {
                    $error = 'Apidoc states parameter is passed by reference but it is not at position '.$pos;
                    //$this->currentFile->addError($error, $errorPos);
                } else if (
                    ($realParams[($pos - 1)]['pass_by_reference'] === TRUE) && 
                    ('&' !== substr($param->getType(), 0, 1))
                ) {
                    $error = 'Apidoc states parameter is not passed by reference but it is at position '.$pos;
                    //$this->currentFile->addError($error, $errorPos);
                }

                /* if ($paramComment === '') {
                    $error = 'Missing comment for param "'.$paramName.'" at position '.$pos;
                    $this->currentFile->addError($error, $errorPos);
                } */

                $previousParam = $param;

            }//end foreach

            if ($spaceBeforeVar !== 1 && $spaceBeforeVar !== 10000 && $spaceBeforeComment !== 10000) {
                $error = 'Expected 1 space after the longest type';
                //$this->currentFile->addError($error, $longestType);
                $this->currentFile->addEvent('XP_FUNCTION_COMMENT_SPACE_AFTER_LONGEST_TYPE', array(), $longestType);
            }

            if ($spaceBeforeComment !== 1 && $spaceBeforeComment !== 10000) {
                $error = 'Expected 1 space after the longest variable name';
                //$this->currentFile->addError($error, $longestVar);
                $this->currentFile->addEvent('XP_FUNCTION_COMMENT_SPACE_AFTER_LONGEST_VARIABLE', array(), $longestType);
            }

        }//end if

        $realNames = array();
        foreach ($realParams as $realParam) {
            $realNames[] = substr($realParam['name'], 1);
        }

        // Report and missing comments.
        $diff = array_diff($realNames, $foundParams);

        foreach ($diff as $neededParam) {
            if (count($params) !== 0) {
                $errorPos = ($params[(count($params) - 1)]->getLine() + $commentStart);
            } else {
                $errorPos = $commentStart;
            }

            $error = 'Doc comment for "'.$neededParam.'" missing';
            //$this->currentFile->addError($error, $errorPos);
            $this->currentFile->addEvent('XP_FUNCTION_COMMENT_PARAM_MISSING', array('message' => $error), $errorPos);
        }

    }//end processParams()


}//end class

?>
