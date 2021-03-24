/**
 * string 转java Integer
 * @param radix 进制
 * @returns {JInteger}
 */
String.prototype.toInt = function (radix = 10) {
  return tools.str.toIntOrNull(this, radix);
}
/**
 * string 转 java Long
 * @param radix 进制
 * @returns {JLong}
 */
String.prototype.toLong = function (radix = 10) {
  return tools.str.toLongOrNull(this, radix);
}
/**
 * string 转 java BigDecimal
 * @param precision 精度，包括整数位
 * @returns {JBigDecimal}
 */
String.prototype.toBigDecimal = function (precision = 0) {
  return tools.str.toBigDecimalOrNull(this, precision);
}
/**
 * string 转 java Byte
 * @param radix 进制
 * @returns {JByte}
 */
String.prototype.toByte = function (radix = 10) {
  return tools.str.toByteOrNull(this, radix);
}
/**
 * string 转 java Float
 * @returns {JFloat}
 */
String.prototype.toFloat = function () {
  return tools.str.toDoubleOrNull(this);
}
/**
 * string 转 java Double
 * @returns {JDouble}
 */
String.prototype.toDouble = function () {
  return tools.str.toDoubleOrNull(this);
}
/**
 * string 转 java Boolean
 * @returns {JBoolean}
 */
String.prototype.toBoolean = function () {
  return tools.str.toBoolean(this);
}
/**
 * string 转 java 原始类型byte数组
 * @param charset
 * @returns {byte[]}
 */
String.prototype.toByteArray = function (charset) {
  return tools.str.toByteArray(this, charset);
}
/**
 * string 转java 原始类型char数组
 * @returns {char[]}
 */
String.prototype.toCharArray = function () {
  return tools.str.toCharArray(this);
}
/**
 * string 转 java LocalDate
 * 默认格式： yyyy-MM-dd
 * @param format 格式字符串
 * @returns {JLocalDate}
 */
String.prototype.toDate = function (format = null) {
  return tools.str.toDate(this, format);
}
/**
 * string 转 java LocalDateTime
 * 默认格式： yyyy-MM-dd HH:mm:ss
 * @param format 格式字符串
 * @returns {JLocalDateTime}
 */
String.prototype.toDateTime = function (format = null) {
  return tools.str.toDateTime(this, format);
}
/**
 * 计算字符串md5
 * @returns {string}
 */
String.prototype.md5 = function () {
  return tools.str.md5(this);
}

Object.freeze(String.prototype)