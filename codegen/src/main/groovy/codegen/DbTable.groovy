package codegen

import groovy.transform.Canonical

/**
 * @author afezeria
 */
@Canonical
class DbTable {
  String schema
  String name
  String comment
  List<Field> fields
}
