//base type
var JString = Java.type('java.lang.String')
var JInteger = Java.type('java.lang.Integer')
var JLong = Java.type('java.lang.Long')
var JDouble = Java.type('java.lang.Double')
var JFloat = Java.type('java.lang.Float')
var JBoolean = Java.type('java.lang.Boolean')
var JByte = Java.type('java.lang.Byte')
var JCharacter = Java.type('java.lang.Character')

var JBigDecimal = Java.type('java.math.BigDecimal')

//time
var JLocalDateTime = Java.type('java.time.LocalDateTime');
var JLocalDate = Java.type('java.time.LocalDate');
var JLocalTime = Java.type('java.time.LocalTime');
var JDateTimeFormatter = Java.type('java.time.format.DateTimeFormatter');
var JDuration = Java.type('java.time.Duration');
var JDayOfWeek = Java.type('java.time.DayOfWeek');
var JTemporalAdjusters = Java.type('java.time.temporal.TemporalAdjusters');
var JIsoFields = Java.type('java.time.temporal.IsoFields');

//exception
var BizError = Java.type(
    'com.github.afezeria.hymn.script.ScriptBusinessException');