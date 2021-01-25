package github.afezeria.hymn.oss.module.service.impl

import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.util.DataNotFoundException
import github.afezeria.hymn.common.util.msgById
import github.afezeria.hymn.oss.module.dao.FileRecordDao
import github.afezeria.hymn.oss.module.dto.FileRecordDto
import github.afezeria.hymn.oss.module.entity.FileRecord
import github.afezeria.hymn.oss.module.service.FileRecordService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author afezeria
 */
@Service
class FileRecordServiceImpl : FileRecordService {

    @Autowired
    private lateinit var fileRecordDao: FileRecordDao

    @Autowired
    private lateinit var dbService: DataBaseService


    override fun removeById(id: String): Int {
        fileRecordDao.selectById(id)
            ?: throw DataNotFoundException("FileRecord".msgById(id))
        val i = fileRecordDao.deleteById(id)
        return i
    }

    override fun removeByBucketAndPath(bucket: String, path: String): Int {
        return fileRecordDao.deleteByBucketAndPath(bucket, path)
    }

    override fun update(id: String, dto: FileRecordDto): Int {
        val e = fileRecordDao.selectById(id)
            ?: throw DataNotFoundException("FileRecord".msgById(id))
        dto.update(e)
        val i = fileRecordDao.update(e)
        return i
    }

    override fun create(dto: FileRecordDto): String {
        val e = dto.toEntity()
        val id = fileRecordDao.insert(e)
        return id
    }

    override fun findAll(): MutableList<FileRecord> {
        return fileRecordDao.selectAll()
    }


    override fun findById(id: String): FileRecord? {
        return fileRecordDao.selectById(id)
    }

    override fun findByIds(ids: List<String>): MutableList<FileRecord> {
        return fileRecordDao.selectByIds(ids)
    }

    override fun findByBucketAndPath(bucket: String, objectName: String): FileRecord? {
        return fileRecordDao.selectByBucketAndPath(bucket, objectName)
    }


}