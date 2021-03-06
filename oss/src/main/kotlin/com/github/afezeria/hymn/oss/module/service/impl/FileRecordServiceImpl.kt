package com.github.afezeria.hymn.oss.module.service.impl

import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.util.msgById
import com.github.afezeria.hymn.oss.module.dao.FileRecordDao
import com.github.afezeria.hymn.oss.module.dto.FileRecordDto
import com.github.afezeria.hymn.oss.module.entity.FileRecord
import com.github.afezeria.hymn.oss.module.service.FileRecordService
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
    private lateinit var dbService: DatabaseService


    override fun removeById(id: String): Int {
        fileRecordDao.selectById(id)
            ?: throw DataNotFoundException("FileRecord".msgById(id))
        val i = fileRecordDao.deleteById(id)
        return i
    }

    override fun update(id: String, dto: FileRecordDto): Int {
        val e = fileRecordDao.selectById(id)
            ?: throw DataNotFoundException("FileRecord".msgById(id))
        dto.update(e)
        val i = fileRecordDao.update(e)
        return i
    }

    override fun update(id: String, data: Map<String, Any?>): Int {
        return fileRecordDao.update(id, data)
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


    override fun removeByBucketAndPath(bucket: String, path: String): Int {
        return fileRecordDao.deleteByBucketAndPath(bucket, path)
    }

    override fun pageFindByBucket(bucket: String, pageSize: Int, pageNum: Int): List<FileRecord> {
        return fileRecordDao.pageSelectByBucket(bucket, pageSize, pageNum)
    }

    override fun pageFindByContainFileName(
        fileName: String?,
        pageSize: Int,
        pageNum: Int
    ): List<FileRecord> {
        return fileRecordDao.pageSelectByContainFileName(fileName, pageSize, pageNum)
    }


}