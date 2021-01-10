package github.afezeria.hymn.oss.ftp

import org.apache.commons.net.ftp.FTPClient

/**
 * @author afezeria
 */
fun FTPClient.createDirIfNotExist(path: String) {
    val cpath = path.trimEnd('/')
    val workDir = printWorkingDirectory()
    if (workDir == cpath) return
    if (workDir.startsWith(cpath)) return
    if (cpath.startsWith(workDir)) {
        val dirs = cpath.removePrefix(workDir).split('/')
        TODO()
    } else {
        changeWorkingDirectory("/")
        val dirs = cpath.split('/')
        TODO()
    }
}

