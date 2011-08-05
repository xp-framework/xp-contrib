<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  $package= 'net.xp_forge.apidoc';

  uses(
    'lang.ResourceProvider',
    'text.doclet.Doclet', 
    'text.doclet.markup.MarkupBuilder', 
    'util.cmd.Console',
    'util.collections.HashTable',
    'io.FileUtil',
    'io.File',
    'io.Folder',
    'io.streams.FileOutputStream'
  );
  
  /**
   * Generates api documentation for the given number of classes
   *
   */
  class net·xp_forge·apidoc·Doclet extends Doclet {
    protected 
      $api      = NULL,
      $css      = NULL,
      $hierarchy= array();
    
    /**
     * Returns the first sentence inside a given text
     *
     * @param   string text
     * @return  string
     */
    protected function firstSentence($text) {
      return strtok($text, ".\r\n");
    }

    /**
     * Writes header
     *
     * @param   string title
     * @param   io.streams.OutputStream
     */
    protected function writeHeader($title, OutputStream $stream) {
      $stream->write('<html><head>');
      $stream->write('<title>'.$title.($this->api ? ' ('.$this->api.')' : '').'</title>');
      $this->css && $stream->write('<style type="text/css">'.$this->css.'</style>');
      $stream->write('</head><body>');
    }

    /**
     * Writes footer
     *
     * @param   io.streams.OutputStream
     */
    protected function writeFooter(OutputStream $stream) {
      $this->gen && $stream->write('<div id="foot">'.$this->gen.'</div>');
      $stream->write('</body></html>');
    }
    
    /**
     * Creates a link to a given type
     *
     * @param   string t qualified type name
     * @param   string base
     * @return  string the link as HTML or the type itself if it cannot be found
     */
    protected function typeLink($type, $base) {
      sscanf($type, '%[^#]#%s', $t, $anchor);
      if ('[]' === substr($t, -2)) {
        $raw= substr($t, 0, -2);
        $components= '[]';
      } else if ($p= strpos($t, '<')) {
        $raw= substr($t, 0, $p);
        $components= htmlspecialchars(substr($t, $p));
      } else {
        $raw= $t;
        $components= '';
      }
      foreach ($this->hierarchy as $level) {
        foreach ($level['contents'] as $docs) {
          foreach ($docs as $doc) {
            if ($doc->qualifiedName() !== $raw) continue;
            return sprintf(
              '<a class="type" title="%s in %s" href="%s%s.html%s">%s%s</a>%s',
              $doc->classType(),
              $doc->containingPackage()->name(),
              $base,
              strtr($raw, '.', '/'),
              $anchor ? '#'.$anchor : '',
              $doc->name(),
              $anchor ? '::'.$anchor.'()' : '',
              $components
            );
          }
        }
      }
      return $t;
    }
    
    /**
     * Returns an array of classes implementing this class
     *
     * @param   text.doclet.ClassDoc class
     * @return  text.doclet.ClassDoc[]
     */
    protected function classesImplementing(ClassDoc $class) {
      $r= array();
      foreach ($this->hierarchy as $level) {
        foreach ($level['contents'] as $docs) {
          foreach ($docs as $doc) {
            for ($doc->interfaces->rewind(); $doc->interfaces->hasNext(); ) {
              if ($class->equals($doc->interfaces->next())) $r[]= $doc;
            }
          }
        }
      }
      return $r;
    }

    /**
     * Returns an array of all interfaces implemented by the given class
     *
     * @param   text.doclet.ClassDoc class
     * @return  text.doclet.ClassDoc[]
     */
    protected function interfacesImplementedBy(ClassDoc $class) {
      $r= array();
      for ($implemented= array(), $class->interfaces->rewind(); $class->interfaces->hasNext(); ) {
        $n= $class->interfaces->next();
        $n && $r[]= $n;
      }
      
      return $r;
    }

    /**
     * Returns an array of all interfaces implemented by the given class
     *
     * @param   text.doclet.ClassDoc class
     * @return  text.doclet.ClassDoc[]
     */
    protected function allInterfacesImplementedBy(ClassDoc $class) {
      $r= array();
      do {
        for ($implemented= array(), $class->interfaces->rewind(); $class->interfaces->hasNext(); ) {
          $n= $class->interfaces->next();
          $n && $r[]= $n;
        }
      } while ($class= $class->superclass);
      
      return $r;
    }

    /**
     * Returns an array of classes subclassing this class
     *
     * @param   text.doclet.ClassDoc class
     * @return  text.doclet.ClassDoc[]
     */
    protected function directSubclassesOf(ClassDoc $class) {
      $r= array();
      foreach ($this->hierarchy as $level) {
        foreach ($level['contents'] as $docs) {
          foreach ($docs as $doc) {
            if ($class->equals($doc->superclass)) $r[]= $doc;
          }
        }
      }
      return $r;
    }
    
    /**
     * Returns a tag's contents
     *
     * @param   text.doclet.Tag tag
     * @param   string base
     * @return  string
     */
    protected function tagContents(Tag $tag, $base) {
      if ($tag instanceof ReturnTag) {
        return $this->typeLink($tag->type, $base).($tag->text ? ' - '.$tag->text : '');
      } else if ($tag instanceof ParamTag) {
        return $this->typeLink($tag->type, $base).' '.$tag->parameter.($tag->text ? ' - '.$tag->text : '');
      } else if ($tag instanceof SeeTag) {
        switch ($tag->scheme) {
          case 'xp': return $this->typeLink($tag->urn, $base);
          case 'http': return '<a href="http://'.$tag->urn.'">http://'.$tag->urn.'</a>';
          default: return $tag->urn;
        }
      } else if ($tag instanceof TestTag) {
        return $this->typeLink($tag->class, $base);
      } else if ($tag instanceof ThrowsTag) {
        return $this->typeLink($tag->exception->qualifiedName(), $base).($tag->text ? ' - '.$tag->text : '');
      } else {
        return $tag->text();
      }
    }
  
    /**
     * Writes method signaturw
     *
     * @param   text.doclet.MethodDoc method
     * @param   string base
     * @param   io.streams.OutputStream
     */
    protected function writeSignature(MethodDoc $method, $base, OutputStream $stream) {
      $return= $method->tags('return');
      $params= $method->tags('param');
      $stream->write(($return ? $this->typeLink($return[0]->type, $base) : 'void').' ');
      $stream->write('<a href="#'.$method->name().'">'.$method->name().'</a>(');
      for ($i= 0, $s= sizeof($params); $i < $s; $i++) {
        $stream->write($this->typeLink($params[$i]->type, $base).' '.$params[$i]->parameter);
        $i < $s- 1 && $stream->write(', ');
      }
      $stream->write(')');
    }
    
    /**
     * Writes tags
     *
     * @param   string title
     * @param   text.doclet.Tag[] tags
     * @param   string base
     * @param   io.streams.OutputStream
     */
    protected function writeTags($title, array $tags, $base, OutputStream $stream) {
      if (empty($tags)) return;
      $stream->write('<dt>'.$title.':</dt>');
      foreach ($tags as $tag) {
        $stream->write('<dd>'.$this->tagContents($tag, $base).'</dd>');
      }
    }
    
    /**
     * Write markup
     *
     * @param   io.streams.OutputStream
     * @param   string input
     */
    protected function writeMarkup($input, OutputStream $stream) {
      $stream->write('<div class="markup"><p>');
      $stream->write(strtr($this->processor->markupFor($input), array(
        '<code>'  => '<pre class="code">',
        '</code>' => '</pre>',
        '<h1>'    => '<h2>',
        '</h1>'   => '</h2>',
        '<h2>'    => '<h3>',
        '</h2>'   => '</h3>',
        '<h3>'    => '<h4>',
        '</h3>'   => '</h4>',
      )));
      $stream->write('</p></div>');
    }
    
    /**
     * Returns whether a given doc is static
     *
     * @param   text.doclet.Doc d
     * @return  bool
     */
    protected function isStatic(Doc $d) {
      return array_key_exists('static', $d->getModifiers());
    }
    
    /**
     * Generate class documentation for a given class and write it to the
     * given output stream
     *
     * @param   text.doclet.ClassDoc doc
     * @param   io.streams.OutputStream
     * @return  io.streams.OutputStream
     */
    protected function writeDoc(ClassDoc $doc, OutputStream $stream) {
      $this->writeHeader($doc->name(), $stream);
      $base= str_repeat('../', substr_count($doc->qualifiedName(), '.'));
      
      // Summary
      $stream->write('<h4><a href="'.$base.'index.html">Overview</a>');
      $stream->write(' &#xbb; <a href="summary.html">'.$doc->containingPackage()->name().'</a></h4>');
      $stream->write('<h1>'.ucfirst($doc->classType()).' '.$doc->name().'</h1>');

      $stream->write('<dl>');
      if ($doc->isInterface()) {
        $implementations= $this->classesImplementing($doc);
        if (!empty($implementations)) {
          $stream->write('<dt>All known implementing classes:</dt><dd>');
          for ($i= 0, $s= sizeof($implementations); $i < $s; $i++) {
            $stream->write($this->typeLink($implementations[$i]->qualifiedName(), $base));
            $i < $s- 1 && $stream->write(', ');
          }
          $stream->write('</dd>');
        }
      } else {
        $implemented= $this->allInterfacesImplementedBy($doc);
        if (!empty($implemented)) {
          $stream->write('<dt>All implemented interfaces:</dt><dd>');
          for ($i= 0, $s= sizeof($implemented); $i < $s; $i++) {
            $stream->write($this->typeLink($implemented[$i]->qualifiedName(), $base));
            $i < $s- 1 && $stream->write(', ');
          }
          $stream->write('</dd>');
        }
        $subclasses= $this->directSubclassesOf($doc);
        if (!empty($subclasses)) {
          $stream->write('<dt>Direct known subclasses:</dt><dd>');
          for ($i= 0, $s= sizeof($subclasses); $i < $s; $i++) {
            $stream->write($this->typeLink($subclasses[$i]->qualifiedName(), $base));
            $i < $s- 1 && $stream->write(', ');
          }
          $stream->write('</dd>');
        }
      }
      $stream->write('</dl>');
      $stream->write('<hr/>');
      
      // Signature
      $stream->write('<code>');
      $stream->write(implode(' ', array_keys($doc->getModifiers())).' '.$doc->classType().' '.$doc->name());
      if (!$doc->isInterface()) {
        $doc->superclass && $stream->write(' extends '.$this->typeLink($doc->superclass->qualifiedName(), $base));
        $implemented= $this->interfacesImplementedBy($doc);
        if (!empty($implemented)) {
          $stream->write(' implements ');
          for ($i= 0, $s= sizeof($implemented); $i < $s; $i++) {
            $stream->write($this->typeLink($implemented[$i]->qualifiedName(), $base));
            $i < $s- 1 && $stream->write(', ');
          }
        }
      }
      $stream->write('</code>');
      $this->writeMarkup($doc->commentText(), $stream);
      
      // Class tags
      $stream->write('<dl>');
      $this->writeTags('See also', $doc->tags('see'), $base, $stream);
      $this->writeTags('Verified by', $doc->tags('test'), $base, $stream);
      $stream->write('</dl>');
      $stream->write('<hr/>');

      // Field summary
      $stream->write('<fieldset><legend>Fields Summary</legend><ul>');
      foreach ($doc->fields as $field) {
        $v= $field->constantValue();
        $stream->write('<li><code>');
        $stream->write($this->isStatic($field) ? 'static ' : '');
        $stream->write($field->name().($v ? ' = '.$v : ''));
        $stream->write('</code><p>'.$this->firstSentence($field->commentText()).'</p></li>');
      }
      $stream->write('</ul></fieldset>');
      
      // Method summary
      $stream->write('<fieldset><legend>Method Summary</legend><ul>');
      foreach ($doc->methods as $method) {
        $stream->write('<li><code>');
        $stream->write($this->isStatic($method) ? 'static ' : '');
        $this->writeSignature($method, $base, $stream);
        $stream->write('</code><p>'.$this->firstSentence($method->commentText()).'</p></li>');
      }
      $stream->write('</ul></fieldset>');
      
      // Method details
      foreach ($doc->methods as $method) {
        $stream->write('<a name="'.$method->name().'"><h3>'.$method->name().'</h3></a>');
        $stream->write('<code>'.implode(' ', array_keys($method->getModifiers())).' ');
        $this->writeSignature($method, $base, $stream);
        $stream->write('</code><div class="dfn">');
        $this->writeMarkup($method->commentText(), $stream);

        $stream->write('<dl>');
        $this->writeTags('Parameters', $method->tags('param'), $base, $stream);
        $this->writeTags('Returns', $method->tags('return'), $base, $stream);
        $this->writeTags('Throws', $method->tags('throws'), $base, $stream);
        $this->writeTags('See also', $method->tags('see'), $base, $stream);
        $stream->write('</dl>');
        $stream->write('</div>');
        $stream->write('<hr/>');
      }
      
      $this->writeFooter($stream);
      return $stream;
    }

    /**
     * Generate package documentation for a given package and write it to the
     * given output stream
     *
     * @param   array contents
     * @param   io.Folder target
     * @return  io.streams.OutputStream
     */
    protected function writePackage(array $definitions, Folder $target) {
      with ($stream= new FileOutputStream(new File($target, 'summary.html'))); {
        $this->writeHeader($definitions['doc']->name(), $stream);

        $stream->write('<h4><a href="'.str_repeat('../', substr_count($definitions['doc']->name(), '.')+ 1).'index.html">Overview</a></h4>');
        $stream->write('<h1>Package '.$definitions['doc']->name().'</h1>');
        $this->writeMarkup($definitions['doc']->commentText(), $stream);

        foreach ($definitions['contents'] as $type => $docs) {
          if (empty($docs)) continue;

          // Contents
          $stream->write('<fieldset><legend>'.ucfirst($type).' Summary</legend><ul>');
          foreach ($docs as $doc) {
            $stream->write('<li><a href="'.$doc->name().'.html">'.$doc->name().'</a>');
            $stream->write('<p>'.$this->firstSentence($doc->commentText()).'</p></li>');
            $this->writeDoc($doc, new FileOutputStream(new File($target, $doc->name().'.html')))->close();
          }
          $stream->write('</ul></fieldset>');
        }
      
        $this->writeFooter($stream);
      }
      return $stream;
    }

    /**
     * Generate overview
     *
     * @param   array<string, array> packages
     * @param   io.Folder target
     * @return  io.streams.OutputStream
     */
    protected function writeOverview(array $packages, Folder $target) {
      with ($stream= new FileOutputStream(new File($target, 'index.html'))); {
        $this->writeHeader('Overview', $stream);
        $stream->write('<h4><a href="index.html">Overview</a></h4>');
        $stream->write('<h1>Overview '.($this->api ? ' ('.$this->api.')' : '').'</h1>');

        // Summary
        $stream->write('<fieldset><legend>Packages</legend><ul>');
        foreach ($packages as $name => $definitions) {
          $stream->write('<li><a href="'.strtr($name, '.', '/').'/summary.html">'.$name.'</a>');
          $stream->write('<p>'.$this->firstSentence($definitions['doc']->commentText()).'</p></li>');

          $this->writePackage($definitions, $definitions['target'])->close();
        }
        $stream->write('</ul></fieldset>');

        $this->writeFooter($stream);
        return $stream;
      }
    }
    
    /**
     * Returns valid options
     *
     * @return  array<string, int>
     */
    public function validOptions() {
      return array('output' => HAS_VALUE, 'api' => HAS_VALUE, 'css' => HAS_VALUE, 'gen' => HAS_VALUE);
    }
    
    /**
     * Entry point
     *
     * @param   text.doclet.RootDoc root
     * @return  var
     */
    public function start(RootDoc $root) {
      $this->processor= new MarkupBuilder();
    
      // Option: API (will be used in title, default: none)
      $this->api= $this->option('api');

      // Option: Gen (will be used in footer)
      $this->gen= $this->option('gen');
      
      // Option: CSS to embed (filename, default: none)
      if ($css= $this->option('css')) {
        $this->css= FileUtil::getContents(new File($css));
      }

      // Option: Output folder (default: Current directory, created if non-existant)
      $target= new Folder($this->option('output', '.'));
      $target->exists() || $target->create();
      
      // Compute hierarchy
      Console::write('[');
      $this->hierarchy= array();

      $seen= array();
      while ($this->classes->hasNext()) {
        $class= $this->classes->next();

        if (isset($seen[$class->qualifiedName()])) {
          continue;
        }

        $seen[$class->qualifiedName()]= TRUE;

        $key= $class->containingPackage()->name();
        if (!isset($this->hierarchy[$key])) {
          $sub= new Folder($target, strtr($key, '.', DIRECTORY_SEPARATOR));
          $sub->exists() || $sub->create();
          
          $this->hierarchy[$key]= array(
            'target'    => $sub,
            'doc'       => $class->containingPackage(),
            'contents'  => array(
              INTERFACE_CLASS => array(),
              ORDINARY_CLASS  => array(),
              ENUM_CLASS      => array(),
              EXCEPTION_CLASS => array(),
              ERROR_CLASS     => array()
            )
          );
        }
        
        Console::write('.');
        $this->hierarchy[$key]['contents'][$class->classType()][]= $class;
      }
      
      // Generate HTML files
      Console::write('>');
      $this->writeOverview($this->hierarchy, $target)->close();
      Console::writeLine($target, ']');
    }
  }
?>
