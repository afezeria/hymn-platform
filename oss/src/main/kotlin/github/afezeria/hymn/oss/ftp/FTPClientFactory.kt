package github.afezeria.hymn.oss.ftp

import github.afezeria.hymn.common.util.InnerException
import mu.KLogging
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject
import java.io.IOException

/**
 * @author afezeria
 */
class FTPClientFactory(val config: FTPConfig) : BasePooledObjectFactory<FTPClient>() {
    companion object : KLogging()

    override fun create(): FTPClient {
        val ftpClient = FTPClient()
        ftpClient.controlEncoding = "UTF-8"
        ftpClient.connectTimeout = config.connectTimeout
        try {
            ftpClient.connect(config.host, config.port)
            val replyCode = ftpClient.replyCode
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect()
                throw InnerException("FTPServer refused connection,replyCode:${replyCode}")
            }
            if (!ftpClient.login(config.username, config.password)) {
                throw InnerException("FTPClient login failed,replyString:${ftpClient.replyString}")
            }
            ftpClient.bufferSize = config.bufferSize
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
        } catch (e: IOException) {
            throw InnerException("create ftp connection failed", e)
        }
        return ftpClient
    }

    override fun wrap(obj: FTPClient): PooledObject<FTPClient> {
        return DefaultPooledObject(obj)
    }


    override fun destroyObject(p: PooledObject<FTPClient>) {
        val ftpClient = p.`object`
        try {
            if (ftpClient.isConnected) {
                ftpClient.logout()
            }
        } catch (io: IOException) {
            logger.error("ftp client logout failed...{}", io);
        } finally {
            try {
                ftpClient.disconnect()
            } catch (io: IOException) {
                logger.error("ftp client logout failed...{}", io);
            }
        }
    }

    override fun validateObject(p: PooledObject<FTPClient>): Boolean {
        try {
            val ftpClient: FTPClient = p.`object`
            ftpClient.isAvailable
            return ftpClient.sendNoOp()
        } catch (e: IOException) {
            logger.error("failed to validate client: {}", e);
        }
        return false
    }
}