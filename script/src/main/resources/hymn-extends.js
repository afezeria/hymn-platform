var BigDecimal = Java.type('java.math.BigDecimal');

//time
var LocalDateTime = Java.type('java.time.LocalDateTime');
var LocalDate = Java.type('java.time.LocalDate');
var LocalTime = Java.type('java.time.LocalTime');
var DateTimeFormatter = Java.type('java.time.format.DateTimeFormatter');

var ScriptBusinessException = Java.type(
    'com.github.afezeria.hymn.script.ScriptBusinessException');

function BizError(msg) {
  return new ScriptBusinessException(msg);
}
