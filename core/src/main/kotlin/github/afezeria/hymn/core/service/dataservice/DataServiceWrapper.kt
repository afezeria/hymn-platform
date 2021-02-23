package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.platform.dataservice.DataService

/**
 * @author afezeria
 */
class DataServiceWrapper(impl: ScriptDataServiceImpl) : DataService by impl