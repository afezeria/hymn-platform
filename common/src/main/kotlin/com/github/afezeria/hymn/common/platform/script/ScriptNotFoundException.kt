package com.github.afezeria.hymn.common.platform.script

import com.github.afezeria.hymn.common.exception.InnerException

/**
 * @author afezeria
 */
class ScriptNotFoundException(api: String) : InnerException("脚本 [api:$api] 不存在")