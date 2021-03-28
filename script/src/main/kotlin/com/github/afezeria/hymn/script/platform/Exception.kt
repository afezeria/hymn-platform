package com.github.afezeria.hymn.script.platform

import com.github.afezeria.hymn.common.exception.BusinessException
import com.github.afezeria.hymn.common.exception.InnerException

/**
 * @author afezeria
 */
class ScriptBusinessException(msg: String) : BusinessException(msg)
class CompileException(msg: String) : InnerException(msg)

class ApiNotFoundException(api: String) : BusinessException("custom api '${api}' not found")
class CustomFunctionNotFoundException(api: String) :
    InnerException("custom function [id:${api}] not found")