package com.github.afezeria.hymn.core.platform.dataservice

import com.github.afezeria.hymn.common.platform.dataservice.DataService

/**
 * @author afezeria
 */
class DataServiceWrapper(impl: ScriptDataServiceImpl) : DataService by impl