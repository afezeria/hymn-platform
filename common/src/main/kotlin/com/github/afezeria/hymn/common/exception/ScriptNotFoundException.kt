package com.github.afezeria.hymn.common.exception

/**
 * @author afezeria
 */
class ScriptNotFoundException(api: String) : InnerException("脚本 [api:$api] 不存在")