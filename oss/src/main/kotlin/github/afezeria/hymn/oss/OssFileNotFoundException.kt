package github.afezeria.hymn.oss

import github.afezeria.hymn.common.exception.BusinessException

/**
 * @author afezeria
 */
class OssFileNotFoundException(conditionStr: String) :
    BusinessException("文件 [$conditionStr] 不存在", httpCode = 404)
