/* This class is part of the XP framework
 *
 * $Id: HudsonJobTest.xp 12589 2010-12-30 17:56:43Z friebe $ 
 */

package org.hudson_ci.unittest;

import unittest.TestCase;
import unittest.AssertionFailedError;
import unittest.PrerequisitesNotMetError;

import org.hudson_ci.api.HudsonClient;
import org.hudson_ci.api.HudsonJob;
import org.hudson_ci.hudson.core.ProjectJobConfiguration;
import org.hudson_ci.hudson.core.Maven2JobConfiguration;

import xml.Tree;
import xml.Node;

/**
 * Tests Hudson job
 *
 * @see   xp://org.hudson_ci.api.HudsonJob
 */
public class HudsonJobTest extends TestCase {
  protected HudsonJob $fixture;

  /**
   * Sets up this test case
   *
   */
  public void setUp() throws AssertionFailedError, PrerequisitesNotMetError {
    $this.fixture= new HudsonJob($this.getName());
  }

  /**
   * Tests getConfigurationXml() with a project
   *
   */
  [@test]
  public void projectJobConfigurationObjectToXml() {
    $tree= $this.fixture.withConfiguration(new ProjectJobConfiguration()).getConfigurationXml();
    $this.assertInstanceOf('xml.Tree', $tree);
    $this.assertEquals('project', $tree.root.getName());
  }


  /**
   * Tests getConfigurationXml() with a project
   *
   */
  [@test]
  public void maven2JobConfigurationObjectToXml() {
    $tree= $this.fixture.withConfiguration(new Maven2JobConfiguration()).getConfigurationXml();
    $this.assertInstanceOf('xml.Tree', $tree);
    $this.assertEquals('maven2-moduleset', $tree.root.getName());
  }

  /**
   * Tests getConfigurationXml() with a project
   *
   */
  [@test]
  public void projectJobConfigurationXmlToXml() {
    $tree= $this.fixture.withConfigurationXml(new Tree('project')).getConfigurationXml();
    $this.assertInstanceOf('xml.Tree', $tree);
    $this.assertEquals('project', $tree.root.getName());
  }

  /**
   * Tests getConfigurationXml() with a project
   *
   */
  [@test]
  public void projectJobConfigurationObjectToObject() {
    $config= $this.fixture.withConfiguration(new ProjectJobConfiguration()).getConfiguration();
    $this.assertInstanceOf('org.hudson_ci.hudson.core.ProjectJobConfiguration', $config);
    $this.assertEquals('project', $config.identifier());
  }

  /**
   * Tests getConfiguration() with a project
   *
   */
  [@test]
  public void projectJobConfigurationXmlToObject() {
    $config= $this.fixture.withConfigurationXml(new Tree('project')).getConfiguration();
    $this.assertInstanceOf('org.hudson_ci.hudson.core.ProjectJobConfiguration', $config);
    $this.assertEquals('project', $config.identifier());
  }

  /**
   * Tests getConfiguration() when no configuration has been set yet
   *
   */
  [@test, @expect(class = 'lang.IllegalStateException', withMessage= 'No configuration available')]
  public void noConfigurationYet() {
    $this.fixture.getConfiguration();
  }

  /**
   * Tests getConfiguration() when no configuration has been set yet
   *
   */
  [@test, @expect(class = 'lang.IllegalStateException', withMessage= 'No configuration available')]
  public void noConfigurationXmlYet() {
    $this.fixture.getConfigurationXml();
  }

  /**
   * Creates a dummy Hudson client
   *
   * @param   url if user is omitted, the client will raise an exception
   */
  protected HudsonClient newDummyClient(string? $url= 'http://user:example@example.com/') {
    return new HudsonClient($url) {
      package Tree getJobConfigurationXml(string $name) {
        if (!$this.conn.getURL().getUser()) {
          throw new util.ServiceException(403, 'Authentication required');
        }
        $t= new Tree('project');
        $t.root.addChild(new Node('description', $name));
        return $t;
      }
    };
  }

  /**
   * Tests getConfigurationXml() lazy-loading the configuration
   *
   */
  [@test]
  public void configurationXmlLazyLoaded() {
    $tree= $this.fixture.withReferenceTo($this.newDummyClient()).getConfigurationXml();
    $this.assertInstanceOf('xml.Tree', $tree);
    $this.assertEquals('project', $tree.root.getName());
    $this.assertEquals($this.getName(), $tree.root.children[0].getContent());
  }

  /**
   * Tests getConfiguration() lazy-loading the configuration
   *
   */
  [@test]
  public void configurationLazyLoaded() {
    $config= $this.fixture.withReferenceTo($this.newDummyClient()).getConfiguration();
    $this.assertInstanceOf('org.hudson_ci.hudson.core.ProjectJobConfiguration', $config);
    $this.assertEquals('project', $config.identifier());
  }

  /**
   * Tests getConfiguration() lazy-loading the configuration
   *
   */
  [@test, @expect(class = 'util.ServiceException', withMessage= 'Authentication required')]
  public void exceptionDuringLazyLoading() {
    $this.fixture.withReferenceTo($this.newDummyClient('http://example.com/')).getConfiguration();
  }
}
