SELECT
  *
FROM
  INFORMATION_SCHEMA.TABLE_CONSTRAINTS
ORDER BY
  CONSTRAINT_CATALOG,
  CONSTRAINT_SCHEMA,
  CONSTRAINT_NAME
