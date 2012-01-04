/**
 * Maven XP-Framework plugin
 * Copyright (C) 2011 1&1 Internet AG
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.apache.maven.plugins.xpframework.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.shared.filtering.MavenResourcesExecution;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;

/**
 * Utility class
 *
 */
public final class MavenResourceUtils {

  /**
   * Utility classes should not have a public or default constructor
   *
   */
  private MavenResourceUtils() {
  }

  /**
   * Wrapper over MavenResourcesExecution: single resource
   *
   */
  public static void copyResource(Resource resource, File targetDirectory, MavenProject project, MavenSession session, MavenResourcesFiltering mavenResourcesFiltering) throws IOException {
    List<Resource> resources= new ArrayList<Resource>();
    resources.add(resource);
    MavenResourceUtils.copyResources(resources, targetDirectory, project, session, mavenResourcesFiltering);
  }

  /**
   * Wrapper over MavenResourcesExecution: multiple resources
   *
   */
  public static void copyResources(List<Resource> resources, File targetDirectory, MavenProject project, MavenSession session, MavenResourcesFiltering mavenResourcesFiltering) throws IOException {

    // Check no resources added
    if (resources.isEmpty()) return;

    // Init executor
    MavenResourcesExecution mavenResourcesExecution= new MavenResourcesExecution(
      resources,
      targetDirectory,
      project,
      "UTF-8",
      null,
      Collections.EMPTY_LIST,
      session
    );

    // Configure executor
    mavenResourcesExecution.setEscapeWindowsPaths(true);
    mavenResourcesExecution.setInjectProjectBuildFilters(false);
    mavenResourcesExecution.setOverwrite(true);
    mavenResourcesExecution.setIncludeEmptyDirs(false);
    mavenResourcesExecution.setSupportMultiLineFiltering(false);

    // Filter resources
    try{
      mavenResourcesFiltering.filterResources(mavenResourcesExecution);

    } catch (MavenFilteringException ex) {
      throw new IOException("Failed to copy resources", ex);
    }
  }
}
