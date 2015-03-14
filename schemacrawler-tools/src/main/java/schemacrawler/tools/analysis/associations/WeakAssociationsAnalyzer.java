/*
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2015, Sualeh Fatehi.
 * This library is free software; you can redistribute it and/or modify it under
 * the terms
 * of the GNU Lesser General Public License as published by the Free Software
 * Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package schemacrawler.tools.analysis.associations;


import static schemacrawler.tools.analysis.associations.CatalogWithAssociations.addWeakAssociationToTable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import schemacrawler.schema.Column;
import schemacrawler.schema.ColumnReference;
import schemacrawler.schema.Table;

final class WeakAssociationsAnalyzer
{

  private static final Logger LOGGER = Logger
    .getLogger(WeakAssociationsAnalyzer.class.getName());

  private final List<Table> tables;
  private final Collection<ColumnReference> weakAssociations;

  WeakAssociationsAnalyzer(final List<Table> tables)
  {
    this.tables = tables;
    weakAssociations = new TreeSet<>();
  }

  Collection<ColumnReference> analyzeTables()
  {
    if (tables == null || tables.size() < 3)
    {
      return Collections.emptySet();
    }

    findWeakAssociations(tables);

    return weakAssociations;
  }

  private void addWeakAssociation(final ColumnReference weakAssociation)
  {
    if (weakAssociation != null)
    {
      weakAssociations.add(weakAssociation);

      addWeakAssociationToTable(weakAssociation.getPrimaryKeyColumn()
        .getParent(), weakAssociation);
      addWeakAssociationToTable(weakAssociation.getForeignKeyColumn()
        .getParent(), weakAssociation);
    }
  }

  private void findWeakAssociations(final List<Table> tables)
  {
    final ForeignKeys foreignKeys = new ForeignKeys(tables);
    final ColumnMatchKeysMap columnMatchKeysMap = new ColumnMatchKeysMap(tables);
    final TableMatchKeys tableMatchKeys = new TableMatchKeys(tables);
    for (final Table table: tables)
    {
      final TableCandidateKeys tableCandidateKeys = new TableCandidateKeys(table);
      for (final Column pkColumn: tableCandidateKeys)
      {
        final Set<String> fkColumnMatchKeys = new HashSet<>();
        // Look for all columns matching this table name
        fkColumnMatchKeys.addAll(tableMatchKeys.get(table));
        // Look for all columns matching this column name
        final ColumnMatchKeys columnMatchKeys = new ColumnMatchKeys(pkColumn);
        fkColumnMatchKeys.addAll(columnMatchKeys.get());

        final Set<Column> fkColumns = new HashSet<>();
        for (final String fkColumnMatchKey: fkColumnMatchKeys)
        {
          if (columnMatchKeysMap.containsKey(fkColumnMatchKey))
          {
            fkColumns.addAll(columnMatchKeysMap.get(fkColumnMatchKey));
          }
        }

        for (final Column fkColumn: fkColumns)
        {
          final WeakAssociation weakAssociation = new WeakAssociation(pkColumn,
                                                                      fkColumn);
          if (weakAssociation.isValid()
              && !foreignKeys.contains(weakAssociation))
          {
            LOGGER.log(Level.FINE, String.format("Found weak association: %s",
                                                 weakAssociation));
            if (weakAssociations != null)
            {
              addWeakAssociation(weakAssociation);
            }
          }
        }
      }
    }
  }

}
