/* 
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2007, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package schemacrawler;


import schemacrawler.main.SchemaCrawlerCommandLine;
import schemacrawler.tools.grep.ColumnsGrep;
import sf.util.CommandLineUtility;
import sf.util.Config;
import dbconnector.dbconnector.DatabaseConnector;
import dbconnector.dbconnector.DatabaseConnectorFactory;

/**
 * Main class that takes arguments for grep-ping table and columns in a
 * schema.
 */
public final class Grep
{

  /**
   * Get connection parameters, and creates a connection, and crawls the
   * schema.
   * 
   * @param args
   *        Arguments passed into the program from the command line.
   * @throws Exception
   *         On an exception
   */
  public static void main(final String[] args)
    throws Exception
  {
    CommandLineUtility.checkForHelp(args, "/schemacrawler-grep-readme.txt");
    CommandLineUtility.setLogLevel(args);

    final SchemaCrawlerCommandLine commandLine = new SchemaCrawlerCommandLine(args);
    final Config config = commandLine.getConfig();
    final DatabaseConnector dataSourceParser = DatabaseConnectorFactory
      .createPropertiesDriverDataSourceParser(args, config);
    ColumnsGrep.grep(args, dataSourceParser);
  }

  private Grep()
  {
  }

}
