package github.afezeria.hymn.oss.module.service

import github.afezeria.hymn.oss.module.dto.FileRecordDto
import github.afezeria.hymn.oss.module.entity.FileRecord

/**
 * @author afezeria
 */
interface FileRecordService {

    fun removeById(id: String): Int

    fun update(id: String, dto: FileRecordDto): Int

    fun update(id: String, data: Map<String, Any?>): Int

    fun create(dto: FileRecordDto): String

    fun findAll(): MutableList<FileRecord>

    fun findById(id: String): FileRecord?

    fun findByIds(ids: List<String>): MutableList<FileRecord>

    fun findByBucketAndPath(bucket: String, objectName: String): FileRecord?

    fun removeByBucketAndPath(bucket: String, path: String): Int

    fun pageFindByBucket(bucket: String, pageSize: Int, pageNum: Int): List<FileRecord>

    fun pageFindByContainFileName(fileName: Int, pageSize: Int, pageNum: Int): List<FileRecord>

}