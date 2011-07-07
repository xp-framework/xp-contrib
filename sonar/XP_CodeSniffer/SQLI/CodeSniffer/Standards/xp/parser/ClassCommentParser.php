<?php
/**
 * Parses Class doc comments.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: ClassCommentParser.php 12539 2010-11-01 16:18:39Z gelli $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

if (class_exists('PHP_CodeSniffer_CommentParser_AbstractParser', true) === false) {
    $error = 'Class PHP_CodeSniffer_CommentParser_AbstractParser not found';
    throw new PHP_CodeSniffer_Exception($error);
}

/**
 * Parses Class doc comments.
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
class SQLI_CodeSniffer_Standards_xp_parser_ClassCommentParser extends PHP_CodeSniffer_CommentParser_AbstractParser
{

    /**
     * The tags that this class can process.
     *
     * @var array(string)
     */
    private static $_tags = array(
                             'ext'         => true,
                             'see'         => true,
                             'purpose'     => true,
                             'test'        => true,
                             'deprecated'  => false
                            );

    /**
     * The package element of this class.
     *
     * @var SingleElement
     */
    private $_extensions = array();

    /**
     * The subpackage element of this class.
     *
     * @var SingleElement
     */
    private $_references = array();

    /**
     * The version element of this class.
     *
     * @var SingleElement
     */
    private $_purpose = null;

    /**
     * The category element of this class.
     *
     * @var SingleElement
     */
    private $_test = null;

    /**
     * Returns the allowed tags withing a class comment.
     *
     * @return array(string => int)
     */
    protected function getAllowedTags()
    {
        return array(
                'ext'         => true,
                'see'         => true,
                'purpose'     => true,
                'test'        => true,
                'deprecated'  => false
               );

    }//end getAllowedTags()

    /**
     * Parses the license tag of this class comment.
     *
     * @param array $tokens The tokens that comprise this tag.
     *
     * @return PHP_CodeSniffer_CommentParser_PairElement
     */
    protected function parsePurpose($tokens)
    {
        $this->_purpose = new PHP_CodeSniffer_CommentParser_SingleElement(
            $this->previousElement,
            $tokens,
            'purpose',
            $this->phpcsFile
        );

        return $this->_purpose;

    }//end parsePurpose()

    /**
     * Parses the license tag of this class comment.
     *
     * @param array $tokens The tokens that comprise this tag.
     *
     * @return PHP_CodeSniffer_CommentParser_PairElement
     */
    protected function parseTest($tokens)
    {
        $this->_test = new PHP_CodeSniffer_CommentParser_SingleElement(
            $this->previousElement,
            $tokens,
            'test',
            $this->phpcsFile
        );

        return $this->_test;

    }//end parsePurpose()

    /**
     * Parses the license tag of this class comment.
     *
     * @param array $tokens The tokens that comprise this tag.
     *
     * @return PHP_CodeSniffer_CommentParser_PairElement
     */
    protected function parseExt($tokens)
    {
        $extension = new PHP_CodeSniffer_CommentParser_SingleElement(
            $this->previousElement,
            $tokens,
            'test',
            $this->phpcsFile
        );
        
        $this->_extensions[]= $extension;
        return $extension;

    }//end parsePurpose()

    /**
     * Returns extensions
     *
     * @return  array(PHP_CodeSniffer_CommentParser_SingleElement)
     */
    public function getExts()
    {
        return $this->_extensions;
    }//end getExts()

    /**
     * Returns extensions
     *
     * @return  array(PHP_CodeSniffer_CommentParser_SingleElement)
     */
    public function getTest()
    {
        return $this->_test;
    }//end getTest()

    /**
     * Returns extensions
     *
     * @return  array(PHP_CodeSniffer_CommentParser_SingleElement)
     */
    public function getPurpose()
    {
        return $this->_purpose;
    }//end getTest()

    /**
     * Returns the authors of this class comment.
     *
     * @return array(PHP_CodeSniffer_CommentParser_SingleElement)
     */
    public function getAuthors()
    {
        return $this->_authors;

    }//end getAuthors()
}//end class
?>
