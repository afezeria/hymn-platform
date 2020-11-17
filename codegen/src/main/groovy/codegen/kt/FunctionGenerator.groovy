package codegen.kt

import groovy.transform.Canonical

/**
 * @author afezeria
 */
@Canonical
class FunctionGenerator {
  String funName
  Gen gen
  Boolean returnList

  Function create() {
    if (!funName.startsWith("findBy")) throw new RuntimeException("查询方法必须以findBy开头")
    def orderBy = getOrderBy()
    def (exprBody, paramBody) = getWhereBody(
        orderBy.isBlank() ? funName.substring(6) : funName.substring(6, funName.lastIndexOf('OrderBy') - 1)
    )
    def entityClassName = gen.entityClassName

    return new Function(
        declare: "fun $funName($paramBody): ${if (returnList) "List<$entityClassName>" else "$entityClassName?"}",
        impl: """
    @TableCache()
    override fun $funName($paramBody): ${returnList ? "List<$entityClassName>" : "$entityClassName?"} {
        return dbService.db().from(table)
            .select(table.columns)
            .where { ${exprBody} }${
          orderBy}${
          returnList ? "" : "\n            .limit(0, 1)"}
            .map { table.createEntity(it) }${returnList ? "" : "\n            .firstOrNull()"}
    }
""")
  }

  static def splitByUpper(String str) {
    str.split("(?=\\p{Upper})")
  }
  def splitKey = [
      "And": "table.%s and %s",
      "Or" : "table.%s or %s",
  ]
  def exprWords = [
      "Is"               : ["table.%s eq %s", 2],
      "Equals"           : ["table.%s eq %s", 2],
      "Between"          : ["table.%s between %s..%s", 3],
      "After"            : ["table.%s greater %s", 2],
      "Before"           : ["table.%s less %s", 2],
      "Like"             : ["table.%s like %s", 2],
      "Not"              : ["table.%s notEq %s", 2],
      "In"               : ["table.%s inList %s", 2],
      "True"             : ["table.%s eq true", 1],
      "False"            : ["table.%s eq false", 1],
//    "Top" : ["limit %s" : 2],
      "LessThan"         : ["table.%s less %s", 2],
      "LessThanEquals"   : ["table.%s lessEq %s", 2],
      "GreaterThan"      : ["table.%s greater %s", 2],
      "GreaterThanEquals": ["table.%s greaterEq %s", 2],
      "IsNull"           : ["table.%s.isNull()", 1],
      "IsNotNull"        : ["table.%s.isNotNull()", 1],
      "NotNull"          : ["table.%s.isNotNull()", 1],
      "NotLike"          : ["table.%s notLike %s", 2],
      "StartingWith"     : ["table.%s like \"%s%%\"", 2],
      "EndingWith"       : ["table.%s like \"%%%s\"", 2],
      "Containing"       : ["table.%s like \"%%%s%%\"", 2],
      "NotIn"            : ["table.%s notInList %s", 2],
  ]
  def standardEqualsExpr = ["table.%s eq %s", 2]

  String getOrderBy() {
    def orderStr = funName - ~/.+OrderBy/
    if (orderStr == funName || orderStr.isBlank()) return ""
    def words = splitByUpper(orderStr)
    def fields = []
    def c = ""
    words.each {
      if (it == "Desc" || it == "Asc") {
        fields << "table.${c.uncapitalize()}.${it.toLowerCase()}()"
        c = ""
      } else {
        c += it
      }
    }
    return ".orderBy(${fields.join(', ')})"
  }
  /**
   * 获取where条件表达式
   */
  def getWhereBody(String name) {
    def splitWord = []
    def wordGroups = []
    def current = []
    splitByUpper(name).findAll {
      !it.isBlank()
    }.each {
      if (splitKey.containsKey(it)) {
        splitWord << it
        wordGroups << current
        current = []
      } else {
        current << it
      }
    }
    wordGroups.add(current)
    def exprList = []
    def parameterList = []
    wordGroups.forEach { group ->
      def (fieldName, expr, count) = getFieldWordAndExprWord(group)
      def field = checkFileldExist(fieldName)
      def tArgs = genParamsByFieldNameAndCount(fieldName, count)
      try{

      exprList.add(String.format(expr, *tArgs))
      }catch(e){
        println()
      }
      tArgs.drop(1).forEach {
        parameterList.add("$it: ${field.javaType}")
      }
    }
    def body = ""
    if (exprList.size() == 1) {
      body = exprList[0]
    } else {
      exprList.eachWithIndex { s, i ->
        if (i == 0) {
          body += "($s) "
        } else {
          body += "${splitWord[i - 1].toLowerCase()} ($s)"
        }
      }
    }
    return [body, parameterList.join(", ")]
  }
  /**
   * 获取函数名中的字段并根据关键字获取表达式
   * e.g. in [User,Name,Like] out ["userName", "table.%s like %s", 2]
   */
  Tuple3<String, String, Integer> getFieldWordAndExprWord(List<String> group) {
    def size = group.size()
    for (i in (3..1)) {
      if (size > i) {
        def exprWord = group.subList(size - i, size).join('')
        if (exprWords.containsKey(exprWord)) {
          return new Tuple3(
              group.subList(0, size).join('').uncapitalize(),
              exprWords[exprWord][0],
              exprWords[exprWord][1],
          )
        }
      }
    }
    return new Tuple3(group.join("").uncapitalize(), standardEqualsExpr[0], standardEqualsExpr[1])
  }

/**
 * 检查字段在表中是否存在
 * @param fieldName 字段名称（不是表的列名）
 */
  def checkFileldExist(String fieldName) {
    def field = gen.fields.find { it.fieldName == fieldName }
    if (field == null) {
      throw new RuntimeException("字段 ${fieldName} 在表 ${gen._table.name} 中不存在")
    }
    field
  }

  /**
   * 生成函数参数
   */
  static def genParamsByFieldNameAndCount(String fieldName, Integer count) {
    if (count <= 0) throw new RuntimeException("count must greater than 0")
    switch (count) {
      case 1: return [fieldName]
      case 2: return [fieldName,fieldName]
      default:
        return (1..count).collect { it > 1 ? fieldName + it : fieldName }
    }
  }

}
